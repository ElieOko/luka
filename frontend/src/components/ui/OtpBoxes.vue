<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  modelValue: string
}>()
const emit = defineEmits<{ 'update:modelValue': [value: string] }>()

const digits = computed(() => {
  const value = props.modelValue.replace(/\D/g, '').slice(0, 6)
  return Array.from({ length: 6 }, (_, i) => value[i] ?? '')
})

function onInput(event: Event) {
  const value = (event.target as HTMLInputElement).value.replace(/\D/g, '').slice(0, 6)
  emit('update:modelValue', value)
}
</script>

<template>
  <label class="otp">
    <span class="sr-only">Code à 6 chiffres</span>
    <input
      :value="modelValue"
      inputmode="numeric"
      autocomplete="one-time-code"
      maxlength="6"
      @input="onInput"
    />
    <div class="boxes" aria-hidden="true">
      <span v-for="(digit, i) in digits" :key="i" :class="{ filled: digit, active: modelValue.length === i }">
        {{ digit }}
      </span>
    </div>
  </label>
</template>

<style scoped>
.otp {
  display: block;
  position: relative;
}
input {
  position: absolute;
  inset: 0;
  opacity: 0;
  letter-spacing: 2em;
}
.boxes {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 8px;
}
.boxes span {
  height: 56px;
  border-radius: 16px;
  background: var(--luka-mist);
  display: grid;
  place-items: center;
  font-size: 22px;
  font-weight: 700;
  border: 2px solid transparent;
  transition: border-color 0.2s, transform 0.2s var(--ease);
}
.boxes span.active,
.boxes span.filled {
  border-color: var(--luka-red);
}
.boxes span.active {
  transform: translateY(-2px);
}
</style>
