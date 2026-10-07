import { deviceSerial } from '@/utils/device'
import { send } from './http'
import { required } from './mappers'
import type {
  AbonnementDto,
  ApiEnvelope,
  CongoCityDto,
  DeviseDto,
  FlexPaymentResponse,
  IdentifiantRequest,
  JobOfferPageDto,
  LooseEnvelope,
  PaiementDto,
  PaymentInitRequest,
  PhoneRegisterRequest,
  ProfileCompletionRequest,
  SaveUserPreferencesRequest,
  SearchDomainDto,
  UpdateProfileRequest,
  UserDto,
  UserPreferencesDto,
  VerifyRequest,
} from './types'

/**
 * Miroir de `LukaApi` (KMP) : mêmes chemins, mêmes corps,
 * y compris GET/PUT `/api/v1/auth/preferences`.
 */
export const lukaApi = {
  registerPhone(phone: string, isStudent = false) {
    const body: PhoneRegisterRequest = { phone, isStudent, buildSerial: deviceSerial() }
    return send<LooseEnvelope>({ method: 'POST', url: '/api/v1/public/auth/register-phone', data: body })
  },

  verifyOtp(identifier: string, code: string) {
    const body: VerifyRequest = { identifier, code, buildSerial: deviceSerial() }
    return send<LooseEnvelope>({ method: 'POST', url: '/api/v1/public/auth/verify-otp', data: body })
  },

  requestLoginOtp(identifier: string) {
    const body: IdentifiantRequest = { identifier, buildSerial: deviceSerial() }
    return send<LooseEnvelope>({ method: 'POST', url: '/api/v1/public/auth/login/request-otp', data: body })
  },

  verifyLoginOtp(identifier: string, code: string) {
    const body: VerifyRequest = { identifier, code, buildSerial: deviceSerial() }
    return send<LooseEnvelope>({ method: 'POST', url: '/api/v1/public/auth/login/verify-otp', data: body })
  },

  resendOtp(identifier: string) {
    const body: IdentifiantRequest = { identifier, buildSerial: deviceSerial() }
    return send<LooseEnvelope>({ method: 'POST', url: '/api/v1/public/auth/resend-otp', data: body })
  },

  async completeProfile(fullName: string, email: string) {
    const body: ProfileCompletionRequest = { fullName, email }
    return required(
      await send<ApiEnvelope<UserDto>>({ method: 'POST', url: '/api/v1/auth/complete-profile', data: body }),
    )
  },

  async getProfile() {
    return required(await send<ApiEnvelope<UserDto>>({ method: 'GET', url: '/api/v1/auth/profile' }))
  },

  async updateProfile(payload: UpdateProfileRequest) {
    return required(
      await send<ApiEnvelope<UserDto>>({ method: 'PUT', url: '/api/v1/auth/profile', data: payload }),
    )
  },

  async savePreferences(domainIds: number[]) {
    const body: SaveUserPreferencesRequest = { domainIds }
    return required(
      await send<ApiEnvelope<UserPreferencesDto>>({
        method: 'PUT',
        url: '/api/v1/auth/preferences',
        data: body,
      }),
    )
  },

  async getPreferences() {
    return required(
      await send<ApiEnvelope<UserPreferencesDto>>({ method: 'GET', url: '/api/v1/auth/preferences' }),
    )
  },

  async listDomains() {
    const envelope = await send<ApiEnvelope<SearchDomainDto[]>>({
      method: 'GET',
      url: '/api/v1/public/catalog/domains',
    })
    return envelope.data ?? []
  },

  async listCities() {
    const envelope = await send<ApiEnvelope<CongoCityDto[]>>({
      method: 'GET',
      url: '/api/v1/public/catalog/cities',
    })
    return envelope.data ?? []
  },

  async listOffers(params: {
    city?: string
    province?: string
    domainIds?: number[]
    page?: number
    size?: number
    status?: string
  } = {}) {
    const envelope = await send<ApiEnvelope<JobOfferPageDto>>({
      method: 'GET',
      url: '/api/offres',
      params: {
        city: params.city || undefined,
        province: params.province || undefined,
        ...(params.domainIds?.length ? { domainId: params.domainIds } : {}),
        page: params.page ?? 0,
        size: params.size ?? 50,
        status: params.status ?? 'ECHEANCE_NON_DEPASSEE',
      },
    })
    return envelope.data ?? { content: [], page: 0, size: 0, totalElements: 0 }
  },

  async listAbonnements() {
    const envelope = await send<ApiEnvelope<AbonnementDto[]>>({
      method: 'GET',
      url: '/api/v1/public/abonnements',
    })
    return envelope.data ?? []
  },

  async listDevises() {
    const envelope = await send<ApiEnvelope<DeviseDto[]>>({
      method: 'GET',
      url: '/api/v1/public/devises',
    })
    return envelope.data ?? []
  },

  async payMobileMoney(abonnementId: number, devise: string, phone: string) {
    const body: PaymentInitRequest = { abonnementId, devise, phone }
    return required(
      await send<ApiEnvelope<FlexPaymentResponse>>({
        method: 'POST',
        url: '/api/v1/auth/payment/mobile-money',
        data: body,
      }),
    )
  },

  async payWithCard(abonnementId: number, devise: string, phone: string) {
    const body: PaymentInitRequest = { abonnementId, devise, phone }
    return required(
      await send<ApiEnvelope<FlexPaymentResponse>>({
        method: 'POST',
        url: '/api/v1/auth/payment/card',
        data: body,
      }),
    )
  },

  async paymentHistory() {
    const envelope = await send<ApiEnvelope<PaiementDto[]>>({
      method: 'GET',
      url: '/api/v1/auth/payment/history',
    })
    return envelope.data ?? []
  },
}
