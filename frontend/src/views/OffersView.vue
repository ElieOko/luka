<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import OfferCard from '@/components/ui/OfferCard.vue'
import PageBackdrop from '@/components/ui/PageBackdrop.vue'
import { offerPreviewLimit, proUnlocked } from '@/data/plans'
import { useCatalogStore } from '@/stores/catalog'
import { useSessionStore } from '@/stores/session'
import LockedCard from '@/components/ui/LockedCard.vue'

const catalog = useCatalogStore()
const session = useSessionStore()
const router = useRouter()

const visible = computed(() => {
  const limit = session.profile ? offerPreviewLimit(session.profile) : 5
  return catalog.filteredOffers.slice(0, Number.isFinite(limit) ? limit : catalog.filteredOffers.length)
})
</script>

<template>
  <PageBackdrop image="/images/onboarding_kinshasa_2.jpg" cinematic>
    <div class="wrap">
      <h1>Offres</h1>
      <p>Les ouvertures actuellement sur le serveur Casanayo.</p>
      <OfferCard v-for="(offer, i) in visible" :key="offer.id" :offer="offer" :index="i" />
      <p v-if="!visible.length" class="empty">Aucune offre pour l’instant. Tire l’accueil pour rafraîchir.</p>
      <LockedCard
        v-if="session.profile && !proUnlocked(session.profile)"
        title="Toutes les offres"
        body="L’abonnement Professionnel ouvre le flux complet, pas seulement l’aperçu."
        @unlock="router.push('/app/abonnement')"
      />
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
p,
.empty {
  color: rgba(255, 255, 255, 0.85);
  margin: 0 0 8px;
}
</style>
