<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import PageBackdrop from '@/components/ui/PageBackdrop.vue'
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
  <PageBackdrop v-if="item" :image="imageUrl(item.imageName)" cinematic>
    <article class="article">
      <button type="button" @click="router.back()">← Retour</button>
      <span>{{ item.domain.toUpperCase() }}</span>
      <h1>{{ item.title }}</h1>
      <p class="meta">{{ item.author }} · {{ formatNewsDate(item.publishedAtEpochMs) }}</p>
      <div v-if="locked" class="lock">
        <p>Ce dossier est réservé aux abonnés.</p>
        <button type="button" @click="router.push('/app/abonnement')">Débloquer</button>
      </div>
      <p v-for="(para, i) in item.body.split('\n\n')" v-else :key="i">{{ para }}</p>
    </article>
  </PageBackdrop>
  <div v-else class="missing">
    <p>Article introuvable.</p>
    <button type="button" @click="router.push('/app/news')">Retour aux news</button>
  </div>
</template>

<style scoped>
.article {
  max-width: 720px;
  margin: 0 auto;
  padding: 24px 20px 48px;
  color: #fff;
}
h1 {
  font-size: clamp(28px, 4vw, 40px);
  font-weight: 900;
  margin: 8px 0;
}
span {
  color: var(--luka-red);
  font-weight: 800;
}
.meta {
  color: rgba(255, 255, 255, 0.75);
}
p {
  font-size: 17px;
  line-height: 1.55;
}
button {
  border: 0;
  background: none;
  color: #fff;
  font-weight: 700;
  padding: 0 0 16px;
}
.lock {
  background: rgba(255, 255, 255, 0.12);
  border-radius: 18px;
  padding: 16px;
}
.missing {
  padding: 40px 20px;
}
</style>
