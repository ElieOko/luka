<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import LukaButton from '@/components/ui/LukaButton.vue'
import { useCatalogStore } from '@/stores/catalog'
import { useSessionStore } from '@/stores/session'

const session = useSessionStore()
const catalog = useCatalogStore()
const router = useRouter()
const displayName = ref(session.profile?.displayName ?? '')
const email = ref(session.profile?.email ?? '')
const bio = ref(session.profile?.bio ?? '')
const cityName = ref(session.profile?.cityName ?? '')
const loading = ref(false)
const error = ref<string | null>(null)

async function save() {
  error.value = null
  if (displayName.value.trim().length < 2) {
    error.value = 'Le nom est trop court.'
    return
  }
  if (email.value && !(email.value.includes('@') && email.value.includes('.'))) {
    error.value = 'E-mail invalide.'
    return
  }
  loading.value = true
  try {
    await session.updateProfile(displayName.value.trim(), bio.value.trim(), email.value.trim(), cityName.value || null)
    router.back()
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Impossible d’enregistrer.'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <main class="edit">
    <header>
      <button type="button" @click="router.back()">←</button>
      <h1>Modifier le profil</h1>
    </header>
    <label>Nom<input v-model="displayName" /></label>
    <label>E-mail<input v-model="email" type="email" /></label>
    <label>Bio<textarea v-model="bio" rows="4" /></label>
    <label>
      Ville
      <select v-model="cityName">
        <option value="">Choisir</option>
        <option v-for="city in catalog.cities" :key="city.id" :value="city.name">{{ city.name }}</option>
      </select>
    </label>
    <p v-if="error" class="error">{{ error }}</p>
    <LukaButton :loading="loading" @click="save">Enregistrer</LukaButton>
  </main>
</template>

<style scoped>
.edit {
  max-width: 560px;
  margin: 0 auto;
  padding: 16px 20px 40px;
  display: grid;
  gap: 12px;
  background: #fff;
  min-height: 100%;
}
header {
  display: flex;
  gap: 8px;
  align-items: center;
}
h1 {
  font-size: 20px;
  margin: 0;
}
button {
  border: 0;
  background: none;
  font-size: 20px;
}
label {
  display: grid;
  gap: 6px;
  font-weight: 600;
  color: var(--luka-muted);
}
input,
textarea,
select {
  height: 52px;
  border-radius: 16px;
  border: 1px solid var(--luka-outline);
  padding: 12px 14px;
  font: inherit;
  color: var(--luka-ink);
}
textarea {
  height: auto;
}
.error {
  color: var(--luka-red-deep);
}
</style>
