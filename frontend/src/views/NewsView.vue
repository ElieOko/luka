<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import LockedCard from '@/components/ui/LockedCard.vue'
import PageHeader from '@/components/ui/PageHeader.vue'
import { NEWS, formatNewsDate, newsTabs } from '@/data/learner'
import { learnerUnlocked } from '@/data/plans'
import { imageUrl } from '@/domain/models'
import { useSessionStore } from '@/stores/session'

const session = useSessionStore()
const router = useRouter()
const tabs = newsTabs(NEWS)
const tab = ref(tabs[1] ?? 'Favoris')
const unlocked = computed(() => Boolean(session.profile && learnerUnlocked(session.profile)))

const visible = computed(() => {
  if (tab.value === 'Favoris') return NEWS.filter((item) => session.favoriteNewsIds.includes(item.id))
  if (tab.value === 'Souscrit') return NEWS.filter((item) => item.subscribed)
  return NEWS.filter((item) => !item.subscribed && item.domain === tab.value)
})
const featured = computed(() => visible.value[0] ?? null)
const rest = computed(() => visible.value.slice(1))
</script>

<template>
  <div>
    <PageHeader title="News" subtitle="Articles classés par domaine — lecture magazine, pas un fil vertical." />
    <div class="tabs">
      <button v-for="label in tabs" :key="label" type="button" :class="{ on: tab === label }" @click="tab = label">
        {{ label }}
      </button>
    </div>
    <LockedCard
      v-if="tab === 'Souscrit' && !unlocked"
      title="Dossier Souscrit"
      body="Briefs de marché et pipelines de stages, avec l’abonnement."
      @unlock="router.push('/app/abonnement')"
    />
    <article v-if="featured" class="featured" @click="router.push(`/app/news/${featured.id}`)">
      <img :src="imageUrl(featured.imageName)" :alt="featured.title" />
      <div>
        <span>{{ featured.domain }}</span>
        <h2>{{ featured.title }}</h2>
        <p>{{ featured.excerpt }}</p>
        <small>{{ featured.author }} · {{ formatNewsDate(featured.publishedAtEpochMs) }}</small>
      </div>
    </article>
    <div class="grid">
      <article v-for="item in rest" :key="item.id" @click="router.push(`/app/news/${item.id}`)">
        <img :src="imageUrl(item.imageName)" :alt="item.title" />
        <div>
          <span>{{ item.domain }}</span>
          <h3>{{ item.title }}</h3>
          <p>{{ item.excerpt }}</p>
          <small>{{ item.author }} · {{ formatNewsDate(item.publishedAtEpochMs) }}</small>
        </div>
      </article>
    </div>
    <p v-if="!visible.length && tab !== 'Souscrit'" class="empty">Rien dans cet onglet.</p>
  </div>
</template>

<style scoped>
.tabs {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin-bottom: 22px;
}
.tabs button {
  border: 1px solid var(--line);
  background: #fff;
  border-radius: 999px;
  padding: 8px 14px;
  font-weight: 650;
}
.tabs .on {
  background: var(--luka-red);
  color: #fff;
  border-color: var(--luka-red);
}
.featured {
  display: grid;
  grid-template-columns: 1.1fr 0.9fr;
  gap: 0;
  background: #fff;
  border: 1px solid var(--line);
  border-radius: var(--radius);
  overflow: hidden;
  cursor: pointer;
  margin-bottom: 18px;
  box-shadow: var(--shadow);
}
.featured img {
  height: 280px;
  width: 100%;
  object-fit: cover;
}
.featured > div,
.grid article > div {
  padding: 22px;
  display: grid;
  align-content: center;
  gap: 8px;
}
.grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}
.grid article {
  background: #fff;
  border: 1px solid var(--line);
  border-radius: var(--radius);
  overflow: hidden;
  cursor: pointer;
  display: grid;
  transition: transform 0.2s var(--ease), box-shadow 0.2s;
}
.grid article:hover,
.featured:hover {
  transform: translateY(-3px);
  box-shadow: var(--shadow);
}
.grid img {
  height: 140px;
  width: 100%;
  object-fit: cover;
}
span {
  color: var(--luka-red);
  font-weight: 700;
  font-size: 12px;
  letter-spacing: 0.04em;
  text-transform: uppercase;
}
h2,
h3 {
  margin: 0;
}
h2 {
  font-size: 28px;
  letter-spacing: -0.03em;
}
h3 {
  font-size: 17px;
}
p,
small,
.empty {
  color: var(--luka-muted);
  margin: 0;
}
@media (max-width: 980px) {
  .featured,
  .grid {
    grid-template-columns: 1fr;
  }
  .featured img {
    height: 180px;
  }
}
</style>
