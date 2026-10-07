<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import LockedCard from '@/components/ui/LockedCard.vue'
import PageHeader from '@/components/ui/PageHeader.vue'
import { proUnlocked } from '@/data/plans'
import { useCatalogStore } from '@/stores/catalog'
import { useSessionStore } from '@/stores/session'

const session = useSessionStore()
const catalog = useCatalogStore()
const router = useRouter()
const profile = computed(() => session.profile)
const cvOk = computed(() => Boolean(profile.value && proUnlocked(profile.value)))
const completeness = computed(() => {
  const p = profile.value
  const checks = [
    Boolean(p?.displayName),
    Boolean(p?.cityName),
    Boolean(p?.profession || p?.tradeTitle),
    Boolean(p?.email.includes('@')),
    Boolean(p?.cvFileName),
  ]
  return checks.filter(Boolean).length / checks.length
})
</script>

<template>
  <div>
    <PageHeader title="Analyses" subtitle="CV, offres et signaux — pour viser juste." />
    <div class="grid">
      <LockedCard
        v-if="!cvOk"
        title="Analyse de CV"
        body="Score de clarté, mots-clés manquants et pistes pour matcher les offres RDC."
        @unlock="router.push('/app/abonnement')"
      />
      <article v-else class="card">
        <h2>Analyse de CV</h2>
        <p>Complétude estimée à {{ Math.round(completeness * 100) }} %.</p>
        <div class="bar"><i :style="{ width: `${completeness * 100}%` }" /></div>
        <p>{{ profile?.cvFileName ? `Fichier : ${profile.cvFileName}` : 'Ajoute un CV dans le compte.' }}</p>
      </article>
      <LockedCard
        v-if="!cvOk"
        title="Analyse des offres"
        body="Contrats, villes, volume sur les ouvertures de ton métier."
        @unlock="router.push('/app/abonnement')"
      />
      <article v-else class="card">
        <h2>Analyse des offres</h2>
        <p>{{ catalog.filteredOffers.length }} offres ouvertes {{ profile?.tradeTitle ? `autour de ${profile.tradeTitle}` : '' }}.</p>
        <p>{{ catalog.stats[0]?.profession.title }} mène le volume cette semaine.</p>
      </article>
    </div>
  </div>
</template>

<style scoped>
.grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 14px;
}
@media (max-width: 800px) {
  .grid {
    grid-template-columns: 1fr;
  }
}
.card {
  background: #fff;
  border: 1px solid var(--line);
  border-radius: var(--radius);
  padding: 20px;
}
p {
  color: var(--luka-muted);
}
.bar {
  height: 8px;
  background: var(--luka-mist);
  border-radius: 99px;
  overflow: hidden;
}
.bar i {
  display: block;
  height: 100%;
  background: var(--luka-red);
}
</style>
