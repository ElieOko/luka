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
  <main class="setup">
    <header>
      <h1>Ton métier.</h1>
      <p>Un seul métier à la fois. Coche la ligne, pas le titre de section.</p>
      <input v-model="query" placeholder="Électricité, data, mines…" />
      <div v-if="selected" class="picked">
        <strong>{{ selected.title }}</strong>
        <span>{{ selected.family }}</span>
      </div>
    </header>
    <div class="list">
      <section v-for="[family, professions] in grouped" :key="family">
        <h2>{{ family }}</h2>
        <button
          v-for="chip in professions"
          :key="tradeKey(chip)"
          type="button"
          class="row"
          :class="{ on: selected && tradeKey(selected) === tradeKey(chip) }"
          @click="selected = chip"
        >
          <span class="radio" />
          <span>
            <strong>{{ chip.title }}</strong>
            <small>{{ chip.tagline }}</small>
          </span>
        </button>
      </section>
    </div>
    <footer>
      <LukaButton :disabled="!selected || saving" :loading="saving" @click="confirm">Continuer</LukaButton>
    </footer>
  </main>
</template>

<style scoped>
.setup {
  min-height: 100dvh;
  display: flex;
  flex-direction: column;
  background: var(--luka-cream);
}
header,
footer {
  padding: 24px;
  max-width: 720px;
  width: 100%;
  margin: 0 auto;
}
h1 {
  margin: 0 0 6px;
  font-size: 30px;
  font-weight: 800;
}
p {
  color: var(--luka-muted);
  margin-top: 0;
}
input {
  width: 100%;
  height: 56px;
  border-radius: 18px;
  border: 1px solid var(--luka-outline);
  padding: 0 16px;
  font-size: 16px;
}
.picked {
  margin-top: 12px;
  background: var(--luka-mist);
  border-radius: 18px;
  padding: 14px;
  display: grid;
}
.picked span {
  color: var(--luka-muted);
}
.list {
  flex: 1;
  overflow: auto;
  padding: 0 20px 16px;
  max-width: 720px;
  width: 100%;
  margin: 0 auto;
}
h2 {
  color: var(--luka-red);
  font-size: 13px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  margin: 14px 0 6px;
}
.row {
  width: 100%;
  display: flex;
  gap: 8px;
  align-items: center;
  text-align: left;
  border: 0;
  border-radius: 16px;
  background: var(--luka-mist);
  padding: 10px 12px;
  margin-bottom: 8px;
  transition: background 0.2s, color 0.2s, transform 0.2s var(--ease);
}
.row span {
  display: grid;
}
small {
  color: var(--luka-muted);
}
.radio {
  width: 18px;
  height: 18px;
  border-radius: 50%;
  border: 2px solid var(--luka-red);
  flex-shrink: 0;
}
.row.on {
  background: var(--luka-red);
  color: #fff;
}
.row.on small {
  color: rgba(255, 255, 255, 0.88);
}
.row.on .radio {
  border-color: #fff;
  box-shadow: inset 0 0 0 4px #fff;
  background: var(--luka-red);
}
</style>
