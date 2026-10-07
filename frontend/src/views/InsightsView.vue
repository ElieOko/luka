<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import LockedCard from '@/components/ui/LockedCard.vue'
import PageBackdrop from '@/components/ui/PageBackdrop.vue'
import { proUnlocked } from '@/data/plans'
import { useCatalogStore } from '@/stores/catalog'
import { useSessionStore } from '@/stores/session'

const session = useSessionStore()
const catalog = useCatalogStore()
const router = useRouter()
const profile = computed(() => session.profile)
const cvOk = computed(() => Boolean(profile.value && proUnlocked(profile.value)))
const offerOk = computed(() => cvOk.value)
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
  <PageBackdrop image="/images/onboarding_kinshasa_2.jpg" cinematic>
    <div class="wrap">
      <h1>Analyses</h1>
      <p>CV, offres et signaux du marché — pour viser juste.</p>
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
        <p>
          {{
            profile?.cvFileName
              ? `Fichier chargé : ${profile.cvFileName}`
              : 'Ajoute un CV dans le profil pour un matching plus précis.'
          }}
        </p>
      </article>
      <LockedCard
        v-if="!offerOk"
        title="Analyse des offres"
        body="Lis le marché de ton métier : contrats, villes, volume sur les offres ouvertes."
        @unlock="router.push('/app/abonnement')"
      />
      <article v-else class="card">
        <h2>Analyse des offres</h2>
        <p>
          {{ catalog.filteredOffers.length }} offres ouvertes
          {{ profile?.tradeTitle ? `autour de ${profile.tradeTitle}` : '' }}.
        </p>
        <p>{{ catalog.stats[0]?.profession.title }} mène le volume cette semaine.</p>
      </article>
    </div>
  </PageBackdrop>
</template>

<style scoped>
.wrap {
  padding: 20px;
  max-width: 880px;
  margin: 0 auto;
  display: grid;
  gap: 12px;
}
h1 {
  color: #fff;
  margin: 0;
  font-weight: 900;
}
p {
  color: rgba(255, 255, 255, 0.85);
  margin: 0;
}
.card {
  background: #fff;
  border-radius: 22px;
  padding: 18px;
  color: var(--luka-ink);
}
.card p {
  color: var(--luka-muted);
  margin-top: 8px;
}
.bar {
  height: 10px;
  border-radius: 99px;
  background: var(--luka-mist);
  overflow: hidden;
  margin-top: 10px;
}
.bar i {
  display: block;
  height: 100%;
  background: var(--luka-red);
}
</style>
