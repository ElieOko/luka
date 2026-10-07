<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import LukaLogo from '@/components/brand/LukaLogo.vue'
import LukaButton from '@/components/ui/LukaButton.vue'
import OtpBoxes from '@/components/ui/OtpBoxes.vue'
import { ACCOUNT_KINDS, type AccountKindId } from '@/domain/models'
import { useSessionStore } from '@/stores/session'

const session = useSessionStore()
const router = useRouter()

const step = ref<'id' | 'otp'>('id')
const input = ref('')
const otp = ref('')
const newAccount = ref(false)
const accountKind = ref<AccountKindId | null>(null)
const privacyAccepted = ref(false)
const showPrivacy = ref(false)
const loading = ref(false)
const error = ref<string | null>(null)
const info = ref<string | null>(null)
const phoneShown = ref('')

const headline = computed(() => {
  if (step.value === 'otp') return 'Confirme que c’est toi.'
  return newAccount.value ? 'Crée ton compte.' : 'Entre sans mot de passe.'
})

const cta = computed(() => {
  if (loading.value) return 'Un instant…'
  if (step.value === 'id' && newAccount.value) return 'Créer le compte'
  if (step.value === 'id') return 'Recevoir le code'
  return 'Continuer'
})

async function submit() {
  error.value = null
  info.value = null
  if (step.value === 'id') {
    if (newAccount.value && !accountKind.value) {
      error.value = 'Choisis un compte apprenant ou professionnel.'
      return
    }
    if (newAccount.value && !privacyAccepted.value) {
      error.value = 'Accepte la politique de confidentialité pour continuer.'
      return
    }
    loading.value = true
    try {
      const result = await session.requestOtp(input.value, newAccount.value, accountKind.value)
      if (result.kind === 'signed-in') {
        await router.replace('/app/accueil')
      } else {
        phoneShown.value = result.phone
        step.value = 'otp'
        info.value = 'Code envoyé par SMS.'
      }
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'Impossible d’envoyer le code.'
    } finally {
      loading.value = false
    }
    return
  }
  loading.value = true
  try {
    await session.verifyOtp(otp.value)
    await router.replace('/app/accueil')
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Code invalide.'
  } finally {
    loading.value = false
  }
}

async function resend() {
  loading.value = true
  error.value = null
  try {
    await session.resendOtp()
    otp.value = ''
    info.value = 'Nouveau code envoyé.'
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Impossible de renvoyer.'
  } finally {
    loading.value = false
  }
}

function toggleMode() {
  newAccount.value = !newAccount.value
  error.value = null
  info.value = null
  step.value = 'id'
  otp.value = ''
  accountKind.value = null
  privacyAccepted.value = false
}
</script>

<template>
  <main class="auth">
    <section v-if="showPrivacy" class="privacy">
      <button class="back" type="button" @click="showPrivacy = false">← Retour</button>
      <h1>Politique de confidentialité</h1>
      <p class="muted">Dernière mise à jour : septembre 2026</p>
      <p>Luka est une application d’orientation et d’emploi centrée sur la RDC. Nous n’utilisons tes données que pour te connecter aux offres, analyses et conseils.</p>
    </section>
    <template v-else>
      <div class="body">
        <LukaLogo />
        <h1>{{ headline }}</h1>
        <p class="lead">
          {{
            step === 'id'
              ? 'Numéro congolais. Un code SMS, puis tu restes connecté.'
              : `Code envoyé par SMS à ${phoneShown}.`
          }}
        </p>
        <template v-if="step === 'id'">
          <label>
            Téléphone
            <input v-model="input" type="tel" placeholder="+243 81 000 0000" autocomplete="tel" />
          </label>
          <template v-if="newAccount">
            <h2>Quel compte ?</h2>
            <button
              v-for="kind in ACCOUNT_KINDS"
              :key="kind.id"
              type="button"
              class="kind"
              :class="{ on: accountKind === kind.id }"
              @click="accountKind = kind.id"
            >
              <strong>{{ kind.title }}</strong>
              <span>{{ kind.subtitle }}</span>
            </button>
            <label class="check">
              <input v-model="privacyAccepted" type="checkbox" />
              <span>
                J’accepte la politique de confidentialité.
                <button type="button" class="link" @click="showPrivacy = true">Lire la politique de confidentialité</button>
              </span>
            </label>
          </template>
          <button class="ghost" type="button" @click="toggleMode">
            {{ newAccount ? 'Déjà inscrit ? Se connecter' : 'Pas encore de compte ? Créer un compte' }}
          </button>
        </template>
        <template v-else>
          <h2>Code à 6 chiffres</h2>
          <OtpBoxes v-model="otp" />
          <button class="ghost" type="button" :disabled="loading" @click="resend">Renvoyer le code</button>
        </template>
        <p v-if="info && !error" class="info">{{ info }}</p>
        <p v-if="error" class="error">{{ error }}</p>
      </div>
      <div class="cta">
        <LukaButton :loading="loading" :disabled="loading" @click="submit">{{ cta }}</LukaButton>
      </div>
    </template>
  </main>
</template>

<style scoped>
.auth {
  min-height: 100dvh;
  display: flex;
  flex-direction: column;
  background: var(--luka-cream);
}
.body,
.privacy {
  flex: 1;
  padding: 28px 24px 16px;
  max-width: 560px;
  width: 100%;
  margin: 0 auto;
}
h1 {
  font-size: 30px;
  margin: 28px 0 8px;
  font-weight: 800;
}
.lead,
.muted {
  color: var(--luka-muted);
  margin-top: 0;
}
label {
  display: block;
  font-weight: 600;
  margin: 20px 0 8px;
}
input[type='tel'] {
  width: 100%;
  margin-top: 8px;
  height: 56px;
  border-radius: 16px;
  border: 1px solid var(--luka-outline);
  padding: 0 16px;
  background: #fff;
  font-size: 16px;
}
h2 {
  margin: 22px 0 10px;
  font-size: 16px;
}
.kind {
  width: 100%;
  text-align: left;
  border: 1px solid transparent;
  border-radius: 18px;
  padding: 14px;
  margin-bottom: 8px;
  background: var(--luka-mist);
  display: grid;
  gap: 4px;
  transition: background 0.2s, color 0.2s, transform 0.2s var(--ease);
}
.kind span {
  color: var(--luka-muted);
  font-size: 13px;
}
.kind.on {
  background: var(--luka-red);
  color: #fff;
}
.kind.on span {
  color: rgba(255, 255, 255, 0.9);
}
.check {
  display: flex;
  gap: 10px;
  align-items: flex-start;
  font-weight: 400;
}
.link,
.ghost {
  background: none;
  border: 0;
  color: var(--luka-red);
  font-weight: 700;
  padding: 8px 0;
}
.cta {
  padding: 16px 24px 28px;
  max-width: 560px;
  width: 100%;
  margin: 0 auto;
}
.error {
  color: var(--luka-red-deep);
}
.info {
  color: var(--luka-muted);
}
.back {
  border: 0;
  background: none;
  font-weight: 700;
  padding: 0 0 12px;
}
</style>
