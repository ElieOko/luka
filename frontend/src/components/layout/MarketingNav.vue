<script setup lang="ts">
import { RouterLink } from 'vue-router'
import LukaLogo from '@/components/brand/LukaLogo.vue'

defineProps<{
  solid?: boolean
}>()
</script>

<template>
  <header class="nav" :class="{ solid }">
    <div class="inner">
      <RouterLink to="/" class="brand" aria-label="Luka accueil">
        <LukaLogo :height="30" />
      </RouterLink>
      <nav>
        <a href="/#offres">Offres</a>
        <a href="/#fonctionnement">Fonctionnement</a>
        <a href="/#tarifs">Tarifs</a>
      </nav>
      <div class="cta">
        <RouterLink to="/auth" class="ghost">Connexion</RouterLink>
        <RouterLink to="/auth?signup=1" class="primary">Créer un compte</RouterLink>
      </div>
    </div>
  </header>
</template>

<style scoped>
.nav {
  position: sticky;
  top: 0;
  z-index: 20;
  backdrop-filter: blur(16px);
  background: rgba(246, 243, 242, 0.92);
  border-bottom: 1px solid var(--line);
}
.nav.solid {
  background: rgba(255, 255, 255, 0.92);
  border-bottom-color: var(--line);
}
.inner {
  max-width: var(--content);
  margin: 0 auto;
  min-height: var(--nav);
  padding: 0 28px;
  display: flex;
  align-items: center;
  gap: 28px;
}
nav {
  display: flex;
  gap: 22px;
  flex: 1;
}
nav a,
.ghost {
  text-decoration: none;
  font-weight: 600;
  color: var(--luka-muted);
}
nav a:hover,
.ghost:hover {
  color: var(--luka-ink);
}
.cta {
  display: flex;
  gap: 10px;
  align-items: center;
}
.primary {
  text-decoration: none;
  background: var(--luka-red);
  color: #fff;
  font-weight: 700;
  border-radius: 999px;
  padding: 10px 16px;
}
@media (max-width: 760px) {
  nav {
    display: none;
  }
}
</style>
