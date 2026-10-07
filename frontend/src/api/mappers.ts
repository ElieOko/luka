import type { CongoCityDto, SearchDomainDto, StoredJobOfferDto, UserDto } from './types'
import type { AuthIdentifier, City, JobOffer, TradeChip, UserProfile } from '@/domain/models'
import { regionIdFor, slug } from '@/data/catalog'
import { inferProfession, localTrades, professionFromCatalogName } from '@/data/professions'
import { PLANS } from '@/data/plans'

export interface AuthTokens {
  accessToken: string
  refreshToken: string
  user: UserDto
}

function asRecord(value: unknown): Record<string, unknown> | null {
  return value && typeof value === 'object' && !Array.isArray(value)
    ? (value as Record<string, unknown>)
    : null
}

function str(record: Record<string, unknown>, ...keys: string[]): string | undefined {
  for (const key of keys) {
    const value = record[key]
    if (typeof value === 'string' && value.trim()) return value
  }
  return undefined
}

function num(record: Record<string, unknown>, ...keys: string[]): number | undefined {
  for (const key of keys) {
    const value = record[key]
    if (typeof value === 'number') return value
    if (typeof value === 'string' && value.trim()) {
      const parsed = Number(value)
      if (!Number.isNaN(parsed)) return parsed
    }
  }
  return undefined
}

function bool(record: Record<string, unknown>, key: string): boolean | undefined {
  const value = record[key]
  return typeof value === 'boolean' ? value : undefined
}

export function toAuthTokens(data: unknown): AuthTokens {
  const root = asRecord(data)
  if (!root) throw new Error('Réponse de connexion inattendue.')
  const nested = asRecord(root.user) ?? asRecord(root.profile)
  const access =
    str(root, 'accessToken', 'token', 'access_token') ??
    (nested ? str(nested, 'accessToken', 'token') : undefined)
  if (!access) throw new Error('Jeton manquant. Réessaie le code.')
  const refresh =
    str(root, 'refreshToken', 'refresh_token') ??
    (nested ? str(nested, 'refreshToken', 'refresh_token') : undefined) ??
    ''
  const profileSource = nested ?? root
  return {
    accessToken: access,
    refreshToken: refresh,
    user: {
      userId: num(profileSource, 'userId', 'id') ?? null,
      email: str(profileSource, 'email') ?? null,
      username: str(profileSource, 'username', 'fullName', 'name') ?? null,
      phone: str(profileSource, 'phone') ?? null,
      city: str(profileSource, 'city') ?? null,
      country: str(profileSource, 'country') ?? null,
      isPremium: bool(profileSource, 'isPremium') ?? false,
      isCertified: bool(profileSource, 'isCertified') ?? bool(profileSource, 'certified') ?? false,
      profileCompleted: bool(profileSource, 'profileCompleted') ?? false,
    },
  }
}

export function requiresOtp(data: unknown): boolean {
  const root = asRecord(data)
  if (!root) return true
  if (typeof root.requiresOtp === 'boolean') return root.requiresOtp
  if (root.otpBypassed === true) return false
  if (typeof root.loginFlow === 'string' && root.loginFlow.toLowerCase() === 'direct') return false
  return str(root, 'accessToken', 'token', 'access_token') == null
}

function defaultName(identifier: AuthIdentifier): string {
  if (identifier.channel === 'EMAIL') {
    const local = identifier.value.split('@')[0] ?? 'Talent'
    return local.charAt(0).toUpperCase() + local.slice(1)
  }
  return 'Talent Luka'
}

export function emptyProfile(identifier: AuthIdentifier): UserProfile {
  return {
    id: '',
    displayName: defaultName(identifier),
    bio: 'Je cherche mon prochain rôle en RDC.',
    photoUrl: '',
    identifier,
    profession: null,
    countryCode: null,
    regionId: null,
    planId: PLANS.STARTER,
    extraProfessionIds: [],
    analysisLaunched: false,
    welcomeSeen: true,
    visibleToRecruiters: false,
    cvFileName: '',
    cvMime: '',
    email: '',
    cityName: null,
    domainId: null,
    profileCompleted: false,
    isCertified: false,
    isPremium: false,
    accountKind: 'professional',
    tradeTitle: '',
  }
}

export function mergeUser(dto: UserDto, identifier: AuthIdentifier, existing?: UserProfile | null): UserProfile {
  const phone = dto.phone?.trim()
  const mail = dto.email?.trim()
  const resolved: AuthIdentifier = phone
    ? { channel: 'PHONE', value: phone }
    : mail
      ? { channel: 'EMAIL', value: mail }
      : identifier
  const fallback = existing?.displayName ?? ''
  const name = dto.username?.trim() || fallback || defaultName(resolved)
  const base = existing ?? emptyProfile(resolved)
  return {
    ...base,
    id: dto.userId?.toString() ?? existing?.id ?? '',
    displayName: name,
    identifier: resolved,
    email: mail || existing?.email || '',
    cityName: dto.city?.trim() || existing?.cityName || null,
    countryCode: dto.country?.trim() || existing?.countryCode || null,
    regionId: dto.city?.trim() ? regionIdFor(dto.city, null) : existing?.regionId ?? null,
    profileCompleted: dto.profileCompleted ?? false,
    isPremium: dto.isPremium ?? false,
    planId: dto.isPremium ? PLANS.PROFESSIONAL : (existing?.planId ?? PLANS.STARTER),
    isCertified: Boolean(dto.isCertified || dto.certified),
  }
}

export function cityFromDto(dto: CongoCityDto): City {
  return {
    id: String(dto.id),
    name: dto.name,
    regionId: slug(dto.province || dto.name),
  }
}

export function tradesFromDomains(domains: SearchDomainDto[]): TradeChip[] {
  const fromApi = domains
    .filter((domain) => domain.isActive !== false)
    .flatMap((domain) =>
      (domain.professions ?? [])
        .filter((profession) => profession.isActive !== false)
        .map((profession) => {
          const mapped = professionFromCatalogName(profession.name)
          return {
            profession: mapped,
            title: profession.name,
            family: domain.name,
            tagline: mapped.tagline,
            domainId: domain.id,
          } satisfies TradeChip
        }),
    )
  const families = new Set(fromApi.map((chip) => chip.family.toLowerCase()))
  const rest = localTrades().filter((chip) => !families.has(chip.family.toLowerCase()))
  return [...fromApi, ...rest]
}

function parseEpoch(value?: string | null): number {
  if (!value) return 0
  const date = Date.parse(value)
  return Number.isNaN(date) ? 0 : date
}

function cleanText(value?: string | null): string {
  const text = value?.trim() ?? ''
  if (!text || text === 'null' || text === 'undefined') return ''
  return text
}

export function offerFromDto(dto: StoredJobOfferDto): JobOffer {
  const cityName = cleanText(dto.city?.split(',')[0]) || 'RDC'
  const apply = cleanText(dto.applicationUrl) || dto.advertisementUrl || ''
  const remote = [dto.opportunityType, dto.city, dto.title].some(
    (value) =>
      value?.toLowerCase().includes('télétravail') ||
      value?.toLowerCase().includes('teletravail') ||
      value?.toLowerCase().includes('remote'),
  )
  const skills = dto.skills ?? []
  return {
    id: String(dto.id),
    title: dto.title,
    company: dto.employer,
    profession: inferProfession(dto.title, skills, dto.searchAgent),
    regionId: regionIdFor(cityName, dto.province),
    city: cityName,
    contract: cleanText(dto.contractType) || cleanText(dto.opportunityType) || 'Emploi',
    salary: '',
    summary:
      skills.map(cleanText).filter(Boolean).slice(0, 6).join(' · ') ||
      [cleanText(dto.opportunityType), cleanText(dto.city)].filter(Boolean).join(' · '),
    applyUrl: apply,
    postedAtEpochMs: parseEpoch(dto.publicationDate) || parseEpoch(dto.collectedAt),
    isRemote: Boolean(remote),
  }
}

export function required<T>(envelope: { message: string; data: T | null }): T {
  if (envelope.data == null) throw new Error(envelope.message || 'Réponse inattendue du serveur.')
  return envelope.data
}
