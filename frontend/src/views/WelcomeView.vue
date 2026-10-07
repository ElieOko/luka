<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import LukaLogo from '@/components/brand/LukaLogo.vue'
import LukaButton from '@/components/ui/LukaButton.vue'
import { useSessionStore } from '@/stores/session'

const pages = [
  {
    image: '/images/onboarding_kinshasa_1.jpg',
    title: 'L’emploi vient à toi.',
    body: 'Kinshasa, le boulevard, les taxis jaunes — Luka trouve les offres pour des millions de jeunes qui ne savent pas où chercher.',
  },
  {
    image: '/images/onboarding_kinshasa_2.jpg',
    title: 'Les sièges recrutent.',
    body: 'Banques, telcos, mines : glisse, choisis un métier, Luka analyse la RDC pour toi.',
  },
  {
    image: '/images/onboarding_kinshasa_3.jpg',
    title: 'La RDC d’abord.',
    body: 'Kinshasa, Lubumbashi, Goma… les liens d’offres arrivent. L’opportunité vient à toi.',
  },
]

const index = ref(0)
const router = useRouter()
const session = useSessionStore()
let timer: number | undefined

const current = computed(() => pages[index.value]!)
const last = computed(() => index.value === pages.length - 1)

function next() {
  if (last.value) {
    session.consumeWelcome()
    void router.push('/auth')
  } else {
    index.value += 1
  }
}

onMounted(() => {
  timer = window.setInterval(() => {
    index.value = (index.value + 1) % pages.length
  }, 5600)
})
onBeforeUnmount(() => {
  if (timer) window.clearInterval(timer)
})
</script>

<template>
  <section class="welcome">
    <Transition name="fade" mode="out-in">
      <img :key="current.image" :src="current.image" :alt="current.title" class="hero" />
    </Transition>
    <div class="gradient" />
    <div class="inner">
      <LukaLogo light />
      <div class="spacer" />
      <Transition name="slide" mode="out-in">
        <div :key="current.title" class="copy">
          <h1>{{ current.title }}</h1>
          <p>{{ current.body }}</p>
        </div>
      </Transition>
      <div class="dots">
        <button
          v-for="(page, i) in pages"
          :key="page.title"
          type="button"
          class="dot"
          :class="{ on: i === index }"
          :aria-label="page.title"
          @click="index = i"
        />
      </div>
      <LukaButton @click="next">
        {{ last ? 'Créer mon compte' : 'Continuer' }}
      </LukaButton>
    </div>
  </section>
</template>

<style scoped>
.welcome {
  min-height: 100dvh;
  position: relative;
  overflow: hidden;
  color: #fff;
}
.hero {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  animation: kenburns 18s ease-in-out alternate infinite;
}
.gradient {
  position: absolute;
  inset: 0;
  background: linear-gradient(
    180deg,
    rgba(26, 10, 12, 0.28),
    transparent 28%,
    rgba(74, 7, 16, 0.55) 52%,
    rgba(74, 7, 16, 0.94)
  );
}
.inner {
  position: relative;
  z-index: 1;
  min-height: 100dvh;
  display: flex;
  flex-direction: column;
  padding: 32px 24px 28px;
  max-width: 560px;
  margin: 0 auto;
}
.spacer {
  flex: 1;
}
h1 {
  font-size: clamp(30px, 5vw, 42px);
  line-height: 1.1;
  margin: 0 0 12px;
  font-weight: 800;
}
p {
  margin: 0;
  font-size: 17px;
  line-height: 1.45;
  color: rgba(255, 255, 255, 0.9);
}
.dots {
  display: flex;
  gap: 8px;
  align-items: center;
  margin: 18px 0 20px;
}
.dot {
  width: 8px;
  height: 8px;
  border: 0;
  border-radius: 99px;
  background: rgba(255, 255, 255, 0.45);
  padding: 0;
  transition: width 0.3s var(--ease), background 0.3s;
}
.dot.on {
  width: 22px;
  background: var(--luka-red);
}
</style>
