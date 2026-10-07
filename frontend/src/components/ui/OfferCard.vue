<script setup lang="ts">
import { onMounted, ref } from 'vue'
import type { JobOffer } from '@/domain/models'
import LukaLogo from '@/components/brand/LukaLogo.vue'

const props = defineProps<{
  offer: JobOffer
  index?: number
}>()

const ready = ref(false)
onMounted(() => {
  window.setTimeout(() => {
    ready.value = true
  }, (props.index ?? 0) * 40)
})
</script>

<template>
  <article class="card" :class="{ ready }" :style="{ transitionDelay: `${(index ?? 0) * 50}ms` }">
    <div class="head">
      <div class="mark">
        <LukaLogo :height="18" />
      </div>
      <div>
        <h3>{{ offer.title }}</h3>
        <p>{{ offer.company }} · {{ offer.city }}</p>
      </div>
    </div>
    <p class="summary">{{ offer.summary }}</p>
    <div class="foot">
      <span>{{ offer.contract }}</span>
      <a :href="offer.applyUrl" target="_blank" rel="noreferrer">Voir l’offre</a>
    </div>
  </article>
</template>

<style scoped>
.card {
  background: #fff;
  border-radius: 22px;
  padding: 16px;
  box-shadow: var(--shadow);
  transform: scale(0.94);
  opacity: 0;
  transition: transform 0.32s var(--ease), opacity 0.32s var(--ease);
}
.card.ready {
  transform: scale(1);
  opacity: 1;
}
.head {
  display: flex;
  gap: 12px;
  align-items: center;
}
.mark {
  width: 52px;
  height: 52px;
  border-radius: 50%;
  background: #fff;
  display: grid;
  place-items: center;
  box-shadow: 0 0 0 1px var(--luka-mist);
  flex-shrink: 0;
}
h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
}
.head p,
.summary {
  margin: 0;
  color: var(--luka-muted);
  font-size: 14px;
}
.summary {
  margin: 10px 0;
  color: var(--luka-ink);
}
.foot {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}
.foot span {
  color: var(--luka-red);
  font-weight: 700;
  font-size: 14px;
}
.foot a {
  color: var(--luka-red);
  font-weight: 700;
  text-decoration: none;
}
</style>
