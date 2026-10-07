import type { AccountKindId, SubscriptionPlan, UserProfile } from '@/domain/models'

export const PLANS = {
  STARTER: 'starter',
  STUDENT: 'student',
  PROFESSIONAL: 'professional',
} as const

export const STARTER_PLAN: SubscriptionPlan = {
  id: PLANS.STARTER,
  name: 'Gratuit',
  priceLabel: '0 $',
  period: 'toujours',
  highlight: false,
  perks: [
    '1 métier suivi, choisi un par un',
    'Aperçu des tendances',
    '1 conseil de carrière',
    '5 offres (compte professionnel)',
  ],
  apiId: 0,
  usdAmount: 0,
}

export const STUDENT_PLAN: SubscriptionPlan = {
  id: PLANS.STUDENT,
  name: 'Étudiant',
  priceLabel: '3 $',
  period: '/ mois',
  highlight: false,
  perks: [
    'Analyses des tendances du marché',
    'News MIT et universités',
    'Orientation complète',
    'Conseils réguliers',
  ],
  apiId: 1,
  usdAmount: 3,
}

export const PROFESSIONAL_PLAN: SubscriptionPlan = {
  id: PLANS.PROFESSIONAL,
  name: 'Professionnel',
  priceLabel: '5 $',
  period: '/ mois',
  highlight: true,
  perks: [
    'Toutes les offres RDC',
    'Analyse de CV',
    'Analyse des offres',
    'Marché en temps réel',
    'Profil proposé aux entreprises, actif 3 mois',
  ],
  apiId: 2,
  usdAmount: 5,
}

export const ALL_PLANS = [STARTER_PLAN, STUDENT_PLAN, PROFESSIONAL_PLAN]
export const PAID_PLANS = [STUDENT_PLAN, PROFESSIONAL_PLAN]

export function planById(id: string): SubscriptionPlan {
  if (id === PLANS.STUDENT) return STUDENT_PLAN
  if (['professional', 'plus', 'pro', 'elite'].includes(id)) return PROFESSIONAL_PLAN
  return STARTER_PLAN
}

export function planByApiId(id: number): SubscriptionPlan {
  return PAID_PLANS.find((plan) => plan.apiId === id) ?? STUDENT_PLAN
}

export function isProfessionalPlan(id: string): boolean {
  return planById(id).id === PLANS.PROFESSIONAL
}

export function isStudentPlan(id: string): boolean {
  return planById(id).id === PLANS.STUDENT
}

export function learnerUnlocked(profile: UserProfile): boolean {
  return profile.isPremium || isStudentPlan(profile.planId) || isProfessionalPlan(profile.planId)
}

export function proUnlocked(profile: UserProfile): boolean {
  return profile.isPremium || isProfessionalPlan(profile.planId)
}

export function offerPreviewLimit(profile: UserProfile): number {
  return proUnlocked(profile) ? Number.POSITIVE_INFINITY : 5
}

export function advicePreviewLimit(profile: UserProfile): number {
  return learnerUnlocked(profile) ? Number.POSITIVE_INFINITY : 1
}

export function suggestedPlan(kind: AccountKindId): SubscriptionPlan {
  return kind === 'learner' ? STUDENT_PLAN : PROFESSIONAL_PLAN
}
