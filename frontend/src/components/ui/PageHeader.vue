<script setup lang="ts">
defineProps<{
  eyebrow?: string
  title: string
  subtitle?: string
}>()
</script>

<template>
  <header class="head">
    <div>
      <p v-if="eyebrow" class="eye">{{ eyebrow }}</p>
      <h1>{{ title }}</h1>
      <p v-if="subtitle" class="sub">{{ subtitle }}</p>
    </div>
    <div v-if="$slots.actions" class="actions">
      <slot name="actions" />
    </div>
  </header>
</template>

<style scoped>
.head {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  align-items: flex-end;
  margin-bottom: 28px;
}
.eye {
  margin: 0 0 6px;
  color: var(--luka-red);
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}
h1 {
  margin: 0;
  font-size: clamp(28px, 3vw, 36px);
  font-weight: 800;
  letter-spacing: -0.03em;
}
.sub {
  margin: 8px 0 0;
  color: var(--luka-muted);
  max-width: 52ch;
}
.actions {
  display: flex;
  gap: 10px;
  flex-shrink: 0;
}
@media (max-width: 720px) {
  .head {
    flex-direction: column;
    align-items: stretch;
  }
}
</style>
