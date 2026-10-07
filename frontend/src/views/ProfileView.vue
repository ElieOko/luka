<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import PageHeader from '@/components/ui/PageHeader.vue'
import { RDC } from '@/data/catalog'
import { learnerUnlocked, planById, proUnlocked } from '@/data/plans'
import { ACCOUNT_KINDS } from '@/domain/models'
import { useSessionStore } from '@/stores/session'

const session = useSessionStore()
const router = useRouter()
const profile = computed(() => session.profile)
const plan = computed(() => planById(profile.value?.planId ?? 'starter'))
const premium = computed(() => Boolean(profile.value && (profile.value.isPremium || proUnlocked(profile.value))))
const completeness = computed(() => {
  const p = profile.value
  const learner = p?.accountKind === 'learner'
  const checks = [
    Boolean(p?.displayName),
    Boolean(p?.cityName),
    Boolean(p?.profession || p?.tradeTitle),
    Boolean(p?.email.includes('@')),
  ]
  if (!learner) checks.push(Boolean(p?.cvFileName))
  return { done: checks.filter(Boolean).length, total: checks.length }
})

function onCv(event: Event) {
  const file = (event.target as HTMLInputElement).files?.[0]
  if (!file) return
  session.saveCv(file.name, file.type)
}
</script>

<template>
  <div v-if="profile">
    <PageHeader :title="profile.displayName || 'Compte'" :subtitle="[profile.tradeTitle || profile.profession?.title, profile.cityName].filter(Boolean).join(' · ')">
      <template #actions>
        <button class="ghost" type="button" @click="router.push('/app/profil/edit')">Modifier</button>
        <button class="ghost" type="button" @click="session.logout(); router.push('/')">Déconnexion</button>
      </template>
    </PageHeader>

    <div class="grid">
      <section class="card">
        <h2>Type de compte</h2>
        <p>Passe d’apprenant à professionnel sans recréer le compte.</p>
        <div class="kinds">
          <button
            v-for="kind in ACCOUNT_KINDS"
            :key="kind.id"
            type="button"
            :class="{ on: profile.accountKind === kind.id }"
            @click="session.saveAccountKind(kind.id)"
          >
            <strong>{{ kind.title }}</strong>
            <small>{{ kind.id === 'learner' ? 'Études, MIT, orientation' : 'Offres, CV, marché' }}</small>
          </button>
        </div>
      </section>
      <section class="card">
        <h2>Informations</h2>
        <dl>
          <div><dt>Téléphone</dt><dd>{{ profile.identifier.value || '—' }}</dd></div>
          <div><dt>E-mail</dt><dd>{{ profile.email || '—' }}</dd></div>
          <div><dt>Ville</dt><dd>{{ profile.cityName || '—' }}</dd></div>
          <div><dt>Pays</dt><dd>{{ RDC.flag }} {{ RDC.name }}</dd></div>
        </dl>
      </section>
      <section class="card">
        <h2>Abonnement</h2>
        <p><strong>{{ premium ? 'Premium' : plan.name }}</strong></p>
        <p>
          {{
            profile.accountKind === 'learner' && learnerUnlocked(profile)
              ? 'Tendances, MIT, orientation et conseils débloqués.'
              : profile.accountKind === 'learner'
                ? 'Offre Étudiant (3 $).'
                : premium
                  ? 'Offres et analyses débloquées.'
                  : 'Offre Professionnel (5 $).'
          }}
        </p>
        <button class="link" type="button" @click="router.push('/app/abonnement')">Gérer l’abonnement</button>
      </section>
      <section class="card">
        <h2>Complétude</h2>
        <p>{{ completeness.done }} / {{ completeness.total }}</p>
        <div class="bar"><i :style="{ width: `${(completeness.done / completeness.total) * 100}%` }" /></div>
        <label class="cv">
          <input type="file" accept=".pdf,.doc,.docx" hidden @change="onCv" />
          {{ profile.cvFileName || 'Charger un CV (PDF, DOC)' }}
        </label>
      </section>
    </div>
  </div>
</template>

<style scoped>
.grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 14px;
}
.card {
  background: #fff;
  border: 1px solid var(--line);
  border-radius: var(--radius);
  padding: 20px;
}
h2 {
  margin: 0 0 8px;
  font-size: 14px;
  color: var(--luka-red);
}
.kinds {
  display: grid;
  gap: 8px;
}
.kinds button {
  text-align: left;
  border: 1px solid var(--line);
  background: #fff;
  border-radius: 12px;
  padding: 12px;
  display: grid;
}
.kinds .on {
  border-color: var(--luka-red);
  background: #fff5f5;
}
dl {
  display: grid;
  gap: 10px;
  margin: 0;
}
dt {
  color: var(--luka-muted);
  font-size: 12px;
}
dd {
  margin: 0;
  font-weight: 650;
}
.ghost {
  border: 1px solid var(--line);
  background: #fff;
  height: 40px;
  border-radius: 999px;
  padding: 0 14px;
  font-weight: 700;
}
.link {
  border: 0;
  background: none;
  color: var(--luka-red);
  font-weight: 700;
  padding: 0;
}
.bar {
  height: 8px;
  background: var(--luka-mist);
  border-radius: 99px;
  overflow: hidden;
  margin: 8px 0 12px;
}
.bar i {
  display: block;
  height: 100%;
  background: var(--luka-red);
}
.cv {
  display: block;
  border: 1px dashed var(--line);
  border-radius: 12px;
  padding: 12px;
  cursor: pointer;
  text-align: center;
  font-weight: 650;
}
@media (max-width: 800px) {
  .grid {
    grid-template-columns: 1fr;
  }
}
</style>
