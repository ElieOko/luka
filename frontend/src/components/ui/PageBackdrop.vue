<script setup lang="ts">
defineProps<{
  image: string
  cinematic?: boolean
}>()
</script>

<template>
  <div class="backdrop">
    <img :src="image" alt="" class="photo" />
    <div class="veil" :class="{ cinematic }" />
    <div class="content">
      <slot />
    </div>
  </div>
</template>

<style scoped>
.backdrop {
  position: relative;
  min-height: 100%;
  overflow: hidden;
}
.photo {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  animation: kenburns 22s ease-in-out alternate infinite;
}
.veil {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(26, 10, 12, 0.18), rgba(74, 7, 16, 0.78) 55%, rgba(20, 6, 8, 0.92));
}
.veil.cinematic {
  background: linear-gradient(180deg, rgba(10, 4, 6, 0.25), rgba(26, 10, 12, 0.55) 40%, rgba(10, 4, 6, 0.88));
}
.content {
  position: relative;
  z-index: 1;
  min-height: 100%;
}
</style>
