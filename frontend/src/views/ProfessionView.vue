<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import LukaButton from '@/components/ui/LukaButton.vue'
import { tradeKey, type TradeChip } from '@/domain/models'
import { useCatalogStore } from '@/stores/catalog'
import { useSessionStore } from '@/stores/session'

const catalog = useCatalogStore()
const session = useSessionStore()
const router = useRouter()
const query = ref('')
const selected = ref<TradeChip | null>(null)
const saving = ref(false)

const grouped = computed(() => {
  const needle = query.value.trim().toLowerCase()
  const list = catalog.trades.filter(
    (chip) =>
      !needle ||
      chip.title.toLowerCase().includes(needle) ||
      chip.family.toLowerCase().includes(needle) ||
      chip.tagline.toLowerCase().includes(needle),
  )
  const map = new Map<string, TradeChip[]>()
  for (const chip of list) {
    const bucket = map.get(chip.family) ?? []
    bucket.push(chip)
    map.set(chip.family, bucket)
  }
  return [...map.entries()]
})

onMounted(() => {
  void catalog.loadPublicCatalog()
})

async function confirm() {
  if (!selected.value) return
  saving.value = true
  await session.saveProfession(selected.value.profession, selected.value.domainId, selected.value.title)
  saving.value = false
  await router.replace('/setup/ville')
}
</script>

<template>
  <section class="panel">
    <h1>Quel métier suivre ?</h1>
    <p>Un seul à la fois. Le domaine est enregistré dans tes préférences API.</p>
    <input v-model="query" class="input" placeholder="Électricité, data, mines…" />
    <p v-if="selected" class="picked">Sélection : <strong>{{ selected.title }}</strong> · {{ selected.family }}</p>
    <div class="list">
      <section v-for="[family, professions] in grouped" :key="family">
        <h2>{{ family }}</h2>
        <div class="rows">
          <button
            v-for="chip in professions"
            :key="tradeKey(chip)"
            type="button"
            :class="{ on: selected && tradeKey(selected) === tradeKey(chip) }"
            @click="selected = chip"
          >
            <strong>{{ chip.title }}</strong>
            <small>{{ chip.tagline }}</small>
          </button>
        </div>
      </section>
    </div>
    <LukaButton :disabled="!selected || saving" :loading="saving" @click="confirm">Continuer</LukaButton>
  </section>
</template>

<style scoped>
.panel {
  background: #fff;
  border: 1px solid var(--line);
  border-radius: 20px;
  padding: 28px;
  box-shadow: var(--shadow);
  display: grid;
  gap: 14px;
}
h1 {
  margin: 0;
  font-size: 28px;
  letter-spacing: -0.03em;
}
p {
  margin: 0;
  color: var(--luka-muted);
}
.list {
  max-height: 46vh;
  overflow: auto;
  display: grid;
  gap: 16px;
}
h2 {
  margin: 0 0 8px;
  color: var(--luka-red);
  font-size: 12px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}
.rows {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 8px;
}
.rows button {
  text-align: left;
  border: 1px solid var(--line);
  background: #fff;
  border-radius: 12px;
  padding: 12px;
  display: grid;
  gap: 4px;
}
small {
  color: var(--luka-muted);
}
.rows .on {
  border-color: var(--luka-red);
  background: #fff5f5;
}
</style>
