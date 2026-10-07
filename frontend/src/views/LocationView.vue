<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import FilterPill from '@/components/ui/FilterPill.vue'
import LukaButton from '@/components/ui/LukaButton.vue'
import { RDC } from '@/data/catalog'
import type { City } from '@/domain/models'
import { useCatalogStore } from '@/stores/catalog'
import { useSessionStore } from '@/stores/session'

const catalog = useCatalogStore()
const session = useSessionStore()
const router = useRouter()
const selected = ref<City | null>(null)
const saving = ref(false)

onMounted(() => {
  void catalog.loadPublicCatalog()
})

async function confirm() {
  if (!selected.value) return
  saving.value = true
  await session.saveLocation(selected.value.regionId, selected.value.name)
  saving.value = false
  await router.replace('/setup/analyse')
}
</script>

<template>
  <main class="setup">
    <div class="body">
      <h1>Où vis-tu ?</h1>
      <p>Pour l’instant, Luka ne propose que des offres en RDC. Choisis ta ville.</p>
      <div class="country">
        <strong>{{ RDC.flag }} {{ RDC.name }}</strong>
        <span>Pays verrouillé — d’autres arriveront plus tard.</span>
      </div>
      <h2>Ta ville</h2>
      <div class="pills">
        <FilterPill
          v-for="city in catalog.cities"
          :key="city.id"
          :label="city.name"
          :selected="selected?.name === city.name"
          @click="selected = city"
        />
      </div>
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
.body,
footer {
  padding: 24px;
  max-width: 720px;
  width: 100%;
  margin: 0 auto;
}
.body {
  flex: 1;
}
h1 {
  font-size: 30px;
  margin: 0 0 8px;
  font-weight: 800;
}
p,
.country span {
  color: var(--luka-muted);
}
.country {
  background: var(--luka-mist);
  border-radius: 20px;
  padding: 16px;
  display: grid;
  gap: 4px;
}
.pills {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
</style>
