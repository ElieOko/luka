<script setup lang="ts">
import { RouterLink } from 'vue-router'
import LukaLogo from '@/components/brand/LukaLogo.vue'
</script>

<template>
  <footer class="foot">
    <div class="inner">
      <div>
        <LukaLogo :height="28" />
        <p>L’emploi et l’orientation en République démocratique du Congo — conçu comme une plateforme web.</p>
      </div>
      <nav>
        <strong>Produit</strong>
        <a href="/#offres">Offres</a>
        <a href="/#fonctionnement">Fonctionnement</a>
        <a href="/#tarifs">Tarifs</a>
      </nav>
      <nav>
        <strong>Compte</strong>
        <RouterLink to="/auth">Connexion</RouterLink>
        <RouterLink to="/auth?signup=1">Créer un compte</RouterLink>
        <RouterLink to="/confidentialite">Confidentialité</RouterLink>
      </nav>
    </div>
    <small>© 2026 Luka · Casanayo</small>
  </footer>
</template>

<style scoped>
.foot {
  border-top: 1px solid var(--line);
  background: #fff;
  margin-top: 0;
}
.inner {
  max-width: var(--content);
  margin: 0 auto;
  padding: 48px 28px 20px;
  display: grid;
  grid-template-columns: 1.4fr 1fr 1fr;
  gap: 28px;
}
p,
small {
  color: var(--luka-muted);
}
p {
  max-width: 40ch;
  line-height: 1.5;
}
nav {
  display: grid;
  gap: 8px;
  align-content: start;
}
strong {
  font-size: 12px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: #b09a9d;
}
a {
  text-decoration: none;
  font-weight: 600;
}
a:hover {
  color: var(--luka-red);
}
small {
  display: block;
  max-width: var(--content);
  margin: 0 auto;
  padding: 0 28px 28px;
}
@media (max-width: 760px) {
  .inner {
    grid-template-columns: 1fr;
  }
}
</style>
