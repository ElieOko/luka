<script setup lang="ts">
import { onMounted } from 'vue'
import { useSessionStore } from '@/stores/session'

const session = useSessionStore()

onMounted(() => {
  session.restoreToken()
  if (session.isAuthenticated) void session.hydrateRemoteProfile()
})
</script>

<template>
  <RouterView v-slot="{ Component, route }">
    <Transition name="page" mode="out-in">
      <component :is="Component" :key="route.path" />
    </Transition>
  </RouterView>
</template>
