<script setup lang="ts">
defineProps<{
  title: string
  body: string
}>()
defineEmits<{ unlock: [] }>()
</script>

<template>
  <article class="lock">
    <div class="icon" aria-hidden="true">🔒</div>
    <h3>{{ title }}</h3>
    <p>{{ body }}</p>
    <button type="button" @click="$emit('unlock')">Débloquer</button>
  </article>
</template>

<style scoped>
.lock {
  background: linear-gradient(180deg, #fff, var(--luka-mist));
  border-radius: 18px;
  padding: 16px;
  text-align: left;
}
h3 {
  margin: 8px 0 4px;
}
p {
  margin: 0 0 12px;
  color: var(--luka-muted);
}
button {
  border: 0;
  background: transparent;
  color: var(--luka-red);
  font-weight: 700;
  padding: 0;
}
</style>
