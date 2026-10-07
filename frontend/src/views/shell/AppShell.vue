<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { RouterLink, useRoute, useRouter } from 'vue-router'
import LukaLogo from '@/components/brand/LukaLogo.vue'
import { firstName } from '@/domain/models'
import { useCatalogStore } from '@/stores/catalog'
import { useSessionStore } from '@/stores/session'

const session = useSessionStore()
const catalog = useCatalogStore()
const route = useRoute()
const router = useRouter()
const menu = ref(false)

const learner = computed(() => session.isLearner)
const kindLabel = computed(() => (learner.value ? 'Apprenant' : 'Professionnel'))
const links = computed(() => {
  if (learner.value) {
    return [
      { to: '/app/accueil', label: 'Tableau de bord' },
      { to: '/app/news', label: 'News' },
      { to: '/app/tendances', label: 'Tendances' },
      { to: '/app/orientation', label: 'Orientation' },
    ]
  }
  return [
    { to: '/app/accueil', label: 'Tableau de bord' },
    { to: '/app/offres', label: 'Offres' },
    { to: '/app/tendances', label: 'Marché' },
    { to: '/app/analyses', label: 'Analyses' },
  ]
})

function active(path: string) {
  return route.path === path || (path !== '/app/accueil' && route.path.startsWith(path))
}

function logout() {
  session.logout()
  void router.push('/')
}

function onSearch(value: string) {
  catalog.onQuery(value)
  if (!learner.value && !route.path.startsWith('/app/offres')) {
    void router.push('/app/offres')
  }
}

watch(
  () => route.fullPath,
  () => {
    menu.value = false
  },
)

onMounted(() => {
  void catalog.bootstrap()
})
</script>

<template>
  <div class="app">
    <div v-if="menu" class="scrim" @click="menu = false" />
    <aside :class="{ open: menu }">
      <RouterLink to="/app/accueil" class="brand"><LukaLogo :height="28" /></RouterLink>
      <p class="group">Espace</p>
      <nav>
        <RouterLink
          v-for="link in links"
          :key="link.to"
          :to="link.to"
          :class="{ on: active(link.to) }"
        >
          {{ link.label }}
        </RouterLink>
      </nav>
      <p class="group">Compte</p>
      <nav>
        <RouterLink to="/app/abonnement" :class="{ on: active('/app/abonnement') }">Abonnement</RouterLink>
        <RouterLink to="/app/profil" :class="{ on: active('/app/profil') }">Compte</RouterLink>
      </nav>
      <div class="bottom">
        <RouterLink to="/app/profil" class="user">
          <span>{{ firstName(session.profile).slice(0, 1) }}</span>
          <div>
            <strong>{{ firstName(session.profile) }}</strong>
            <small>{{ kindLabel }}</small>
          </div>
        </RouterLink>
        <button type="button" class="out" @click="logout">Se déconnecter</button>
      </div>
    </aside>
    <div class="main">
      <header class="top">
        <button class="burger" type="button" @click="menu = !menu">Menu</button>
        <input
          class="search"
          :value="catalog.query"
          :placeholder="learner ? 'Rechercher dans Luka…' : 'Rechercher un poste, une entreprise, une ville…'"
          @input="onSearch(($event.target as HTMLInputElement).value)"
        />
        <RouterLink to="/app/profil" class="chip">
          <span>{{ firstName(session.profile).slice(0, 1) }}</span>
          {{ firstName(session.profile) }}
        </RouterLink>
      </header>
      <div class="page">
        <RouterView v-slot="{ Component }">
          <Transition name="page" mode="out-in">
            <component :is="Component" />
          </Transition>
        </RouterView>
      </div>
    </div>
  </div>
</template>

<style scoped>
.app {
  min-height: 100dvh;
  display: grid;
  grid-template-columns: var(--sidebar) 1fr;
  background: var(--canvas);
}
aside {
  background: #fff;
  border-right: 1px solid var(--line);
  padding: 22px 14px;
  display: flex;
  flex-direction: column;
  position: sticky;
  top: 0;
  height: 100dvh;
  overflow: auto;
}
.brand {
  padding: 2px 10px 18px;
}
.group {
  margin: 12px 10px 6px;
  font-size: 11px;
  font-weight: 800;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: #b09a9d;
}
nav {
  display: grid;
  gap: 2px;
}
nav a {
  text-decoration: none;
  padding: 9px 12px;
  border-radius: 10px;
  font-weight: 600;
  color: var(--luka-muted);
}
nav a.on,
nav a:hover {
  background: var(--luka-mist);
  color: var(--luka-ink);
}
nav a.on {
  color: var(--luka-red);
}
.bottom {
  margin-top: auto;
  padding-top: 16px;
  border-top: 1px solid var(--line);
  display: grid;
  gap: 8px;
}
.user {
  display: flex;
  gap: 10px;
  align-items: center;
  text-decoration: none;
  padding: 8px;
  border-radius: 12px;
}
.user:hover {
  background: var(--luka-mist);
}
.user span,
.chip span {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: var(--luka-red);
  color: #fff;
  display: grid;
  place-items: center;
  font-weight: 800;
  flex-shrink: 0;
}
.user strong,
.user small {
  display: block;
}
.user small {
  color: var(--luka-muted);
  font-weight: 500;
  font-size: 12px;
}
.out {
  border: 0;
  background: none;
  color: var(--luka-muted);
  font-weight: 650;
  text-align: left;
  padding: 6px 8px;
}
.out:hover {
  color: var(--luka-red);
}
.main {
  min-width: 0;
  display: flex;
  flex-direction: column;
}
.top {
  display: flex;
  gap: 12px;
  align-items: center;
  padding: 14px 28px;
  background: rgba(255, 255, 255, 0.92);
  border-bottom: 1px solid var(--line);
  position: sticky;
  top: 0;
  z-index: 5;
  backdrop-filter: blur(12px);
}
.search {
  flex: 1;
  height: 42px;
  border-radius: 999px;
  border: 1px solid var(--line);
  padding: 0 16px;
  background: var(--canvas);
}
.chip {
  display: flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
  font-weight: 700;
}
.burger,
.scrim {
  display: none;
}
.page {
  padding: 32px 28px 56px;
  max-width: 1120px;
  width: 100%;
}
@media (max-width: 900px) {
  .app {
    grid-template-columns: 1fr;
  }
  aside {
    position: fixed;
    left: 0;
    top: 0;
    z-index: 30;
    width: min(280px, 86vw);
    transform: translateX(-105%);
    transition: transform 0.24s var(--ease);
    box-shadow: var(--shadow);
  }
  aside.open {
    transform: none;
  }
  .scrim {
    display: block;
    position: fixed;
    inset: 0;
    background: rgba(20, 6, 8, 0.4);
    z-index: 25;
  }
  .burger {
    display: inline-flex;
    align-items: center;
    border: 1px solid var(--line);
    background: #fff;
    border-radius: 10px;
    height: 42px;
    padding: 0 12px;
    font-weight: 700;
  }
  .chip span + * {
    display: none;
  }
  .page {
    padding: 24px 16px 40px;
  }
}
</style>
