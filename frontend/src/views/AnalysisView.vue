<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import LukaButton from '@/components/ui/LukaButton.vue'
import { useCatalogStore } from '@/stores/catalog'
import { useSessionStore } from '@/stores/session'

const session = useSessionStore()
const catalog = useCatalogStore()
const router = useRouter()
const launching = ref(false)
const step = ref(0)
const sectors = ['Banques et microfinance', 'Télécoms', 'Mines et industries', 'ONG et institutions']

async function launch() {
  launching.value = true
  step.value = 1
  const timer = window.setInterval(() => {
    step.value = Math.min(sectors.length, step.value + 1)
  }, 220)
  await catalog.bootstrap()
  await new Promise((resolve) => window.setTimeout(resolve, 900))
  window.clearInterval(timer)
  step.value = sectors.length
  session.markAnalysisLaunched()
  await router.replace('/app/accueil')
}
</script>

<template>
  <section class="panel">
    <h1>Activer le radar</h1>
    <p>Une fois. Ensuite Luka charge le catalogue et les offres ouvertes sur le serveur.</p>
    <ul>
      <li v-for="(item, i) in sectors" :key="item" :class="{ on: step > i }">
        {{ item }}
      </li>
    </ul>
    <p class="status">
      {{ launching ? 'Scan des banques, telcos, ONG et mines…' : 'Prêt à lancer le radar.' }}
    </p>
    <LukaButton :loading="launching" :disabled="launching" @click="launch">
      {{ launching ? 'Analyse en cours…' : 'Lancer le radar' }}
    </LukaButton>
  </section>
</template>

<style scoped>
.panel {
  background: #fff;
  border: 1px solid var(--line);
  border-radius: 20px;
  padding: 36px 28px;
  box-shadow: var(--shadow);
  display: grid;
  gap: 14px;
}
h1 {
  margin: 0;
  letter-spacing: -0.03em;
}
p {
  color: var(--luka-muted);
  max-width: 48ch;
  margin: 0;
}
ul {
  list-style: none;
  padding: 0;
  margin: 8px 0;
  display: grid;
  gap: 8px;
}
li {
  border: 1px solid var(--line);
  border-radius: 12px;
  padding: 12px 14px;
  color: var(--luka-muted);
  background: var(--canvas);
}
li.on {
  border-color: var(--luka-red);
  color: var(--luka-ink);
  background: #fff5f5;
  font-weight: 650;
}
</style>
