<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import LockedCard from '@/components/ui/LockedCard.vue'
import PageHeader from '@/components/ui/PageHeader.vue'
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
  <div>
    <PageHeader
      :title="realtime ? 'Marché' : 'Tendances'"
      :subtitle="realtime ? 'Ouvertures agrégées à partir du flux serveur.' : 'Métiers les plus demandés cette semaine.'"
    />
    <p v-if="!stats.length" class="empty">Les tendances se construisent à partir des offres du backend.</p>
    <div class="grid">
      <article v-for="item in stats" :key="item.profession.id">
        <div class="head">
          <strong>{{ item.profession.emoji }} {{ item.profession.title }}</strong>
          <span>{{ item.sharePercent }} %</span>
        </div>
        <p>{{ item.trend }}</p>
        <div class="bar"><i :style="{ width: `${item.sharePercent}%` }" /></div>
      </article>
    </div>
    <LockedCard
      v-if="!unlocked"
      :title="realtime ? 'Marché temps réel' : 'Tendances complètes'"
      body="L’abonnement ouvre le diagnostic entier."
      @unlock="router.push('/app/abonnement')"
    />
  </div>
</template>

<style scoped>
.grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 14px;
  margin-bottom: 18px;
}
article {
  background: #fff;
  border: 1px solid var(--line);
  border-radius: var(--radius);
  padding: 18px;
}
.head {
  display: flex;
  justify-content: space-between;
}
p,
.empty {
  color: var(--luka-muted);
}
.bar {
  height: 8px;
  border-radius: 99px;
  background: var(--luka-mist);
  overflow: hidden;
  margin-top: 10px;
}
.bar i {
  display: block;
  height: 100%;
  background: var(--luka-red);
  animation: fill 0.7s var(--ease) both;
}
@media (max-width: 800px) {
  .grid {
    grid-template-columns: 1fr;
  }
}
</style>
