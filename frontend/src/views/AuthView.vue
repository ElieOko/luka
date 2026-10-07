<script setup lang="ts">
import { computed, ref } from 'vue'
import { RouterLink, useRoute, useRouter } from 'vue-router'
import LukaLogo from '@/components/brand/LukaLogo.vue'
import LukaButton from '@/components/ui/LukaButton.vue'
import OtpBoxes from '@/components/ui/OtpBoxes.vue'
import { ACCOUNT_KINDS, type AccountKindId } from '@/domain/models'
import { useSessionStore } from '@/stores/session'
import { pathForDestination, resolveDestination } from '@/utils/destination'

const session = useSessionStore()
const router = useRouter()
const route = useRoute()

const step = ref<'id' | 'otp'>('id')
const input = ref('')
const otp = ref('')
const newAccount = ref(route.query.signup === '1')
const accountKind = ref<AccountKindId | null>(null)
const privacyAccepted = ref(false)
const loading = ref(false)
const error = ref<string | null>(null)
const info = ref<string | null>(null)
const phoneShown = ref('')

const headline = computed(() => {
  if (step.value === 'otp') return 'Code de confirmation'
  return newAccount.value ? 'Créer un compte Luka' : 'Connexion'
})

const cta = computed(() => {
  if (loading.value) return 'Un instant…'
  if (step.value === 'id' && newAccount.value) return 'Recevoir le code'
  if (step.value === 'id') return 'Recevoir le code'
  return 'Entrer sur la plateforme'
})

async function submit() {
  error.value = null
  info.value = null
  if (step.value === 'id') {
    if (newAccount.value && !accountKind.value) {
      error.value = 'Choisis un compte apprenant ou professionnel.'
      return
    }
    if (newAccount.value && !privacyAccepted.value) {
      error.value = 'Accepte la politique de confidentialité pour continuer.'
      return
    }
    loading.value = true
    try {
      const result = await session.requestOtp(input.value, newAccount.value, accountKind.value)
      if (result.kind === 'signed-in') {
        await router.replace(pathForDestination(resolveDestination(session.session)))
      } else {
        phoneShown.value = result.phone
        step.value = 'otp'
        info.value = 'Code envoyé par SMS.'
      }
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'Impossible d’envoyer le code.'
    } finally {
      loading.value = false
    }
    return
  }
  loading.value = true
  try {
    await session.verifyOtp(otp.value)
    await router.replace(pathForDestination(resolveDestination(session.session)))
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Code invalide.'
  } finally {
    loading.value = false
  }
}

async function resend() {
  loading.value = true
  error.value = null
  try {
    await session.resendOtp()
    otp.value = ''
    info.value = 'Nouveau code envoyé.'
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Impossible de renvoyer.'
  } finally {
    loading.value = false
  }
}

function toggleMode() {
  newAccount.value = !newAccount.value
  error.value = null
  info.value = null
  step.value = 'id'
  otp.value = ''
  accountKind.value = null
  privacyAccepted.value = false
}
</script>

<template>
  <main class="split">
    <aside class="brand">
      <RouterLink to="/" class="back">← Accueil</RouterLink>
      <img src="/images/onboarding_kinshasa_2.jpg" alt="" />
      <div class="overlay">
        <LukaLogo light :height="32" />
        <blockquote>
          « Luka trouve les offres pour des millions de jeunes qui ne savent pas où chercher. »
        </blockquote>
        <p>Kinshasa · Lubumbashi · Goma</p>
      </div>
    </aside>
    <section class="panel">
      <div class="card">
        <LukaLogo :height="28" />
        <h1>{{ headline }}</h1>
        <p class="lead">
          {{
            step === 'id'
              ? 'Numéro congolais. Un code SMS, sans mot de passe.'
              : `Code envoyé au ${phoneShown}.`
          }}
        </p>
        <template v-if="step === 'id'">
          <label class="field">
            Téléphone
            <input v-model="input" type="tel" placeholder="+243 81 000 0000" autocomplete="tel" />
          </label>
          <template v-if="newAccount">
            <p class="label">Type de compte</p>
            <div class="kinds">
              <button
                v-for="kind in ACCOUNT_KINDS"
                :key="kind.id"
                type="button"
                :class="{ on: accountKind === kind.id }"
                @click="accountKind = kind.id"
              >
                <strong>{{ kind.title }}</strong>
                <span>{{ kind.subtitle }}</span>
              </button>
            </div>
            <label class="check">
              <input v-model="privacyAccepted" type="checkbox" />
              <span>
                J’accepte la
                <RouterLink to="/confidentialite">politique de confidentialité</RouterLink>.
              </span>
            </label>
          </template>
        </template>
        <template v-else>
          <OtpBoxes v-model="otp" />
          <button class="text" type="button" :disabled="loading" @click="resend">Renvoyer le code</button>
        </template>
        <p v-if="info && !error" class="info">{{ info }}</p>
        <p v-if="error" class="error">{{ error }}</p>
        <LukaButton block :loading="loading" :disabled="loading" @click="submit">{{ cta }}</LukaButton>
        <button v-if="step === 'id'" class="text" type="button" @click="toggleMode">
          {{ newAccount ? 'Déjà un compte ? Se connecter' : 'Pas encore de compte ? Créer un compte' }}
        </button>
      </div>
    </section>
  </main>
</template>

<style scoped>
.split {
  min-height: 100dvh;
  display: grid;
  grid-template-columns: 0.9fr 1.1fr;
}
.brand {
  position: relative;
  overflow: hidden;
  background: var(--luka-wine);
}
.brand img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  opacity: 0.55;
}
.overlay {
  position: absolute;
  inset: 0;
  padding: 32px;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  color: #fff;
  background: linear-gradient(transparent, rgba(20, 6, 8, 0.78));
}
.back {
  position: absolute;
  top: 28px;
  left: 32px;
  z-index: 2;
  color: #fff;
  text-decoration: none;
  font-weight: 600;
}
blockquote {
  font-size: 26px;
  font-weight: 650;
  line-height: 1.25;
  margin: 18px 0 8px;
}
.panel {
  display: grid;
  place-items: center;
  padding: 40px 24px;
  background: var(--canvas);
}
.card {
  width: min(440px, 100%);
  background: #fff;
  border: 1px solid var(--line);
  border-radius: 20px;
  padding: 32px;
  box-shadow: var(--shadow);
  display: grid;
  gap: 14px;
}
h1 {
  margin: 8px 0 0;
  font-size: 28px;
  letter-spacing: -0.03em;
}
.lead,
.info {
  color: var(--luka-muted);
  margin: 0;
}
.label {
  font-size: 13px;
  font-weight: 700;
  color: var(--luka-muted);
  margin: 4px 0 0;
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
  gap: 4px;
}
.kinds button span {
  color: var(--luka-muted);
  font-size: 12px;
}
.kinds .on {
  border-color: var(--luka-red);
  background: #fff5f5;
}
.check {
  display: flex;
  gap: 8px;
  align-items: flex-start;
  font-size: 14px;
}
.check a {
  color: var(--luka-red);
}
.text {
  border: 0;
  background: none;
  color: var(--luka-red);
  font-weight: 700;
  justify-self: start;
  padding: 0;
}
.error {
  color: var(--luka-red-deep);
  margin: 0;
}
@media (max-width: 860px) {
  .split {
    grid-template-columns: 1fr;
  }
  .brand {
    min-height: 180px;
  }
}
</style>
