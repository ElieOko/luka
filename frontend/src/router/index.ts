import { createRouter, createWebHistory } from 'vue-router'
import { useSessionStore } from '@/stores/session'
import { resolveDestination } from '@/utils/destination'

const router = createRouter({
  history: createWebHistory(),
  scrollBehavior() {
    return { top: 0 }
  },
  routes: [
    { path: '/', name: 'welcome', component: () => import('@/views/WelcomeView.vue'), meta: { guest: true } },
    { path: '/auth', name: 'auth', component: () => import('@/views/AuthView.vue'), meta: { guest: true } },
    { path: '/setup/metier', name: 'profession', component: () => import('@/views/ProfessionView.vue'), meta: { auth: true } },
    { path: '/setup/ville', name: 'location', component: () => import('@/views/LocationView.vue'), meta: { auth: true } },
    { path: '/setup/analyse', name: 'analysis', component: () => import('@/views/AnalysisView.vue'), meta: { auth: true } },
    {
      path: '/app',
      component: () => import('@/views/shell/AppShell.vue'),
      meta: { auth: true, home: true },
      children: [
        { path: '', redirect: '/app/accueil' },
        { path: 'accueil', name: 'home', component: () => import('@/views/HomeView.vue') },
        { path: 'offres', name: 'offers', component: () => import('@/views/OffersView.vue') },
        { path: 'news', name: 'news', component: () => import('@/views/NewsView.vue') },
        { path: 'news/:id', name: 'news-article', component: () => import('@/views/NewsArticleView.vue') },
        { path: 'tendances', name: 'trends', component: () => import('@/views/TrendsView.vue') },
        { path: 'orientation', name: 'orientation', component: () => import('@/views/OrientationView.vue') },
        { path: 'analyses', name: 'insights', component: () => import('@/views/InsightsView.vue') },
        { path: 'profil', name: 'profile', component: () => import('@/views/ProfileView.vue') },
        { path: 'profil/edit', name: 'profile-edit', component: () => import('@/views/ProfileEditView.vue') },
        { path: 'abonnement', name: 'subscription', component: () => import('@/views/SubscriptionView.vue') },
        { path: 'confidentialite', name: 'privacy', component: () => import('@/views/PrivacyView.vue') },
      ],
    },
    { path: '/:pathMatch(.*)*', redirect: '/' },
  ],
})

const destinationRoute: Record<ReturnType<typeof resolveDestination>, string> = {
  welcome: '/',
  auth: '/auth',
  profession: '/setup/metier',
  location: '/setup/ville',
  analysis: '/setup/analyse',
  home: '/app/accueil',
}

router.beforeEach((to) => {
  const session = useSessionStore()
  session.restoreToken()
  const destination = resolveDestination(session.session, session.welcomeConsumed)
  const target = destinationRoute[destination]

  if (destination !== 'home' && to.path.startsWith('/app')) return target
  if (destination === 'home' && (to.meta.guest || to.path.startsWith('/setup'))) return '/app/accueil'
  if (destination === 'welcome' && to.name !== 'welcome') return '/'
  if (destination === 'auth' && to.name !== 'auth' && to.name !== 'privacy') return '/auth'
  if (destination === 'profession' && to.name !== 'profession') return '/setup/metier'
  if (destination === 'location' && to.name !== 'location') return '/setup/ville'
  if (destination === 'analysis' && to.name !== 'analysis') return '/setup/analyse'
  return true
})

export default router
