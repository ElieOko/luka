<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { RouterLink, useRoute, useRouter } from 'vue-router'
import LukaLogo from '@/components/brand/LukaLogo.vue'
import { useCatalogStore } from '@/stores/catalog'
import { useSessionStore } from '@/stores/session'

const session = useSessionStore()
const catalog = useCatalogStore()
const route = useRoute()
const router = useRouter()
const drawer = ref(false)

const learner = computed(() => session.isLearner)

const tabs = computed(() => {
  if (learner.value) {
    return [
      { to: '/app/accueil', label: 'Accueil', icon: '⌂' },
      { to: '/app/news', label: 'News', icon: '☰' },
      { to: '/app/tendances', label: 'Tendances', icon: '▲', center: true },
      { to: '/app/orientation', label: 'Orientation', icon: '✦' },
      { to: '/app/profil', label: 'Profil', icon: '●' },
    ]
  }
  return [
    { to: '/app/accueil', label: 'Accueil', icon: '⌂' },
    { to: '/app/offres', label: 'Offres', icon: '☐' },
    { to: '/app/tendances', label: 'Marché', icon: '▲', center: true },
    { to: '/app/analyses', label: 'Analyses', icon: '▣' },
    { to: '/app/profil', label: 'Profil', icon: '●' },
  ]
})

function isActive(path: string) {
  return route.path === path || (path !== '/app/accueil' && route.path.startsWith(path))
}

onMounted(() => {
  void catalog.bootstrap()
})
</script>

<template>
  <div class="shell">
    <aside class="sidebar">
      <LukaLogo :height="32" />
      <nav>
        <RouterLink
          v-for="tab in tabs"
          :key="tab.to"
          :to="tab.to"
          class="side-link"
          :class="{ on: isActive(tab.to), hot: tab.center }"
        >
          <span>{{ tab.icon }}</span>
          {{ tab.label }}
        </RouterLink>
      </nav>
      <button class="ghost" type="button" @click="router.push('/app/abonnement')">Abonnement</button>
    </aside>
    <div class="main">
      <header class="top">
        <button class="menu" type="button" @click="drawer = true">☰</button>
        <LukaLogo :height="28" />
        <button class="bell" type="button" @click="router.push('/app/abonnement')">★</button>
      </header>
      <div class="page">
        <RouterView v-slot="{ Component }">
          <Transition name="page" mode="out-in">
            <component :is="Component" />
          </Transition>
        </RouterView>
      </div>
      <nav class="bar">
        <RouterLink
          v-for="tab in tabs"
          :key="tab.to"
          :to="tab.to"
          class="item"
          :class="{ on: isActive(tab.to), center: tab.center }"
        >
          <span class="ico">{{ tab.icon }}</span>
          <small>{{ tab.label }}</small>
        </RouterLink>
      </nav>
    </div>
    <Transition name="fade">
      <div v-if="drawer" class="overlay" @click="drawer = false">
        <aside class="drawer" @click.stop>
          <LukaLogo :height="32" />
          <p>{{ session.profile?.displayName || 'Ton profil' }}</p>
          <RouterLink to="/app/abonnement" @click="drawer = false">Abonnement</RouterLink>
          <RouterLink to="/app/confidentialite" @click="drawer = false">Confidentialité</RouterLink>
          <RouterLink to="/app/profil" @click="drawer = false">Profil</RouterLink>
          <button type="button" @click="session.logout(); drawer = false; router.push('/')">Se déconnecter</button>
        </aside>
      </div>
    </Transition>
  </div>
</template>

<style scoped>
.shell {
  min-height: 100dvh;
  display: grid;
  background: var(--luka-cream);
}
.sidebar {
  display: none;
}
.main {
  min-width: 0;
  display: flex;
  flex-direction: column;
  min-height: 100dvh;
}
.top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  position: sticky;
  top: 0;
  z-index: 5;
  background: rgba(255, 246, 245, 0.86);
  backdrop-filter: blur(12px);
}
.menu,
.bell,
.ghost {
  border: 0;
  background: #fff;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  font-size: 16px;
}
.page {
  flex: 1;
  padding-bottom: 88px;
}
.bar {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  height: 64px;
  background: var(--bar);
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  align-items: end;
  padding-bottom: 8px;
  z-index: 8;
}
.item {
  color: var(--bar-muted);
  text-decoration: none;
  display: grid;
  place-items: center;
  font-size: 10px;
  font-weight: 600;
}
.item.on {
  color: #fff;
}
.item.center .ico {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: var(--luka-red);
  color: #fff;
  display: grid;
  place-items: center;
  transform: translateY(-8px);
  box-shadow: 0 8px 18px rgba(227, 27, 35, 0.4);
}
.ico {
  font-size: 18px;
  line-height: 1;
}
.overlay {
  position: fixed;
  inset: 0;
  background: rgba(10, 4, 6, 0.45);
  z-index: 20;
}
.drawer {
  width: min(320px, 86vw);
  height: 100%;
  background: #fff;
  padding: 28px 20px;
  display: grid;
  align-content: start;
  gap: 14px;
}
.drawer a,
.drawer button {
  text-align: left;
  background: none;
  border: 0;
  font-weight: 700;
  color: var(--luka-ink);
  text-decoration: none;
}
@media (min-width: 980px) {
  .shell {
    grid-template-columns: 240px 1fr;
  }
  .sidebar {
    display: flex;
    flex-direction: column;
    gap: 24px;
    padding: 28px 20px;
    background: #fff;
    border-right: 1px solid var(--luka-mist);
    position: sticky;
    top: 0;
    height: 100dvh;
  }
  .side-link {
    display: flex;
    gap: 10px;
    align-items: center;
    text-decoration: none;
    padding: 10px 12px;
    border-radius: 14px;
    font-weight: 600;
    color: var(--luka-muted);
  }
  .side-link.on,
  .side-link.hot.on {
    background: var(--luka-mist);
    color: var(--luka-red);
  }
  .side-link.hot {
    color: var(--luka-red);
  }
  .ghost {
    width: auto;
    border-radius: 14px;
    margin-top: auto;
    background: var(--luka-red);
    color: #fff;
    font-weight: 700;
  }
  .top,
  .bar {
    display: none;
  }
  .page {
    padding-bottom: 0;
  }
}
</style>
