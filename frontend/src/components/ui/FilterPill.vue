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
  border: 1px solid var(--line);
  border-radius: 999px;
  padding: 8px 14px;
  background: #fff;
  color: var(--luka-ink);
  font-weight: 600;
  transition: background 0.2s, color 0.2s, border-color 0.2s, transform 0.2s var(--ease);
}
.pill.selected {
  background: var(--luka-red);
  color: #fff;
  border-color: var(--luka-red);
}
.pill:hover {
  transform: translateY(-1px);
}
</style>
