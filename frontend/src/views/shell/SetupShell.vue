<script setup lang="ts">
import { computed } from 'vue'
import { RouterLink, useRoute } from 'vue-router'
import LukaLogo from '@/components/brand/LukaLogo.vue'

const route = useRoute()
const steps = [
  { to: '/setup/metier', label: 'Métier', name: 'profession' },
  { to: '/setup/ville', label: 'Ville', name: 'location' },
  { to: '/setup/analyse', label: 'Radar', name: 'analysis' },
]
const current = computed(() => steps.findIndex((step) => step.name === route.name))
</script>

<template>
  <div class="setup">
    <header>
      <LukaLogo :height="28" />
      <ol>
        <li v-for="(step, i) in steps" :key="step.to" :class="{ on: i === current, done: i < current }">
          <RouterLink :to="step.to">{{ i + 1 }}. {{ step.label }}</RouterLink>
        </li>
      </ol>
    </header>
    <main>
      <RouterView />
    </main>
  </div>
</template>

<style scoped>
.setup {
  min-height: 100dvh;
  background: var(--canvas);
}
header {
  max-width: 860px;
  margin: 0 auto;
  padding: 28px 24px 0;
  display: flex;
  justify-content: space-between;
  gap: 20px;
  align-items: center;
}
ol {
  display: flex;
  gap: 16px;
  list-style: none;
  padding: 0;
  margin: 0;
}
a {
  text-decoration: none;
  color: var(--luka-muted);
  font-weight: 600;
  font-size: 14px;
}
.on a,
.done a {
  color: var(--luka-red);
}
main {
  max-width: 860px;
  margin: 0 auto;
  padding: 28px 24px 48px;
}
@media (max-width: 640px) {
  header {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
