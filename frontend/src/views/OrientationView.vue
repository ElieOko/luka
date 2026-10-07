<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import LukaButton from '@/components/ui/LukaButton.vue'
import PageBackdrop from '@/components/ui/PageBackdrop.vue'
import RadarPulse from '@/components/ui/RadarPulse.vue'
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
  }, 1800)
}
</script>

<template>
  <PageBackdrop image="/images/onboarding_kinshasa_3.jpg" cinematic>
    <div v-if="phase === 'pick'" class="wrap">
      <h1>Orientation</h1>
      <p>Informatif, comme les offres : on analyse le marché national selon qui tu es.</p>
      <button
        v-for="persona in PERSONAS"
        :key="persona.id"
        type="button"
        class="persona"
        :class="{ on: selected?.id === persona.id }"
        @click="selected = persona"
      >
        <strong>{{ persona.title }}</strong>
        <span>{{ persona.subtitle }}</span>
      </button>
      <LukaButton :disabled="!selected && unlocked" @click="launch">
        {{ unlocked ? 'Lancer l’analyse' : 'Débloquer l’orientation' }}
      </LukaButton>
    </div>
    <div v-else-if="phase === 'analyzing'" class="wrap center">
      <h1>Analyse nationale</h1>
      <p>
        {{
          selected?.id === 'eleve'
            ? 'Lecture des filières d’études supérieures les plus demandées…'
            : selected?.id === 'etudiant'
              ? 'Lecture des premiers emplois après les études…'
              : selected?.id === 'employe'
                ? 'Lecture des reconversions et autres emplois…'
                : selected?.id === 'employeur'
                  ? 'Lecture des domaines où investir…'
                  : 'Scan du marché congolais…'
        }}
      </p>
      <RadarPulse />
      <p>À partir des offres actuellement ouvertes sur le serveur.</p>
    </div>
    <div v-else class="wrap">
      <p class="kicker">{{ selected?.title }}</p>
      <h1>{{ selected?.resultTitle }}</h1>
      <p>Chiffres tirés des offres actuellement ouvertes sur Luka.</p>
      <article v-for="item in insights" :key="item.label" class="result">
        <div class="head">
          <b>{{ item.rank }}</b>
          <div>
            <strong>{{ item.label }}</strong>
            <small>{{ item.sharePercent }} % · {{ item.detail }}</small>
          </div>
        </div>
        <div class="bar"><i :style="{ width: `${item.sharePercent}%` }" /></div>
      </article>
      <p v-if="!insights.length">Pas encore assez d’offres pour un diagnostic.</p>
      <button class="ghost" type="button" @click="phase = 'pick'">Changer de profil</button>
    </div>
  </PageBackdrop>
</template>

<style scoped>
.wrap {
  padding: 20px;
  max-width: 720px;
  margin: 0 auto;
  display: grid;
  gap: 12px;
  color: #fff;
}
.center {
  text-align: center;
  justify-items: center;
}
h1 {
  margin: 0;
  font-weight: 900;
}
.persona {
  text-align: left;
  border: 1px solid rgba(255, 255, 255, 0.22);
  background: rgba(255, 255, 255, 0.14);
  color: #fff;
  border-radius: 20px;
  padding: 16px;
  display: grid;
  gap: 4px;
}
.persona.on {
  background: var(--luka-red);
  border-color: transparent;
}
.result {
  background: rgba(255, 255, 255, 0.12);
  border-radius: 18px;
  padding: 16px;
}
.head {
  display: flex;
  gap: 12px;
  align-items: center;
}
.head small {
  display: block;
  opacity: 0.8;
}
.bar {
  height: 10px;
  margin-top: 10px;
  border-radius: 99px;
  background: rgba(227, 27, 35, 0.22);
}
.bar i {
  display: block;
  height: 100%;
  background: var(--luka-red);
  border-radius: 99px;
}
.ghost {
  border: 0;
  background: none;
  color: #fff;
  font-weight: 700;
}
.kicker {
  font-weight: 700;
  margin: 0;
}
</style>
