<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import LockedCard from '@/components/ui/LockedCard.vue'
import PageBackdrop from '@/components/ui/PageBackdrop.vue'
import { learnerUnlocked, proUnlocked } from '@/data/plans'
import { useCatalogStore } from '@/stores/catalog'
import { useSessionStore } from '@/stores/session'

const session = useSessionStore()
const catalog = useCatalogStore()
const router = useRouter()
const realtime = computed(() => !session.isLearner)
const unlocked = computed(() => {
  if (!session.profile) return false
  return realtime.value ? proUnlocked(session.profile) : learnerUnlocked(session.profile) || proUnlocked(session.profile)
})
const stats = computed(() => (unlocked.value ? catalog.stats : catalog.stats.slice(0, 2)))
</script>

<template>
  <PageBackdrop image="/images/onboarding_kinshasa_2.jpg" cinematic>
    <div class="wrap">
      <h1>{{ realtime ? 'Marché en temps réel' : 'Tendances' }}</h1>
      <p>
        {{
          realtime
            ? 'Les ouvertures qui bougent maintenant, à partir des offres du serveur.'
            : 'Les métiers les plus demandés cette semaine'
        }}
      </p>
      <p v-if="!stats.length" class="empty">Les tendances se construisent à partir des offres du backend.</p>
      <article v-for="(item, i) in stats" :key="item.profession.id" class="stat" :style="{ animationDelay: `${i * 70}ms` }">
        <div class="head">
          <strong>{{ item.profession.emoji }} {{ item.profession.title }}</strong>
          <span>{{ item.sharePercent }} %</span>
        </div>
        <p>{{ item.trend }}</p>
        <div class="bar">
          <i :style="{ width: `${item.sharePercent}%` }" />
        </div>
      </article>
      <LockedCard
        v-if="!unlocked"
        :title="realtime ? 'Marché temps réel' : 'Tendances complètes'"
        body="L’abonnement ouvre le diagnostic entier, pas seulement les deux premiers métiers."
        @unlock="router.push('/app/abonnement')"
      />
    </div>
  </PageBackdrop>
</template>

<style scoped>
.wrap {
  padding: 24px 20px 40px;
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
.stat {
  background: #fff;
  border-radius: 18px;
  padding: 16px;
  margin-bottom: 10px;
  animation: rise 0.5s var(--ease) both;
}
.head {
  display: flex;
  justify-content: space-between;
}
.stat p {
  color: var(--luka-muted);
  margin: 6px 0 10px;
}
.bar {
  height: 10px;
  border-radius: 99px;
  background: rgba(227, 27, 35, 0.22);
  overflow: hidden;
}
.bar i {
  display: block;
  height: 100%;
  background: var(--luka-red);
  border-radius: 99px;
  animation: fill 0.8s var(--ease) both;
}
.empty {
  color: #fff;
}
@keyframes rise {
  from {
    opacity: 0;
    transform: translateY(8px);
  }
}
@keyframes fill {
  from {
    width: 0 !important;
  }
}
</style>
