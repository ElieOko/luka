<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { NEWS, formatNewsDate } from '@/data/learner'
import { learnerUnlocked } from '@/data/plans'
import { imageUrl } from '@/domain/models'
import { useSessionStore } from '@/stores/session'

const route = useRoute()
const router = useRouter()
const session = useSessionStore()
const item = computed(() => NEWS.find((article) => article.id === route.params.id))
const locked = computed(
  () => item.value?.subscribed && !(session.profile && learnerUnlocked(session.profile)),
)
</script>

<template>
  <div>
  <article v-if="item" class="read">
    <button type="button" @click="router.push('/app/news')">← News</button>
    <p class="domain">{{ item.domain }}</p>
    <h1>{{ item.title }}</h1>
    <p class="meta">{{ item.author }} · {{ formatNewsDate(item.publishedAtEpochMs) }}</p>
    <img :src="imageUrl(item.imageName)" :alt="item.title" />
    <div v-if="locked" class="lock">
      <p>Ce dossier est réservé aux abonnés.</p>
      <button class="cta" type="button" @click="router.push('/app/abonnement')">Débloquer</button>
    </div>
    <p v-for="(para, i) in item.body.split('\n\n')" v-else :key="i">{{ para }}</p>
  </article>
  <div v-else>
    <p>Article introuvable.</p>
    <button type="button" @click="router.push('/app/news')">Retour</button>
  </div>
  </div>
</template>

<style scoped>
.read {
  max-width: 760px;
  background: #fff;
  border: 1px solid var(--line);
  border-radius: 18px;
  padding: 28px 32px 40px;
  box-shadow: var(--shadow);
}
img {
  width: 100%;
  height: 280px;
  object-fit: cover;
  border-radius: 16px;
  margin: 8px 0 18px;
}
.domain {
  color: var(--luka-red);
  font-weight: 800;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  font-size: 12px;
}
h1 {
  font-size: clamp(28px, 4vw, 40px);
  letter-spacing: -0.03em;
  margin: 0 0 8px;
}
.meta,
p {
  color: var(--luka-ink);
  line-height: 1.6;
  font-size: 17px;
}
.meta {
  color: var(--luka-muted);
}
button {
  border: 0;
  background: none;
  font-weight: 700;
  color: var(--luka-muted);
}
.cta {
  background: var(--luka-red);
  color: #fff;
  border-radius: 999px;
  height: 40px;
  padding: 0 16px;
}
.lock {
  background: #fff;
  border: 1px dashed var(--luka-red);
  border-radius: 14px;
  padding: 16px;
}
</style>
