<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import PageBackdrop from '@/components/ui/PageBackdrop.vue'
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
  <PageBackdrop image="/images/onboarding_kinshasa_1.jpg" cinematic>
    <div v-if="profile" class="wrap">
      <header>
        <div class="avatar">{{ profile.displayName.slice(0, 1).toUpperCase() || 'L' }}</div>
        <div>
          <div class="badges">
            <span>{{ profile.accountKind === 'learner' ? 'Apprenant' : 'Professionnel' }}</span>
            <span :class="{ gold: premium }">{{ premium ? 'Premium' : plan.name }}</span>
          </div>
          <h1>{{ profile.displayName || 'Ton profil' }} {{ profile.isCertified ? '✓' : '' }}</h1>
          <p>{{ [profile.tradeTitle || profile.profession?.title, profile.cityName].filter(Boolean).join(' · ') || 'Complète ton profil' }}</p>
        </div>
        <button class="edit" type="button" @click="router.push('/app/profil/edit')">✎</button>
      </header>

      <section>
        <h2>Type de compte</h2>
        <p>Tu peux passer d’apprenant à professionnel sans recréer le compte.</p>
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

      <section>
        <h2>Tes informations</h2>
        <dl>
          <div><dt>Nom</dt><dd>{{ profile.displayName || '—' }}</dd></div>
          <div><dt>Téléphone</dt><dd>{{ profile.identifier.value || '—' }}</dd></div>
          <div><dt>E-mail</dt><dd>{{ profile.email || '—' }}</dd></div>
          <div><dt>Métier</dt><dd>{{ profile.tradeTitle || profile.profession?.title || '—' }}</dd></div>
          <div><dt>Ville</dt><dd>{{ profile.cityName || '—' }}</dd></div>
          <div><dt>Pays</dt><dd>{{ RDC.flag }} {{ RDC.name }}</dd></div>
        </dl>
      </section>

      <section>
        <h2>Abonnement</h2>
        <strong>{{ premium ? 'Premium' : plan.name }}</strong>
        <p>
          {{
            profile.accountKind === 'learner' && learnerUnlocked(profile)
              ? 'Tendances, MIT, orientation et conseils réguliers débloqués.'
              : profile.accountKind === 'learner'
                ? 'Abonnement Étudiant (3 $) : tendances, news MIT, orientation, conseils.'
                : premium
                  ? 'Offres, analyse CV, analyse des offres, marché temps réel.'
                  : 'Abonnement Professionnel (5 $) : offres illimitées et analyses.'
          }}
        </p>
      </section>

      <section>
        <h2>Profil</h2>
        <p>{{ completeness.done }} / {{ completeness.total }} éléments remplis</p>
        <div class="bar"><i :style="{ width: `${(completeness.done / completeness.total) * 100}%` }" /></div>
      </section>

      <label class="cv" :class="{ ok: profile.cvFileName }">
        <input type="file" accept=".pdf,.doc,.docx" hidden @change="onCv" />
        <strong>{{ profile.cvFileName ? 'CV chargé' : 'Charger ton CV' }}</strong>
        <span>{{ profile.cvFileName || 'PDF, DOC ou DOCX — un tap, c’est envoyé' }}</span>
      </label>

      <button class="ghost" type="button" @click="router.push('/app/confidentialite')">Politique de confidentialité</button>
      <button class="ghost" type="button" @click="session.logout(); router.push('/')">Se déconnecter</button>
    </div>
  </PageBackdrop>
</template>

<style scoped>
.wrap {
  padding: 20px;
  max-width: 760px;
  margin: 0 auto;
  display: grid;
  gap: 16px;
  color: #fff;
}
header {
  display: flex;
  gap: 14px;
  align-items: center;
}
.avatar {
  width: 76px;
  height: 76px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--luka-red), #7a0c18);
  display: grid;
  place-items: center;
  font-size: 28px;
  font-weight: 900;
}
.badges {
  display: flex;
  gap: 8px;
}
.badges span {
  background: rgba(255, 255, 255, 0.92);
  color: var(--luka-red);
  border-radius: 99px;
  padding: 4px 10px;
  font-weight: 700;
  font-size: 13px;
}
.badges .gold {
  background: var(--luka-gold);
  color: #3a2208;
}
h1 {
  margin: 6px 0 0;
  font-weight: 900;
}
.edit {
  margin-left: auto;
  width: 44px;
  height: 44px;
  border: 0;
  border-radius: 50%;
  background: #fff;
  color: var(--luka-red);
  font-size: 18px;
}
section {
  background: #fff;
  color: var(--luka-ink);
  border-radius: 22px;
  padding: 18px;
}
h2 {
  color: var(--luka-red);
  margin: 0 0 8px;
  font-size: 14px;
}
.kinds {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px;
}
.kinds button {
  border: 0;
  border-radius: 16px;
  background: var(--luka-mist);
  padding: 12px;
  text-align: left;
  display: grid;
}
.kinds .on {
  background: var(--luka-red);
  color: #fff;
}
dl {
  display: grid;
  gap: 10px;
  margin: 0;
}
dt {
  color: var(--luka-muted);
  font-size: 13px;
}
dd {
  margin: 0;
  font-weight: 600;
}
.bar {
  height: 8px;
  background: var(--luka-mist);
  border-radius: 99px;
  overflow: hidden;
}
.bar i {
  display: block;
  height: 100%;
  background: var(--luka-red);
}
.cv {
  display: grid;
  place-items: center;
  gap: 6px;
  padding: 20px;
  border-radius: 22px;
  background: var(--luka-mist);
  color: var(--luka-ink);
  border: 2px dashed var(--luka-red);
  cursor: pointer;
}
.cv.ok {
  background: #e8f5e9;
  border-color: #2e7d32;
}
.ghost {
  border: 0;
  background: none;
  color: rgba(255, 255, 255, 0.85);
  font-weight: 700;
}
</style>
