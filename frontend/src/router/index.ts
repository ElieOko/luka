import { createRouter, createWebHistory } from 'vue-router'
import { useSessionStore } from '@/stores/session'
import { pathForDestination, resolveDestination } from '@/utils/destination'

const router = createRouter({
  history: createWebHistory(),
  scrollBehavior(to) {
    if (to.hash) return { el: to.hash, behavior: 'smooth' }
    return { top: 0 }
  },
  routes: [
    { path: '/', name: 'landing', component: () => import('@/views/LandingView.vue'), meta: { public: true } },
    { path: '/auth', name: 'auth', component: () => import('@/views/AuthView.vue'), meta: { public: true } },
    { path: '/confidentialite', name: 'privacy', component: () => import('@/views/PrivacyView.vue'), meta: { public: true } },
    {
      path: '/setup',
      component: () => import('@/views/shell/SetupShell.vue'),
      meta: { auth: true },
      children: [
        { path: '', redirect: '/setup/metier' },
        { path: 'metier', name: 'profession', component: () => import('@/views/ProfessionView.vue') },
        { path: 'ville', name: 'location', component: () => import('@/views/LocationView.vue') },
        { path: 'analyse', name: 'analysis', component: () => import('@/views/AnalysisView.vue') },
      ],
    },
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
      ],
    },
    { path: '/:pathMatch(.*)*', redirect: '/' },
  ],
})

router.beforeEach((to) => {
  const session = useSessionStore()
  session.restoreToken()
  const destination = resolveDestination(session.session)

  if (destination === 'landing') {
    return to.meta.public ? true : '/auth'
  }

  if (destination === 'home') {
    if (to.meta.public || to.path.startsWith('/setup')) return pathForDestination('home')
    return true
  }

  const setupPath = pathForDestination(destination)
  if (to.path !== setupPath && to.name !== destination) return setupPath
  return true
})

export default router
