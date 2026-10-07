<script setup lang="ts">
defineProps<{
  title: string
  body: string
}>()
defineEmits<{ unlock: [] }>()
</script>

<template>
  <article class="lock">
    <div>
      <h3>{{ title }}</h3>
      <p>{{ body }}</p>
    </div>
    <button type="button" @click="$emit('unlock')">Débloquer</button>
  </article>
</template>

<style scoped>
.lock {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  align-items: center;
  background: #fff;
  border: 1px dashed var(--luka-red);
  border-radius: var(--radius);
  padding: 16px 18px;
}
h3 {
  margin: 0 0 4px;
}
p {
  margin: 0;
  color: var(--luka-muted);
}
button {
  border: 0;
  background: var(--luka-red);
  color: #fff;
  border-radius: 999px;
  height: 40px;
  padding: 0 16px;
  font-weight: 700;
  white-space: nowrap;
}
@media (max-width: 640px) {
  .lock {
    flex-direction: column;
    align-items: stretch;
  }
}
</style>
