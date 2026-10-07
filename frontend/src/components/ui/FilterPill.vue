<script setup lang="ts">
defineProps<{
  label: string
  selected?: boolean
}>()
</script>

<template>
  <button class="pill" type="button" :class="{ selected }">
    {{ label }}
  </button>
</template>

<style scoped>
.pill {
  border: 0;
  border-radius: 20px;
  padding: 8px 14px;
  background: var(--luka-mist);
  color: var(--luka-ink);
  font-weight: 600;
  transition: background 0.2s, color 0.2s, transform 0.2s var(--ease);
}
.pill.selected {
  background: var(--luka-red);
  color: #fff;
}
.pill:hover {
  transform: translateY(-1px);
}
</style>
