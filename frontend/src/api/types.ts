export interface ApiEnvelope<T> {
  message: string
  data: T | null
}

export interface LooseEnvelope {
  message: string
  data: unknown
}

export interface PhoneRegisterRequest {
  phone: string
  isStudent?: boolean
  buildSerial?: string | null
}

export interface VerifyRequest {
  identifier: string
  code: string
  buildSerial?: string | null
}

export interface IdentifiantRequest {
  identifier: string
  buildSerial?: string | null
}

export interface ProfileCompletionRequest {
  fullName: string
  email: string
}

export interface UpdateProfileRequest {
  fullName?: string | null
  email?: string | null
  city?: string | null
  country?: string | null
}

export interface SaveUserPreferencesRequest {
  domainIds: number[]
}

export interface UserDto {
  userId?: number | null
  email?: string | null
  username?: string | null
  phone?: string | null
  city?: string | null
  country?: string | null
  isPremium?: boolean
  isCertified?: boolean
  profileCompleted?: boolean
  certified?: boolean | null
}

export interface UserPreferencesDto {
  domainIds: number[]
  domains: SearchDomainDto[]
}

export interface SearchDomainDto {
  id: number
  name: string
  searchAgent?: number | null
  isActive?: boolean
  professions?: DomainProfessionDto[]
}

export interface DomainProfessionDto {
  id: number
  domainId: number
  name: string
  isActive?: boolean
}

export interface CongoCityDto {
  id: number
  name: string
  province?: string | null
  isActive?: boolean
}

export interface JobOfferPageDto {
  content: StoredJobOfferDto[]
  page: number
  size: number
  totalElements: number
}

export interface StoredJobOfferDto {
  id: number
  searchAgent?: number | null
  title: string
  employer: string
  country?: string | null
  city?: string | null
  province?: string | null
  opportunityType?: string | null
  contractType?: string | null
  skills?: string[]
  advertisementUrl?: string
  applicationUrl?: string | null
  status?: string | null
  publicationDate?: string | null
  collectedAt?: string | null
}

export interface AbonnementDto {
  id: number
  code?: string
  name?: string
  amountUsd?: number
  description?: string | null
  active?: boolean
}

export interface DeviseDto {
  id: number
  code: string
  name: string
  tauxLocal: number
}

export interface PaymentInitRequest {
  abonnementId: number
  devise: string
  phone: string
}

export interface FlexPaymentResponse {
  code?: string | null
  message?: string | null
  orderNumber?: string | null
  url?: string | null
  paymentAccepted?: boolean
}

export interface PaiementDto {
  id?: number | null
  userId?: number | null
  abonnementId: number
  reference?: string
  amount?: string
  devise?: string
  description?: string | null
  typePayment?: string
  status?: string
  createdAt?: string | null
  updatedAt?: string | null
}

export class ApiException extends Error {
  status: number
  constructor(message: string, status = 0) {
    super(message)
    this.name = 'ApiException'
    this.status = status
  }
}
