<script setup lang="ts">
withDefaults(
  defineProps<{
    disabled?: boolean
    loading?: boolean
    variant?: 'primary' | 'ghost' | 'soft'
    block?: boolean
  }>(),
  { variant: 'primary', block: false },
)
</script>

<template>
  <button class="btn" :class="[variant, { block }]" type="button" :disabled="disabled || loading">
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
  height: 46px;
  padding: 0 18px;
  border: 0;
  border-radius: 999px;
  background: var(--luka-red);
  color: #fff;
  font-size: 15px;
  font-weight: 650;
  transition: transform 0.2s var(--ease), filter 0.2s var(--ease), opacity 0.2s, background 0.2s;
}
.btn.block {
  width: 100%;
  height: 50px;
  border-radius: 12px;
}
.btn.ghost {
  background: transparent;
  color: var(--luka-ink);
  border: 1px solid var(--line);
}
.btn.soft {
  background: var(--luka-mist);
  color: var(--luka-red);
}
.btn:hover:not(:disabled) {
  filter: brightness(1.04);
  transform: translateY(-1px);
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
