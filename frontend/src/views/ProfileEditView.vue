<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import LukaButton from '@/components/ui/LukaButton.vue'
import PageHeader from '@/components/ui/PageHeader.vue'
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
  <div>
    <PageHeader title="Modifier le compte" />
    <div class="form">
    <label class="field">Nom<input v-model="displayName" /></label>
    <label class="field">E-mail<input v-model="email" type="email" /></label>
    <label class="field">Bio<textarea v-model="bio" rows="4" /></label>
    <label class="field">
      Ville
      <select v-model="cityName">
        <option value="">Choisir</option>
        <option v-for="city in catalog.cities" :key="city.id" :value="city.name">{{ city.name }}</option>
      </select>
    </label>
    <p v-if="error" class="error">{{ error }}</p>
    <LukaButton :loading="loading" @click="save">Enregistrer</LukaButton>
    </div>
  </div>
</template>

<style scoped>
.form {
  max-width: 520px;
  display: grid;
  gap: 14px;
  background: #fff;
  border: 1px solid var(--line);
  border-radius: var(--radius);
  padding: 24px;
  box-shadow: var(--shadow);
}
.error {
  color: var(--luka-red-deep);
}
</style>
