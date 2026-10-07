export type AccountKindId = 'learner' | 'professional'

export interface AccountKind {
  id: AccountKindId
  title: string
  subtitle: string
}

export const ACCOUNT_KINDS: AccountKind[] = [
  {
    id: 'learner',
    title: 'Apprenant',
    subtitle: 'Tendances, news MIT, orientation et conseils pour étudier ou te réorienter.',
  },
  {
    id: 'professional',
    title: 'Professionnel',
    subtitle: 'Offres, analyse de CV, analyse des offres et marché en temps réel.',
  },
]

export interface Profession {
  id: string
  title: string
  tagline: string
  emoji: string
  family: string
  imageName: string
}

export interface TradeChip {
  profession: Profession
  title: string
  family: string
  tagline: string
  domainId: number | null
}

export function tradeKey(chip: TradeChip): string {
  return `${chip.domainId ?? 'local'}::${chip.title}`
}

export interface Country {
  code: string
  name: string
  flag: string
}

export interface City {
  id: string
  name: string
  regionId: string
}

export interface JobOffer {
  id: string
  title: string
  company: string
  profession: Profession
  regionId: string
  city: string
  contract: string
  salary: string
  summary: string
  applyUrl: string
  postedAtEpochMs: number
  isRemote: boolean
}

export interface PlatformAd {
  id: string
  title: string
  subtitle: string
  cta: string
  imageName: string
}

export interface NewsItem {
  id: string
  title: string
  excerpt: string
  source: string
  imageName: string
  publishedAtEpochMs: number
  domain: string
  author: string
  body: string
  subscribed: boolean
}

export interface CareerAdvice {
  id: string
  title: string
  body: string
}

export interface DemandStat {
  profession: Profession
  openings: number
  sharePercent: number
  trend: string
}

export interface OrientationPersona {
  id: string
  title: string
  subtitle: string
  lens: string
  resultTitle: string
}

export interface NationalInsight {
  persona: OrientationPersona
  rank: number
  label: string
  sharePercent: number
  detail: string
}

export interface SubscriptionPlan {
  id: string
  name: string
  priceLabel: string
  period: string
  highlight: boolean
  perks: string[]
  apiId: number
  usdAmount: number
}

export interface AuthIdentifier {
  channel: 'PHONE' | 'EMAIL'
  value: string
}

export interface UserProfile {
  id: string
  displayName: string
  bio: string
  photoUrl: string
  identifier: AuthIdentifier
  profession: Profession | null
  countryCode: string | null
  regionId: string | null
  planId: string
  extraProfessionIds: string[]
  analysisLaunched: boolean
  welcomeSeen: boolean
  visibleToRecruiters: boolean
  cvFileName: string
  cvMime: string
  email: string
  cityName: string | null
  domainId: number | null
  profileCompleted: boolean
  isCertified: boolean
  isPremium: boolean
  accountKind: AccountKindId
  tradeTitle: string
}

export interface UserSession {
  token: string
  refreshToken: string
  profile: UserProfile
}

export type AppDestination = 'welcome' | 'auth' | 'profession' | 'location' | 'analysis' | 'home'

export function isAuthenticated(session: UserSession | null): boolean {
  return Boolean(session?.token)
}

export function firstName(profile: UserProfile | null | undefined): string {
  const name = profile?.displayName?.trim() ?? ''
  return name.split(' ')[0] || name || 'toi'
}

export function imageUrl(name: string): string {
  return `/images/${name}.jpg`
}
