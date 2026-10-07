<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import LukaButton from '@/components/ui/LukaButton.vue'
import { PAID_PLANS, planByApiId, suggestedPlan } from '@/data/plans'
import { lukaApi } from '@/api/lukaApi'
import { phoneForPayment } from '@/utils/phone'
import { useSessionStore } from '@/stores/session'
import type { AbonnementDto, DeviseDto } from '@/api/types'

const session = useSessionStore()
const router = useRouter()
const method = ref<'mobile' | 'card'>('mobile')
const phone = ref(session.profile?.identifier.value ?? '')
const plans = ref(PAID_PLANS)
const selected = ref(suggestedPlan(session.profile?.accountKind ?? 'professional'))
const devises = ref<DeviseDto[]>([])
const devise = ref('USD')
const loading = ref(false)
const message = ref<string | null>(null)
const error = ref<string | null>(null)
const suggested = computed(() => suggestedPlan(session.profile?.accountKind ?? 'professional'))

onMounted(async () => {
  try {
    const [abos, currencies] = await Promise.all([lukaApi.listAbonnements(), lukaApi.listDevises()])
    if (abos.length) {
      plans.value = abos.filter((item) => item.active !== false).map((item) => toPlan(item))
      selected.value = plans.value.find((plan) => plan.id === suggested.value.id) ?? plans.value[0]!
    }
    devises.value = currencies
    if (currencies[0]) devise.value = currencies[0].code
  } catch {
    /* fallback to local plans */
  }
})

function toPlan(dto: AbonnementDto) {
  const local = planByApiId(dto.id)
  return {
    ...local,
    name: dto.name || local.name,
    usdAmount: dto.amountUsd ?? local.usdAmount,
    apiId: dto.id,
    priceLabel: `${dto.amountUsd ?? local.usdAmount} $`,
  }
}

async function pay() {
  error.value = null
  message.value = null
  loading.value = true
  try {
    const msisdn = phoneForPayment(phone.value)
    const result =
      method.value === 'mobile'
        ? await lukaApi.payMobileMoney(selected.value.apiId, devise.value, msisdn)
        : await lukaApi.payWithCard(selected.value.apiId, devise.value, msisdn)
    if (result.url) window.open(result.url, '_blank')
    message.value = result.message || (result.paymentAccepted ? 'Paiement accepté.' : 'Paiement initié.')
    if (result.paymentAccepted) session.selectPlan(selected.value.id)
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Paiement impossible.'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <main class="pay">
    <button class="back" type="button" @click="router.back()">← Retour</button>
    <h1>Abonnement</h1>
    <p>Le même parcours que sur mobile : un plan, un numéro, Mobile Money ou carte.</p>
    <div class="plans">
      <button
        v-for="plan in plans"
        :key="plan.id"
        type="button"
        :class="{ on: selected.id === plan.id, hi: plan.highlight }"
        @click="selected = plan"
      >
        <strong>{{ plan.name }}</strong>
        <span>{{ plan.priceLabel }} {{ plan.period }}</span>
        <small v-for="perk in plan.perks" :key="perk">{{ perk }}</small>
      </button>
    </div>
    <div class="tabs">
      <button type="button" :class="{ on: method === 'mobile' }" @click="method = 'mobile'">Mobile Money</button>
      <button type="button" :class="{ on: method === 'card' }" @click="method = 'card'">Carte</button>
    </div>
    <label>Téléphone<input v-model="phone" type="tel" placeholder="243 81 000 0000" /></label>
    <label>
      Devise
      <select v-model="devise">
        <option v-for="item in devises.length ? devises : [{ code: 'USD', name: 'Dollar US', tauxLocal: 1, id: 1 }]" :key="item.code" :value="item.code">
          {{ item.name }} ({{ item.code }})
        </option>
      </select>
    </label>
    <p v-if="message">{{ message }}</p>
    <p v-if="error" class="error">{{ error }}</p>
    <LukaButton :loading="loading" @click="pay">Payer {{ selected.priceLabel }}</LukaButton>
  </main>
</template>

<style scoped>
.pay {
  max-width: 720px;
  margin: 0 auto;
  padding: 20px;
  display: grid;
  gap: 14px;
}
h1 {
  margin: 0;
  font-weight: 900;
}
.plans {
  display: grid;
  gap: 10px;
}
.plans button {
  text-align: left;
  border: 1px solid var(--luka-outline);
  background: #fff;
  border-radius: 18px;
  padding: 14px;
  display: grid;
  gap: 4px;
}
.plans .on {
  border-color: var(--luka-red);
  background: var(--luka-mist);
}
.plans .hi.on {
  background: var(--luka-red);
  color: #fff;
}
.tabs {
  display: grid;
  grid-template-columns: 1fr 1fr;
  background: var(--luka-mist);
  border-radius: 16px;
  padding: 4px;
}
.tabs button {
  border: 0;
  background: transparent;
  border-radius: 12px;
  height: 40px;
  font-weight: 700;
}
.tabs .on {
  background: #fff;
  color: var(--luka-red);
}
label {
  display: grid;
  gap: 6px;
  font-weight: 600;
}
input,
select {
  height: 52px;
  border-radius: 16px;
  border: 1px solid var(--luka-outline);
  padding: 0 14px;
}
.back {
  width: fit-content;
  border: 0;
  background: none;
  font-weight: 700;
}
.error {
  color: var(--luka-red-deep);
}
</style>
