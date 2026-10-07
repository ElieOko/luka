<template>
  <div class="radar" aria-hidden="true">
    <span class="ring" />
    <span class="ring delay" />
    <span class="ring later" />
    <span class="ring last" />
    <span class="core" />
  </div>
</template>

<style scoped>
.radar {
  position: relative;
  width: 260px;
  height: 260px;
  margin: 0 auto;
}
.ring,
.core {
  position: absolute;
  inset: 50%;
  border-radius: 50%;
}
.ring {
  width: 40px;
  height: 40px;
  margin: -20px;
  border: 2px solid rgba(227, 27, 35, 0.45);
  animation: pulse-ring 1.8s linear infinite;
}
.delay {
  animation-delay: 0.35s;
}
.later {
  animation-delay: 0.7s;
}
.last {
  animation-delay: 1.05s;
}
.core {
  width: 18px;
  height: 18px;
  margin: -9px;
  background: var(--luka-red);
  box-shadow: 0 0 0 8px rgba(227, 27, 35, 0.18);
  animation: float-y 1.6s ease-in-out infinite;
}
</style>
