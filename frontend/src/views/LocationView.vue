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
  <section class="panel">
    <h1>Où travailles-tu ?</h1>
    <p>Luka ne propose pour l’instant que des offres en RDC.</p>
    <div class="country">
      <strong>{{ RDC.flag }} {{ RDC.name }}</strong>
      <span>Pays verrouillé — d’autres arriveront plus tard.</span>
    </div>
    <h2>Ville</h2>
    <div class="pills">
      <FilterPill
        v-for="city in catalog.cities"
        :key="city.id"
        :label="city.name"
        :selected="selected?.name === city.name"
        @click="selected = city"
      />
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
  gap: 16px;
}
h1 {
  margin: 0;
  letter-spacing: -0.03em;
}
p,
.country span {
  color: var(--luka-muted);
  margin: 0;
}
.country {
  background: var(--canvas);
  border-radius: 12px;
  padding: 14px;
  display: grid;
  gap: 4px;
}
h2 {
  margin: 0;
  font-size: 15px;
}
.pills {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
</style>
