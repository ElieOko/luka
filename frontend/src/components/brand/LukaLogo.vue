<script setup lang="ts">
defineProps<{
  light?: boolean
  height?: number
}>()
</script>

<template>
  <svg
    class="luka-logo"
    :height="height ?? 40"
    viewBox="0 0 143 46"
    role="img"
    aria-label="Luka"
  >
    <circle cx="125.6" cy="28.6" r="13.35" fill="#E0301E" />
    <path
      d="M2 1.6 V43 H25.2"
      fill="none"
      :stroke="light ? '#fff' : '#141414'"
      stroke-width="3.15"
      stroke-linejoin="miter"
    />
    <path
      d="M33.15 1.6 V29.1 A13.6 13.6 0 0 0 60.35 29.1 V1.6"
      fill="none"
      :stroke="light ? '#fff' : '#141414'"
      stroke-width="3.15"
    />
    <path
      d="M75.6 1.6 V43 M102 3.2 L78.2 21.6 L104 43"
      fill="none"
      :stroke="light ? '#fff' : '#141414'"
      stroke-width="3.15"
    />
    <path
      d="M123.5 2.4 L107.2 43 H140.8 Z"
      fill="none"
      :stroke="light ? '#fff' : '#141414'"
      stroke-width="3.15"
      stroke-linejoin="miter"
    />
  </svg>
</template>

<style scoped>
.luka-logo {
  display: block;
  width: auto;
}
</style>
