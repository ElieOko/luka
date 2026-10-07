<script setup lang="ts">
defineProps<{
  disabled?: boolean
  loading?: boolean
}>()
</script>

<template>
  <button class="btn" type="button" :disabled="disabled || loading">
    <span v-if="loading" class="spinner" aria-hidden="true" />
    <slot />
  </button>
</template>

<style scoped>
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  width: 100%;
  height: 56px;
  border: 0;
  border-radius: 18px;
  background: var(--luka-red);
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  transition: transform 0.2s var(--ease), filter 0.2s var(--ease), opacity 0.2s;
}
.btn:hover:not(:disabled) {
  filter: brightness(1.05);
  transform: translateY(-1px);
}
.btn:active:not(:disabled) {
  transform: translateY(1px) scale(0.99);
}
.btn:disabled {
  opacity: 0.55;
  cursor: not-allowed;
}
.spinner {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.35);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}
@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
</style>
