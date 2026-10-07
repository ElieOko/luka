<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import FilterPill from '@/components/ui/FilterPill.vue'
import LockedCard from '@/components/ui/LockedCard.vue'
import OfferCard from '@/components/ui/OfferCard.vue'
import PageBackdrop from '@/components/ui/PageBackdrop.vue'
import { ADVICE, PUBLIC_NEWS } from '@/data/learner'
import { advicePreviewLimit, learnerUnlocked, offerPreviewLimit } from '@/data/plans'
import { firstName } from '@/domain/models'
import { useCatalogStore } from '@/stores/catalog'
import { useSessionStore } from '@/stores/session'

const session = useSessionStore()
const catalog = useCatalogStore()
const router = useRouter()
const showFilters = ref(false)
const adIndex = ref(0)
let adTimer: number | undefined

const learner = computed(() => session.isLearner)
const isFree = computed(() => {
  const plan = session.profile?.planId
  return session.profile?.isPremium !== true && (!plan || plan === 'starter')
})
const preview = computed(() => {
  const limit = session.profile ? Math.min(5, offerPreviewLimit(session.profile)) : 5
  return catalog.filteredOffers.slice(0, Number.isFinite(limit) ? limit : 5)
})
const advice = computed(() => {
  const limit = session.profile ? advicePreviewLimit(session.profile) : 1
  return ADVICE.slice(0, Number.isFinite(limit) ? limit : ADVICE.length)
})
const mit = PUBLIC_NEWS[0]!

onMounted(() => {
  adTimer = window.setInterval(() => {
    if (!catalog.ads.length) return
    adIndex.value = (adIndex.value + 1) % catalog.ads.length
  }, 3800)
})
onBeforeUnmount(() => {
  if (adTimer) window.clearInterval(adTimer)
})
</script>

<template>
  <PageBackdrop image="/images/onboarding_kinshasa_1.jpg" cinematic>
    <div class="home">
      <header>
        <span v-if="isFree" class="badge">Gratuit</span>
        <h1>Bonjour {{ firstName(session.profile) }}</h1>
        <p>
          {{
            learner
              ? 'Tendances, news MIT, orientation — un conseil à la fois.'
              : 'L’emploi vient à toi. 5 pistes, puis tout voir.'
          }}
        </p>
        <button v-if="!learner" class="search" type="button" @click="showFilters = true">
          Recherche et filtres
          <i v-if="catalog.filterActive" />
        </button>
      </header>

      <div class="tiles">
        <button v-if="learner" type="button" @click="router.push('/app/news')">
          <strong>News MIT</strong><span>Universités</span>
        </button>
        <button v-else type="button" @click="router.push('/app/offres')">
          <strong>Offres</strong><span>Ouvertes</span>
        </button>
        <button type="button" @click="router.push('/app/tendances')">
          <strong>{{ learner ? 'Tendances' : 'Marché' }}</strong>
          <span>{{ learner ? 'Marché' : 'Temps réel' }}</span>
        </button>
        <button type="button" @click="router.push(learner ? '/app/orientation' : '/app/analyses')">
          <strong>{{ learner ? 'Orientation' : 'Analyses' }}</strong>
          <span>{{ learner ? 'Filières' : 'CV & offres' }}</span>
        </button>
      </div>

      <template v-if="learner">
        <h2>Conseil du moment</h2>
        <article v-for="item in advice" :key="item.id" class="paper">
          <strong>{{ item.title }}</strong>
          <p>{{ item.body }}</p>
        </article>
        <LockedCard
          v-if="!session.profile || !learnerUnlocked(session.profile)"
          title="Conseils réguliers"
          body="Chaque semaine : un geste concret pour ton orientation et tes études."
          @unlock="router.push('/app/abonnement')"
        />
        <div class="row-title">
          <h2>News MIT</h2>
          <button type="button" @click="router.push('/app/news')">Voir</button>
        </div>
        <article class="paper">
          <span class="red">{{ mit.source }}</span>
          <strong>{{ mit.title }}</strong>
          <p>{{ mit.excerpt }}</p>
        </article>
      </template>

      <template v-else>
        <div class="row-title">
          <h2>Offres pour toi</h2>
          <button type="button" @click="router.push('/app/offres')">Voir tout</button>
        </div>
        <OfferCard v-for="(offer, i) in preview" :key="offer.id" :offer="offer" :index="i" />
        <p v-if="!preview.length" class="empty">Aucune offre pour ces filtres. Change de ville ou de métier.</p>
        <h2>Pubs partenaires</h2>
        <div class="ads">
          <Transition name="fade" mode="out-in">
            <article :key="catalog.ads[adIndex]?.id" class="ad">
              <img :src="`/images/${catalog.ads[adIndex]?.imageName}.jpg`" alt="" />
              <div>
                <small>PUBLICITÉ</small>
                <strong>{{ catalog.ads[adIndex]?.title }}</strong>
                <p>{{ catalog.ads[adIndex]?.subtitle }}</p>
              </div>
            </article>
          </Transition>
        </div>
      </template>
    </div>
  </PageBackdrop>

  <Teleport to="body">
    <div v-if="showFilters" class="sheet" @click.self="showFilters = false">
      <div class="panel">
        <h3>Recherche</h3>
        <p>Ville et métier — le sheet reste court.</p>
        <input :value="catalog.query" placeholder="Poste, entreprise, ville…" @input="catalog.onQuery(($event.target as HTMLInputElement).value)" />
        <div class="split">
          <strong>Ville</strong>
          <button type="button" @click="catalog.onCity(null)">Toutes</button>
        </div>
        <div class="pills">
          <FilterPill
            v-for="city in catalog.cities"
            :key="city.id"
            :label="city.name"
            :selected="catalog.filters.city === city.name"
            @click="catalog.onCity(city.name)"
          />
        </div>
        <div class="split">
          <strong>Métier</strong>
          <button type="button" @click="catalog.onProfession(null)">Tous</button>
        </div>
        <div class="pills">
          <FilterPill
            v-for="chip in catalog.trades"
            :key="chip.title"
            :label="chip.title"
            :selected="catalog.filters.tradeKey === `${chip.domainId ?? 'local'}::${chip.title}`"
            @click="catalog.onProfession(chip)"
          />
        </div>
        <button class="reset" type="button" @click="catalog.resetFilters()">Réinitialiser les filtres</button>
      </div>
    </div>
  </Teleport>
</template>

<style scoped>
.home {
  padding: 16px 20px 40px;
  max-width: 880px;
  margin: 0 auto;
  display: grid;
  gap: 12px;
}
h1 {
  color: #fff;
  margin: 8px 0 4px;
  font-weight: 900;
}
header p,
.empty {
  color: rgba(255, 255, 255, 0.85);
  margin: 0;
}
.badge {
  display: inline-block;
  background: var(--luka-mist);
  color: var(--luka-red);
  border-radius: 99px;
  padding: 4px 10px;
  font-weight: 700;
  font-size: 13px;
}
.search {
  margin-top: 10px;
  border: 0;
  background: rgba(255, 255, 255, 0.16);
  color: #fff;
  border-radius: 99px;
  padding: 8px 14px;
  position: relative;
  width: fit-content;
}
.search i {
  position: absolute;
  top: 6px;
  right: 8px;
  width: 8px;
  height: 8px;
  background: var(--luka-red);
  border-radius: 50%;
}
.tiles {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
}
.tiles button {
  background: var(--luka-mist);
  border: 0;
  border-radius: 18px;
  padding: 12px;
  display: grid;
  gap: 4px;
}
.tiles span {
  color: var(--luka-muted);
  font-size: 12px;
}
h2 {
  color: #fff;
  margin: 10px 0 0;
  font-size: 18px;
}
.row-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.row-title button,
.reset,
.split button {
  border: 0;
  background: none;
  color: var(--luka-red);
  font-weight: 700;
}
.paper {
  background: #fff;
  border-radius: 18px;
  padding: 16px;
  display: grid;
  gap: 6px;
}
.paper p {
  margin: 0;
  color: var(--luka-muted);
}
.red {
  color: var(--luka-red);
  font-weight: 700;
}
.ad {
  position: relative;
  height: 200px;
  border-radius: 24px;
  overflow: hidden;
}
.ad img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.ad div {
  position: absolute;
  inset: auto 0 0;
  padding: 16px;
  background: linear-gradient(transparent, #000);
  color: #fff;
}
.sheet {
  position: fixed;
  inset: 0;
  background: rgba(10, 4, 6, 0.4);
  display: flex;
  align-items: flex-end;
  z-index: 30;
}
.panel {
  width: 100%;
  background: var(--luka-cream);
  border-radius: 24px 24px 0 0;
  padding: 20px;
  max-height: 70vh;
  overflow: auto;
}
.panel input {
  width: 100%;
  height: 52px;
  border-radius: 18px;
  border: 1px solid var(--luka-outline);
  padding: 0 14px;
}
.split {
  display: flex;
  justify-content: space-between;
  margin-top: 14px;
}
.pills {
  display: flex;
  gap: 8px;
  overflow: auto;
  padding: 8px 0;
}
@media (min-width: 980px) {
  .sheet {
    align-items: center;
    justify-content: center;
  }
  .panel {
    max-width: 560px;
    border-radius: 24px;
  }
}
</style>
