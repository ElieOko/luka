import type { AppDestination, UserSession } from '@/domain/models'
import { isAuthenticated } from '@/domain/models'

export function resolveDestination(session: UserSession | null, welcomeConsumed: boolean): AppDestination {
  if (!isAuthenticated(session)) return welcomeConsumed ? 'auth' : 'welcome'
  const profile = session!.profile
  if (!profile.profession && !profile.cityName) return 'profession'
  if (!profile.countryCode || !profile.regionId) return 'location'
  if (!profile.analysisLaunched) return 'analysis'
  return 'home'
}

export function demandFrom(offers: { profession: { id: string; title: string; family: string; tagline: string; emoji: string; imageName: string } }[]) {
  if (!offers.length) return []
  const grouped = new Map<string, typeof offers>()
  for (const offer of offers) {
    const list = grouped.get(offer.profession.id) ?? []
    list.push(offer)
    grouped.set(offer.profession.id, list)
  }
  const total = offers.length
  return [...grouped.values()]
    .sort((a, b) => b.length - a.length)
    .map((list) => {
      const profession = list[0]!.profession
      const share = Math.max(1, Math.round((list.length * 100) / total))
      return {
        profession,
        openings: list.length,
        sharePercent: share,
        trend: `${list.length} offres`,
      }
    })
}
