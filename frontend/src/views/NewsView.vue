<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import LockedCard from '@/components/ui/LockedCard.vue'
import PageBackdrop from '@/components/ui/PageBackdrop.vue'
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
</script>

<template>
  <PageBackdrop image="/images/onboarding_kinshasa_2.jpg" cinematic>
    <div class="wrap">
      <h1>News</h1>
      <p>Les articles se lisent ici, classés par domaine.</p>
      <div class="tabs">
        <button
          v-for="label in tabs"
          :key="label"
          type="button"
          :class="{ on: tab === label }"
          @click="tab = label"
        >
          {{ label }}
        </button>
      </div>
      <LockedCard
        v-if="tab === 'Souscrit' && !unlocked"
        title="Dossier Souscrit"
        body="Briefs de marché, contrats, pipelines de stages : les infos précieuses, avec l’abonnement."
        @unlock="router.push('/app/abonnement')"
      />
      <article
        v-for="item in visible"
        :key="item.id"
        class="card"
        @click="router.push(`/app/news/${item.id}`)"
      >
        <img :src="imageUrl(item.imageName)" :alt="item.title" />
        <div>
          <strong class="domain">{{ item.domain }}</strong>
          <h2>{{ item.title }}</h2>
          <p>{{ item.excerpt }}</p>
          <small>{{ item.author }} · {{ formatNewsDate(item.publishedAtEpochMs) }}</small>
        </div>
        <button
          type="button"
          class="fav"
          @click.stop="session.toggleNewsFavorite(item.id)"
        >
          {{ session.favoriteNewsIds.includes(item.id) ? '★' : '☆' }}
        </button>
      </article>
      <p v-if="!visible.length && tab !== 'Souscrit'" class="empty">Rien ici pour l’instant.</p>
    </div>
  </PageBackdrop>
</template>

<style scoped>
.wrap {
  padding: 20px;
  max-width: 880px;
  margin: 0 auto;
}
h1 {
  color: #fff;
  margin: 0;
  font-weight: 900;
}
p {
  color: rgba(255, 255, 255, 0.85);
}
.tabs {
  display: flex;
  gap: 8px;
  overflow: auto;
  margin: 12px 0;
}
.tabs button {
  border: 0;
  background: rgba(255, 255, 255, 0.14);
  color: #fff;
  border-radius: 99px;
  padding: 8px 12px;
  font-weight: 600;
  white-space: nowrap;
}
.tabs .on {
  background: var(--luka-red);
}
.card {
  position: relative;
  background: #fff;
  border-radius: 18px;
  overflow: hidden;
  margin-bottom: 12px;
  cursor: pointer;
  display: grid;
  animation: rise 0.4s var(--ease) both;
}
.card img {
  height: 140px;
  width: 100%;
  object-fit: cover;
}
.card div {
  padding: 14px 16px 16px;
}
.domain {
  color: var(--luka-red);
}
h2 {
  margin: 4px 0;
  font-size: 18px;
}
.card p {
  color: var(--luka-muted);
  margin: 0 0 6px;
}
.fav {
  position: absolute;
  top: 10px;
  right: 10px;
  border: 0;
  background: rgba(255, 255, 255, 0.92);
  width: 36px;
  height: 36px;
  border-radius: 50%;
  color: var(--luka-red);
  font-size: 18px;
}
.empty {
  color: #fff;
}
@keyframes rise {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
}
</style>
