import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import { lukaApi } from '@/api/lukaApi'
import { emptyProfile, mergeUser, requiresOtp, toAuthTokens } from '@/api/mappers'
import { setAccessToken } from '@/api/http'
import type { AccountKindId, AuthIdentifier, Profession, UserProfile, UserSession } from '@/domain/models'
import { normalizePhone } from '@/utils/phone'

export const useSessionStore = defineStore(
  'session',
  () => {
    const session = ref<UserSession | null>(null)
    const welcomeConsumed = ref(false)
    const favoriteNewsIds = ref<string[]>([])
    const pending = ref<{
      phone: string
      newAccount: boolean
      accountKind: AccountKindId | null
    } | null>(null)

    const profile = computed(() => session.value?.profile ?? null)
    const isLearner = computed(() => profile.value?.accountKind === 'learner')
    const isAuthenticated = computed(() => Boolean(session.value?.token))

    function applySession(next: UserSession | null) {
      session.value = next
      setAccessToken(next?.token ?? null)
    }

    function persistProfile(transform: (current: UserProfile) => UserProfile) {
      if (!session.value) return
      applySession({ ...session.value, profile: transform(session.value.profile) })
    }

    async function persistFromAuth(phone: string, payload: unknown, skipOnboarding: boolean) {
      const tokens = toAuthTokens(payload)
      const identifier: AuthIdentifier = { channel: 'PHONE', value: tokens.user.phone || phone }
      const pendingKind = pending.value?.accountKind
      const merged = mergeUser(tokens.user, identifier, session.value?.profile)
      const profileNext: UserProfile = {
        ...merged,
        id: tokens.user.userId?.toString() || session.value?.profile.id || crypto.randomUUID(),
        welcomeSeen: true,
        analysisLaunched: skipOnboarding || session.value?.profile.analysisLaunched === true,
        accountKind: pendingKind ?? session.value?.profile.accountKind ?? 'professional',
      }
      applySession({
        token: tokens.accessToken,
        refreshToken: tokens.refreshToken,
        profile: profileNext,
      })
      await hydrateRemoteProfile()
    }

    async function requestOtp(raw: string, newAccount: boolean, accountKind: AccountKindId | null) {
      const phone = normalizePhone(raw)
      pending.value = { phone, newAccount, accountKind }
      const payload = newAccount
        ? await lukaApi.registerPhone(phone, accountKind === 'learner')
        : await lukaApi.requestLoginOtp(phone)
      if (!requiresOtp(payload.data)) {
        await persistFromAuth(phone, payload.data, true)
        return { kind: 'signed-in' as const }
      }
      return { kind: 'otp' as const, phone }
    }

    async function verifyOtp(code: string) {
      const current = pending.value
      if (!current) throw new Error('Demande d’abord un code.')
      const payload = current.newAccount
        ? await lukaApi.verifyOtp(current.phone, code)
        : await lukaApi.verifyLoginOtp(current.phone, code)
      await persistFromAuth(current.phone, payload.data, false)
    }

    async function resendOtp() {
      const current = pending.value
      if (!current) throw new Error('Demande d’abord un code.')
      if (current.newAccount) await lukaApi.resendOtp(current.phone)
      else await lukaApi.requestLoginOtp(current.phone)
    }

    async function hydrateRemoteProfile() {
      if (!session.value?.token) return
      try {
        const dto = await lukaApi.getProfile()
        persistProfile((current) => mergeUser(dto, current.identifier, current))
      } catch {
        /* offline / 401 handled by UI */
      }
      try {
        const prefs = await lukaApi.getPreferences()
        const domainId = prefs.domainIds[0]
        if (domainId != null) persistProfile((current) => ({ ...current, domainId }))
      } catch {
        /* preferences optional if the account has none yet */
      }
    }

    async function saveProfession(profession: Profession, domainId: number | null, tradeTitle: string) {
      persistProfile((current) => ({ ...current, profession, domainId, tradeTitle }))
      if (domainId != null) {
        try {
          await lukaApi.savePreferences([domainId])
        } catch {
          /* keep local métier even if prefs sync fails */
        }
      }
    }

    async function saveLocation(regionId: string, cityName: string | null) {
      persistProfile((current) => ({
        ...current,
        countryCode: 'CD',
        regionId,
        cityName,
      }))
      const current = session.value?.profile
      if (!current) return
      try {
        const dto = await lukaApi.updateProfile({
          fullName: current.displayName || null,
          email: current.email.includes('@') ? current.email : null,
          city: cityName,
          country: 'CD',
        })
        persistProfile((profile) => mergeUser(dto, profile.identifier, profile))
      } catch {
        /* local city still saved */
      }
    }

    function markAnalysisLaunched() {
      persistProfile((current) => ({ ...current, analysisLaunched: true }))
    }

    function consumeWelcome() {
      welcomeConsumed.value = true
    }

    function saveAccountKind(kind: AccountKindId) {
      persistProfile((current) => ({ ...current, accountKind: kind }))
    }

    async function updateProfile(displayName: string, bio: string, email: string, cityName: string | null) {
      persistProfile((current) => ({
        ...current,
        displayName,
        bio,
        email: email || current.email,
        cityName: cityName ?? current.cityName,
        countryCode: current.countryCode ?? 'CD',
      }))
      const latest = session.value?.profile
      if (!latest) return
      if (!latest.profileCompleted && latest.email.includes('@')) {
        try {
          const dto = await lukaApi.completeProfile(displayName, latest.email)
          persistProfile((current) => mergeUser(dto, current.identifier, current))
        } catch {
          /* continue with update */
        }
      }
      const dto = await lukaApi.updateProfile({
        fullName: displayName,
        email: latest.email.includes('@') ? latest.email : null,
        city: latest.cityName,
        country: latest.countryCode ?? 'CD',
      })
      persistProfile((current) => ({ ...mergeUser(dto, current.identifier, current), bio }))
    }

    function saveCv(fileName: string, mimeType: string) {
      persistProfile((current) => ({ ...current, cvFileName: fileName, cvMime: mimeType }))
    }

    function selectPlan(planId: string) {
      persistProfile((current) => ({
        ...current,
        planId,
        visibleToRecruiters: planId === 'professional',
      }))
    }

    function toggleNewsFavorite(id: string) {
      if (!id) return
      favoriteNewsIds.value = favoriteNewsIds.value.includes(id)
        ? favoriteNewsIds.value.filter((item) => item !== id)
        : [...favoriteNewsIds.value, id]
    }

    function logout() {
      applySession(null)
      pending.value = null
      welcomeConsumed.value = true
    }

    function restoreToken() {
      setAccessToken(session.value?.token ?? null)
    }

    return {
      session,
      welcomeConsumed,
      favoriteNewsIds,
      pending,
      profile,
      isLearner,
      isAuthenticated,
      applySession,
      emptyGuest: emptyProfile,
      requestOtp,
      verifyOtp,
      resendOtp,
      hydrateRemoteProfile,
      saveProfession,
      saveLocation,
      markAnalysisLaunched,
      consumeWelcome,
      saveAccountKind,
      updateProfile,
      saveCv,
      selectPlan,
      toggleNewsFavorite,
      logout,
      restoreToken,
    }
  },
  {
    persist: {
      pick: ['session', 'welcomeConsumed', 'favoriteNewsIds', 'pending'],
    },
  },
)
