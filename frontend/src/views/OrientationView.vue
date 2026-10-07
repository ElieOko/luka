<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import LukaButton from '@/components/ui/LukaButton.vue'
import PageHeader from '@/components/ui/PageHeader.vue'
import { PERSONAS } from '@/data/learner'
import { learnerUnlocked } from '@/data/plans'
import type { NationalInsight, OrientationPersona } from '@/domain/models'
import { useCatalogStore } from '@/stores/catalog'
import { useSessionStore } from '@/stores/session'
import { demandFrom } from '@/utils/destination'

const session = useSessionStore()
const catalog = useCatalogStore()
const router = useRouter()
const phase = ref<'pick' | 'analyzing' | 'results'>('pick')
const selected = ref<OrientationPersona | null>(null)
const insights = ref<NationalInsight[]>([])
const unlocked = computed(() => Boolean(session.profile && learnerUnlocked(session.profile)))

function launch() {
  if (!unlocked.value) {
    void router.push('/app/abonnement')
    return
  }
  if (!selected.value) return
  phase.value = 'analyzing'
  window.setTimeout(() => {
    const persona = selected.value!
    insights.value = demandFrom(catalog.offers)
      .slice(0, 7)
      .map((stat, index) => ({
        persona,
        rank: index + 1,
        label: stat.profession.title,
        sharePercent: stat.sharePercent,
        detail: `${stat.openings} offres ouvertes — ${persona.lens}.`,
      }))
    phase.value = 'results'
  }, 1400)
}
</script>

<template>
  <div>
    <PageHeader title="Orientation" subtitle="Lecture nationale du marché selon qui tu es." />
    <div v-if="phase === 'pick'">
      <div class="personas">
        <button
          v-for="persona in PERSONAS"
          :key="persona.id"
          type="button"
          :class="{ on: selected?.id === persona.id }"
          @click="selected = persona"
        >
          <strong>{{ persona.title }}</strong>
          <span>{{ persona.subtitle }}</span>
        </button>
      </div>
      <div class="cta">
        <LukaButton :disabled="!selected && unlocked" @click="launch">
          {{ unlocked ? 'Lancer l’analyse' : 'Débloquer l’orientation' }}
        </LukaButton>
      </div>
    </div>
    <div v-else-if="phase === 'analyzing'" class="center">
      <div class="bar wide"><i /></div>
      <p>Lecture des offres ouvertes sur le serveur…</p>
    </div>
    <div v-else>
      <h2>{{ selected?.resultTitle }}</h2>
      <article v-for="item in insights" :key="item.label" class="result">
        <b>{{ item.rank }}</b>
        <div>
          <strong>{{ item.label }}</strong>
          <small>{{ item.sharePercent }} % · {{ item.detail }}</small>
          <div class="bar"><i :style="{ width: `${item.sharePercent}%` }" /></div>
        </div>
      </article>
      <p v-if="!insights.length">Pas encore assez d’offres pour un diagnostic.</p>
      <button class="text" type="button" @click="phase = 'pick'">Changer de profil</button>
    </div>
  </div>
</template>

<style scoped>
.personas {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}
.personas button {
  text-align: left;
  border: 1px solid var(--line);
  background: #fff;
  border-radius: var(--radius);
  padding: 18px;
  display: grid;
  gap: 4px;
}
.personas span {
  color: var(--luka-muted);
}
.personas .on {
  border-color: var(--luka-red);
  background: #fff5f5;
}
.center {
  display: grid;
  justify-items: center;
  gap: 12px;
  padding: 48px 0;
  background: #fff;
  border: 1px solid var(--line);
  border-radius: var(--radius);
}
.wide {
  width: min(420px, 90%);
}
.wide i {
  width: 40%;
  animation: fill 1.2s var(--ease) infinite alternate;
}
.result {
  display: flex;
  gap: 14px;
  background: #fff;
  border: 1px solid var(--line);
  border-radius: 14px;
  padding: 16px;
  margin-bottom: 10px;
}
.bar {
  height: 8px;
  background: var(--luka-mist);
  border-radius: 99px;
  overflow: hidden;
  margin-top: 8px;
}
.bar i {
  display: block;
  height: 100%;
  background: var(--luka-red);
}
.cta {
  margin-top: 16px;
}
.text {
  border: 0;
  background: none;
  color: var(--luka-red);
  font-weight: 700;
}
@media (max-width: 700px) {
  .personas {
    grid-template-columns: 1fr;
  }
}
</style>
