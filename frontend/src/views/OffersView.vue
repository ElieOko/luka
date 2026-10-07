<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import FilterPill from '@/components/ui/FilterPill.vue'
import LockedCard from '@/components/ui/LockedCard.vue'
import PageHeader from '@/components/ui/PageHeader.vue'
import { offerPreviewLimit, proUnlocked } from '@/data/plans'
import { useCatalogStore } from '@/stores/catalog'
import { useSessionStore } from '@/stores/session'

const catalog = useCatalogStore()
const session = useSessionStore()
const router = useRouter()

const visible = computed(() => {
  const limit = session.profile ? offerPreviewLimit(session.profile) : 5
  return catalog.filteredOffers.slice(0, Number.isFinite(limit) ? limit : catalog.filteredOffers.length)
})
</script>

<template>
  <div>
    <PageHeader title="Offres" subtitle="Ouvertures actuellement publiées sur le serveur Casanayo.">
      <template #actions>
        <button class="ghost" type="button" @click="catalog.resetFilters()">Réinitialiser</button>
      </template>
    </PageHeader>
    <div class="filters">
      <FilterPill
        v-for="city in catalog.cities.slice(0, 12)"
        :key="city.id"
        :label="city.name"
        :selected="catalog.filters.city === city.name"
        @click="catalog.onCity(catalog.filters.city === city.name ? null : city.name)"
      />
    </div>
    <div class="table-wrap">
      <table>
        <thead>
          <tr>
            <th>Poste</th>
            <th>Entreprise</th>
            <th>Ville</th>
            <th>Contrat</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="offer in visible" :key="offer.id">
            <td>
              <strong>{{ offer.title }}</strong>
              <small v-if="offer.summary">{{ offer.summary }}</small>
            </td>
            <td>{{ offer.company }}</td>
            <td>{{ offer.city }}</td>
            <td><span class="badge">{{ offer.contract }}</span></td>
            <td class="end">
              <a :href="offer.applyUrl" target="_blank" rel="noreferrer">Ouvrir</a>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <p v-if="!visible.length" class="empty">Aucune offre pour ces filtres.</p>
    <LockedCard
      v-if="session.profile && !proUnlocked(session.profile)"
      title="Flux complet"
      body="L’abonnement Professionnel ouvre toutes les offres, pas seulement l’aperçu."
      @unlock="router.push('/app/abonnement')"
    />
  </div>
</template>

<style scoped>
.filters {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 18px;
}
.table-wrap {
  background: #fff;
  border: 1px solid var(--line);
  border-radius: var(--radius);
  overflow: auto;
  margin-bottom: 18px;
  box-shadow: var(--shadow);
}
table {
  width: 100%;
  border-collapse: collapse;
  min-width: 680px;
}
th,
td {
  text-align: left;
  padding: 14px 16px;
  border-bottom: 1px solid var(--line);
  vertical-align: top;
}
th {
  font-size: 12px;
  letter-spacing: 0.06em;
  text-transform: uppercase;
  color: var(--luka-muted);
  background: #fbf8f8;
}
tbody tr:last-child td {
  border-bottom: 0;
}
tbody tr:hover td {
  background: #fffafa;
}
td strong {
  display: block;
}
td small {
  display: block;
  margin-top: 4px;
  color: var(--luka-muted);
  font-size: 13px;
  max-width: 42ch;
}
.end {
  text-align: right;
}
a {
  color: var(--luka-red);
  font-weight: 700;
  text-decoration: none;
}
.ghost {
  border: 1px solid var(--line);
  background: #fff;
  height: 40px;
  border-radius: 999px;
  padding: 0 14px;
  font-weight: 700;
}
.empty {
  color: var(--luka-muted);
}
</style>
