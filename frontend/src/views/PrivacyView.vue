<script setup lang="ts">
import MarketingNav from '@/components/layout/MarketingNav.vue'
import SiteFooter from '@/components/layout/SiteFooter.vue'

const blocks = [
  ['Qui nous sommes', 'Luka est une plateforme d’orientation et d’emploi centrée sur la République démocratique du Congo. L’éditeur traite tes données pour te connecter aux offres, aux analyses de marché et aux conseils de carrière.'],
  ['Données collectées', 'Nous enregistrons le numéro de téléphone (authentification par SMS), le prénom ou nom que tu indiques, la ville, le métier choisi, le type de compte (apprenant ou professionnel), un CV si tu le déposes, et les événements techniques nécessaires au fonctionnement (jetons, serial appareil, journaux d’erreur).'],
  ['Pourquoi', 'Créer et sécuriser ton compte, personnaliser les offres et les tendances, analyser un CV ou des offres si tu es professionnel abonné, envoyer des codes OTP, et améliorer le service. Nous ne vendons pas tes données.'],
  ['Partage', 'Les SMS OTP passent par notre serveur (Casanayo). Le paiement d’abonnement, si tu en prends un, transite par le prestataire de paiement. Les crashs peuvent être envoyés à un outil de diagnostic. Aucun partenaire n’utilise tes données pour de la pub tierce.'],
  ['Conservation', 'Le compte reste tant que tu l’utilises. Tu peux te déconnecter à tout moment. Pour une suppression définitive, contacte le support Luka ; nous effaçons alors le profil local et demandons l’effacement côté serveur.'],
  ['Tes droits', 'Tu peux consulter et modifier nom, e-mail, ville et CV dans l’espace compte. Tu peux refuser l’abonnement. L’usage de Luka implique d’avoir 16 ans ou plus, ou l’accord d’un responsable.'],
  ['Contact', 'Questions : support Luka, via le profil ou le canal officiel de l’éditeur.'],
]
</script>

<template>
  <div>
    <MarketingNav solid />
    <main class="legal">
      <h1>Politique de confidentialité</h1>
      <p class="muted">Dernière mise à jour : septembre 2026</p>
      <section v-for="[title, body] in blocks" :key="title">
        <h2>{{ title }}</h2>
        <p>{{ body }}</p>
      </section>
    </main>
    <SiteFooter />
  </div>
</template>

<style scoped>
.legal {
  max-width: 760px;
  margin: 0 auto;
  padding: 48px 24px;
}
h1 {
  letter-spacing: -0.03em;
  margin-bottom: 4px;
}
.muted {
  color: var(--luka-muted);
}
h2 {
  margin: 28px 0 8px;
}
p {
  line-height: 1.6;
}
</style>
