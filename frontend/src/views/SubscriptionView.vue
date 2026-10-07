<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import LukaButton from '@/components/ui/LukaButton.vue'
import PageHeader from '@/components/ui/PageHeader.vue'
import { PAID_PLANS, planByApiId, suggestedPlan } from '@/data/plans'
import { lukaApi } from '@/api/lukaApi'
import { phoneForPayment } from '@/utils/phone'
import { useSessionStore } from '@/stores/session'
import type { AbonnementDto, DeviseDto } from '@/api/types'

const session = useSessionStore()
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
    /* local plans */
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
  <div>
    <PageHeader title="Abonnement" subtitle="Un plan, un numéro, Mobile Money ou carte." />
    <div class="layout">
    <div class="plans">
      <button
        v-for="plan in plans"
        :key="plan.id"
        type="button"
        :class="{ on: selected.id === plan.id, hi: plan.highlight }"
        @click="selected = plan"
      >
        <h3>{{ plan.name }}</h3>
        <p class="price">{{ plan.priceLabel }} <small>{{ plan.period }}</small></p>
        <ul>
          <li v-for="perk in plan.perks" :key="perk">{{ perk }}</li>
        </ul>
      </button>
    </div>
    <div class="checkout">
      <div class="tabs">
        <button type="button" :class="{ on: method === 'mobile' }" @click="method = 'mobile'">Mobile Money</button>
        <button type="button" :class="{ on: method === 'card' }" @click="method = 'card'">Carte</button>
      </div>
      <label class="field">Téléphone<input v-model="phone" type="tel" placeholder="243 81 000 0000" /></label>
      <label class="field">
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
    </div>
    </div>
  </div>
</template>

<style scoped>
.layout {
  display: grid;
  grid-template-columns: 1.2fr 0.8fr;
  gap: 22px;
  align-items: start;
}
.plans {
  display: grid;
  gap: 14px;
}
.plans button {
  text-align: left;
  border: 1px solid var(--line);
  background: #fff;
  border-radius: 16px;
  padding: 20px;
}
.plans .on {
  border-color: var(--luka-red);
}
.price {
  font-size: 26px;
  font-weight: 800;
  margin: 6px 0 12px;
}
ul {
  margin: 0;
  padding-left: 16px;
  color: var(--luka-muted);
}
.checkout {
  display: grid;
  gap: 12px;
  background: #fff;
  border: 1px solid var(--line);
  border-radius: 16px;
  padding: 20px;
}
.tabs {
  display: grid;
  grid-template-columns: 1fr 1fr;
  background: var(--luka-mist);
  border-radius: 12px;
  padding: 4px;
}
.tabs button {
  border: 0;
  background: transparent;
  height: 36px;
  border-radius: 10px;
  font-weight: 700;
}
.tabs .on {
  background: #fff;
  color: var(--luka-red);
}
.error {
  color: var(--luka-red-deep);
}
@media (max-width: 900px) {
  .layout {
    grid-template-columns: 1fr;
  }
}
</style>
