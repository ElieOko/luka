<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import LukaButton from '@/components/ui/LukaButton.vue'
import RadarPulse from '@/components/ui/RadarPulse.vue'
import { useCatalogStore } from '@/stores/catalog'
import { useSessionStore } from '@/stores/session'

const session = useSessionStore()
const catalog = useCatalogStore()
const router = useRouter()
const launching = ref(false)

async function launch() {
  launching.value = true
  await catalog.bootstrap()
  await new Promise((resolve) => window.setTimeout(resolve, 1400))
  session.markAnalysisLaunched()
  await router.replace('/app/accueil')
}
</script>

<template>
  <main class="analysis">
    <h1>Analyses infinies</h1>
    <p>Une seule fois. Ensuite Luka scrute le marché congolais sans s’arrêter — et t’envoie les offres.</p>
    <RadarPulse />
    <p class="status">
      {{ launching ? 'Scan des banques, telcos, ONG et mines…' : 'Prêt à lancer le radar Luka.' }}
    </p>
    <LukaButton :loading="launching" :disabled="launching" @click="launch">
      {{ launching ? 'Analyse en cours…' : 'Lancer les analyses infinies' }}
    </LukaButton>
  </main>
</template>

<style scoped>
.analysis {
  min-height: 100dvh;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 48px 24px 32px;
  max-width: 560px;
  margin: 0 auto;
  background: var(--luka-cream);
}
h1 {
  font-size: 30px;
  font-weight: 800;
  margin-bottom: 8px;
}
p {
  color: var(--luka-muted);
}
.status {
  margin: 24px 0 20px;
}
.analysis :deep(.radar) {
  margin-top: auto;
}
</style>
