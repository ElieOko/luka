<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import LockedCard from '@/components/ui/LockedCard.vue'
import PageHeader from '@/components/ui/PageHeader.vue'
import { ADVICE, PUBLIC_NEWS } from '@/data/learner'
import { advicePreviewLimit, learnerUnlocked, offerPreviewLimit } from '@/data/plans'
import { firstName } from '@/domain/models'
import { useCatalogStore } from '@/stores/catalog'
import { useSessionStore } from '@/stores/session'

const session = useSessionStore()
const catalog = useCatalogStore()
const router = useRouter()

const learner = computed(() => session.isLearner)
const preview = computed(() => {
  const limit = session.profile ? Math.min(6, offerPreviewLimit(session.profile)) : 6
  return catalog.filteredOffers.slice(0, Number.isFinite(limit) ? limit : 6)
})
const advice = computed(() => {
  const limit = session.profile ? advicePreviewLimit(session.profile) : 1
  return ADVICE.slice(0, Number.isFinite(limit) ? limit : ADVICE.length)
})
const kpis = computed(() => [
  { label: 'Offres ouvertes', value: String(catalog.filteredOffers.length) },
  { label: 'Métier suivi', value: session.profile?.tradeTitle || session.profile?.profession?.title || '—' },
  { label: 'Ville', value: session.profile?.cityName || 'RDC' },
])
</script>

<template>
  <div>
    <PageHeader
      :title="`Bonjour ${firstName(session.profile)}`"
      :subtitle="learner ? 'News, tendances et orientation — un dossier à la fois.' : 'Les ouvertures correspondant à ton métier et ta ville.'"
    >
      <template #actions>
        <button v-if="!learner" class="ghost" type="button" @click="router.push('/app/offres')">Toutes les offres</button>
      </template>
    </PageHeader>

    <section class="kpis">
      <article v-for="item in kpis" :key="item.label">
        <span>{{ item.label }}</span>
        <strong>{{ item.value }}</strong>
      </article>
    </section>

    <div class="layout">
      <section>
        <template v-if="learner">
          <h2>Conseil du moment</h2>
          <article v-for="item in advice" :key="item.id" class="paper">
            <strong>{{ item.title }}</strong>
            <p>{{ item.body }}</p>
          </article>
          <LockedCard
            v-if="!session.profile || !learnerUnlocked(session.profile)"
            title="Conseils réguliers"
            body="Chaque semaine : un geste concret pour ton orientation."
            @unlock="router.push('/app/abonnement')"
          />
        </template>
        <template v-else>
          <div class="row">
            <h2>Offres pour toi</h2>
            <button type="button" @click="router.push('/app/offres')">Voir tout</button>
          </div>
          <div class="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Poste</th>
                  <th>Entreprise</th>
                  <th>Ville</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="offer in preview" :key="offer.id">
                  <td><strong>{{ offer.title }}</strong></td>
                  <td>{{ offer.company }}</td>
                  <td>{{ offer.city }}</td>
                  <td class="end"><a :href="offer.applyUrl" target="_blank" rel="noreferrer">Ouvrir</a></td>
                </tr>
              </tbody>
            </table>
          </div>
          <p v-if="!preview.length" class="empty">Aucune offre pour ces filtres.</p>
        </template>
      </section>
      <aside>
        <h2>{{ learner ? 'News MIT' : 'Marché' }}</h2>
        <article v-if="learner" class="paper click" @click="router.push('/app/news')">
          <span class="red">{{ PUBLIC_NEWS[0]?.source }}</span>
          <strong>{{ PUBLIC_NEWS[0]?.title }}</strong>
          <p>{{ PUBLIC_NEWS[0]?.excerpt }}</p>
        </article>
        <article v-else class="paper">
          <p v-if="!catalog.stats.length">Les tendances se construisent à partir des offres.</p>
          <div v-for="stat in catalog.stats.slice(0, 4)" :key="stat.profession.id" class="stat">
            <span>{{ stat.profession.title }}</span>
            <b>{{ stat.sharePercent }}%</b>
          </div>
          <button type="button" @click="router.push('/app/tendances')">Ouvrir le marché</button>
        </article>
      </aside>
    </div>
  </div>
</template>

<style scoped>
.kpis {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  margin-bottom: 28px;
}
.kpis article {
  background: #fff;
  border: 1px solid var(--line);
  border-radius: var(--radius);
  padding: 16px 18px;
  display: grid;
  gap: 6px;
}
.kpis span {
  color: var(--luka-muted);
  font-size: 13px;
}
.kpis strong {
  font-size: 20px;
}
.layout {
  display: grid;
  grid-template-columns: 1.4fr 0.8fr;
  gap: 22px;
}
h2 {
  margin: 0 0 12px;
  font-size: 16px;
}
.row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.row button,
.paper button,
.ghost {
  border: 0;
  background: none;
  color: var(--luka-red);
  font-weight: 700;
}
.ghost {
  border: 1px solid var(--line);
  background: #fff;
  height: 40px;
  padding: 0 14px;
  border-radius: 999px;
}
.table-wrap {
  background: #fff;
  border: 1px solid var(--line);
  border-radius: var(--radius);
  overflow: auto;
  margin-bottom: 10px;
}
table {
  width: 100%;
  border-collapse: collapse;
  min-width: 480px;
}
th,
td {
  text-align: left;
  padding: 12px 14px;
  border-bottom: 1px solid var(--line);
}
th {
  font-size: 11px;
  letter-spacing: 0.06em;
  text-transform: uppercase;
  color: var(--luka-muted);
  background: #fbf8f8;
}
tbody tr:last-child td {
  border-bottom: 0;
}
.end {
  text-align: right;
}
.end a {
  color: var(--luka-red);
  font-weight: 700;
  text-decoration: none;
}
.paper {
  background: #fff;
  border: 1px solid var(--line);
  border-radius: var(--radius);
  padding: 16px;
  display: grid;
  gap: 6px;
  margin-bottom: 10px;
}
.paper p {
  margin: 0;
  color: var(--luka-muted);
}
.click {
  cursor: pointer;
}
.red {
  color: var(--luka-red);
  font-weight: 700;
  font-size: 12px;
}
.stat {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px solid var(--line);
}
.empty {
  color: var(--luka-muted);
}
@media (max-width: 900px) {
  .kpis,
  .layout {
    grid-template-columns: 1fr;
  }
}
</style>
