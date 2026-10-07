<script setup lang="ts">
import { RouterLink } from 'vue-router'
import MarketingNav from '@/components/layout/MarketingNav.vue'
import SiteFooter from '@/components/layout/SiteFooter.vue'
import { PAID_PLANS, STARTER_PLAN } from '@/data/plans'

const mockOffers = [
  { role: 'Analyste risque', company: 'Rawbank', city: 'Kinshasa', contract: 'CDI' },
  { role: 'Ingénieur réseau', company: 'Vodacom', city: 'Lubumbashi', contract: 'CDD' },
  { role: 'Géologue junior', company: 'Minière', city: 'Kolwezi', contract: 'Stage' },
  { role: 'Chargé de programmes', company: 'ONG', city: 'Goma', contract: 'CDI' },
]
</script>

<template>
  <div class="site">
    <MarketingNav />
    <main>
      <section class="hero">
        <div class="copy">
          <p class="kicker">Plateforme emploi · République démocratique du Congo</p>
          <h1>Le marché du travail congolais, lu clairement.</h1>
          <p class="lead">
            Luka est un espace web pour suivre un métier, filtrer les ouvertures Casanayo et
            lire les tendances — Kinshasa, Lubumbashi, Goma. Un compte, un métier, un tableau de bord.
          </p>
          <div class="actions">
            <RouterLink class="primary" to="/auth?signup=1">Créer un compte</RouterLink>
            <RouterLink class="ghost" to="/auth">Se connecter</RouterLink>
          </div>
          <ul class="proof">
            <li>Authentification par SMS</li>
            <li>Catalogue officiel métiers / villes</li>
            <li>Apprenant ou professionnel</li>
          </ul>
        </div>
        <div class="preview" aria-hidden="true">
          <div class="chrome">
            <aside>
              <strong>Luka</strong>
              <span class="on">Tableau de bord</span>
              <span>Offres</span>
              <span>Marché</span>
              <span>Analyses</span>
            </aside>
            <div class="board">
              <header>
                <input disabled placeholder="Rechercher un poste, une entreprise…" />
                <b>Grace</b>
              </header>
              <div class="kpis">
                <article><small>Offres ouvertes</small><strong>128</strong></article>
                <article><small>Métier suivi</small><strong>Finance</strong></article>
                <article><small>Ville</small><strong>Kinshasa</strong></article>
              </div>
              <table>
                <thead>
                  <tr>
                    <th>Poste</th>
                    <th>Entreprise</th>
                    <th>Ville</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="row in mockOffers" :key="row.role">
                    <td>{{ row.role }}</td>
                    <td>{{ row.company }}</td>
                    <td>{{ row.city }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          <img class="city" src="/images/onboarding_kinshasa_1.jpg" alt="" />
        </div>
      </section>

      <section class="stats">
        <div class="inner row">
          <p><strong>1 métier</strong> suivi à la fois</p>
          <p><strong>RDC</strong> d’abord — villes du catalogue</p>
          <p><strong>SMS</strong> sans mot de passe</p>
          <p><strong>API</strong> préférences Casanayo</p>
        </div>
      </section>

      <section id="offres" class="band">
        <div class="inner">
          <header class="sec">
            <h2>Conçu pour le bureau, pas pour un fil mobile.</h2>
            <p>Sidebar, recherche, tableaux et lecture longue — pour travailler depuis un ordinateur.</p>
          </header>
          <div class="grid3">
            <article>
              <h3>Offres ciblées</h3>
              <p>Un métier à la fois. Ville et domaine issus du catalogue officiel, filtrables en colonnes.</p>
            </article>
            <article>
              <h3>News &amp; orientation</h3>
              <p>Pour les apprenants : MIT, filières, conseils — un dossier éditorial, pas un scroll infini.</p>
            </article>
            <article>
              <h3>Analyses pro</h3>
              <p>CV, volume d’ouvertures et marché agrégé pour les comptes professionnels.</p>
            </article>
          </div>
        </div>
      </section>

      <section id="fonctionnement" class="inner steps">
        <header class="sec">
          <h2>Trois étapes</h2>
          <p>Le même parcours que l’app, dans une interface de plateforme.</p>
        </header>
        <ol>
          <li><strong>Compte SMS</strong><span>Numéro congolais, type apprenant ou professionnel.</span></li>
          <li><strong>Métier et ville</strong><span>Un métier. La RDC d’abord. Les préférences partent à l’API.</span></li>
          <li><strong>Tableau de bord</strong><span>Offres, tendances et lectures, filtrables.</span></li>
        </ol>
      </section>

      <section id="tarifs" class="inner pricing">
        <header class="sec">
          <h2>Tarifs</h2>
          <p>Gratuit pour commencer. Étudiant 3&nbsp;$ · Professionnel 5&nbsp;$.</p>
        </header>
        <div class="plans">
          <article>
            <h3>{{ STARTER_PLAN.name }}</h3>
            <p class="price">{{ STARTER_PLAN.priceLabel }}</p>
            <ul>
              <li v-for="perk in STARTER_PLAN.perks" :key="perk">{{ perk }}</li>
            </ul>
          </article>
          <article v-for="plan in PAID_PLANS" :key="plan.id" :class="{ hi: plan.highlight }">
            <p v-if="plan.highlight" class="tag">Recommandé</p>
            <h3>{{ plan.name }}</h3>
            <p class="price">{{ plan.priceLabel }} <small>{{ plan.period }}</small></p>
            <ul>
              <li v-for="perk in plan.perks" :key="perk">{{ perk }}</li>
            </ul>
            <RouterLink to="/auth?signup=1">Commencer</RouterLink>
          </article>
        </div>
      </section>

      <section class="cta-band">
        <div class="inner">
          <h2>Ouvre ton radar depuis le navigateur.</h2>
          <p>Crée un compte, choisis un métier, et Luka charge les ouvertures Casanayo.</p>
          <RouterLink class="primary" to="/auth?signup=1">Créer un compte</RouterLink>
        </div>
      </section>
    </main>
    <SiteFooter />
  </div>
</template>

<style scoped>
.site {
  background: var(--canvas);
  overflow-x: clip;
}
.hero {
  max-width: var(--content);
  margin: 0 auto;
  padding: 48px 28px 64px;
  display: grid;
  grid-template-columns: 0.92fr 1.08fr;
  gap: 48px;
  align-items: center;
}
.kicker {
  color: var(--luka-red);
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  font-size: 12px;
  margin: 0 0 12px;
}
h1 {
  font-size: clamp(36px, 4.6vw, 54px);
  line-height: 1.05;
  letter-spacing: -0.04em;
  margin: 0 0 16px;
  font-weight: 800;
}
.lead {
  font-size: 18px;
  color: var(--luka-muted);
  line-height: 1.55;
  max-width: 48ch;
}
.actions {
  display: flex;
  gap: 12px;
  margin: 28px 0 18px;
}
.primary,
.ghost {
  text-decoration: none;
  height: 48px;
  padding: 0 20px;
  border-radius: 999px;
  display: inline-flex;
  align-items: center;
  font-weight: 700;
}
.primary {
  background: var(--luka-red);
  color: #fff;
}
.ghost {
  border: 1px solid var(--line);
  background: #fff;
}
.proof {
  display: flex;
  flex-wrap: wrap;
  gap: 8px 18px;
  padding: 0;
  margin: 0;
  list-style: none;
  color: var(--luka-muted);
  font-size: 13px;
}
.preview {
  position: relative;
}
.chrome {
  display: grid;
  grid-template-columns: 132px 1fr;
  background: #fff;
  border: 1px solid var(--line);
  border-radius: 18px;
  box-shadow: var(--shadow);
  min-height: 380px;
  overflow: hidden;
}
.chrome aside {
  background: #faf7f7;
  border-right: 1px solid var(--line);
  padding: 18px 12px;
  display: grid;
  align-content: start;
  gap: 8px;
  font-size: 12px;
  color: var(--luka-muted);
}
.chrome aside strong {
  color: var(--luka-ink);
  font-size: 15px;
  margin-bottom: 8px;
}
.chrome aside .on {
  color: var(--luka-red);
  font-weight: 700;
}
.board {
  padding: 14px 16px 18px;
}
.board header {
  display: flex;
  gap: 10px;
  align-items: center;
  margin-bottom: 12px;
}
.board header input {
  flex: 1;
  height: 32px;
  border-radius: 999px;
  border: 1px solid var(--line);
  padding: 0 12px;
  background: var(--canvas);
  font-size: 12px;
}
.board header b {
  font-size: 12px;
}
.kpis {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
  margin-bottom: 12px;
}
.kpis article {
  border: 1px solid var(--line);
  border-radius: 10px;
  padding: 8px 10px;
  display: grid;
}
.kpis small {
  color: var(--luka-muted);
  font-size: 10px;
}
.kpis strong {
  font-size: 16px;
}
table {
  width: 100%;
  border-collapse: collapse;
  font-size: 12px;
}
th {
  text-align: left;
  color: var(--luka-muted);
  font-weight: 600;
  padding: 6px 0;
  border-bottom: 1px solid var(--line);
}
td {
  padding: 8px 0;
  border-bottom: 1px solid var(--line);
}
.city {
  position: absolute;
  width: 148px;
  height: 108px;
  object-fit: cover;
  border-radius: 14px;
  right: -18px;
  bottom: -22px;
  border: 3px solid #fff;
  box-shadow: var(--shadow);
}
.stats {
  border-block: 1px solid var(--line);
  background: #fff;
}
.row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  padding: 22px 28px;
}
.row p {
  margin: 0;
  color: var(--luka-muted);
  font-size: 14px;
}
.band {
  background: var(--canvas);
}
.inner {
  max-width: var(--content);
  margin: 0 auto;
  padding: 72px 28px;
}
.sec {
  max-width: 52ch;
}
.grid3 {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 18px;
  margin-top: 28px;
}
.grid3 article {
  padding: 24px;
  border: 1px solid var(--line);
  border-radius: var(--radius);
  background: #fff;
}
h2 {
  font-size: 32px;
  letter-spacing: -0.03em;
  margin: 0 0 8px;
}
.sec p,
.grid3 p,
.steps span {
  color: var(--luka-muted);
  margin: 0;
  line-height: 1.5;
}
.steps ol {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 18px;
  padding: 0;
  margin: 28px 0 0;
  list-style: none;
  counter-reset: step;
}
.steps li {
  border: 1px solid var(--line);
  background: #fff;
  border-radius: var(--radius);
  padding: 22px;
  counter-increment: step;
  display: grid;
  gap: 8px;
}
.steps li::before {
  content: counter(step);
  color: var(--luka-red);
  font-weight: 800;
}
.plans {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-top: 28px;
}
.plans article {
  background: #fff;
  border: 1px solid var(--line);
  border-radius: 18px;
  padding: 22px;
  position: relative;
}
.plans .hi {
  border-color: var(--luka-red);
  background: #fff8f8;
}
.tag {
  position: absolute;
  top: 16px;
  right: 16px;
  margin: 0;
  font-size: 11px;
  font-weight: 800;
  color: var(--luka-red);
  letter-spacing: 0.06em;
  text-transform: uppercase;
}
.price {
  font-size: 28px;
  font-weight: 800;
  margin: 8px 0 16px;
}
.price small {
  font-size: 14px;
  color: var(--luka-muted);
  font-weight: 500;
}
.plans ul {
  padding: 0;
  margin: 0 0 18px;
  list-style: none;
  color: var(--luka-muted);
  display: grid;
  gap: 8px;
}
.plans a {
  color: var(--luka-red);
  font-weight: 700;
  text-decoration: none;
}
.cta-band {
  background: var(--luka-wine);
  color: #fff;
}
.cta-band .inner {
  padding: 56px 28px;
  display: grid;
  justify-items: start;
  gap: 10px;
}
.cta-band p {
  margin: 0 0 8px;
  color: rgba(255, 255, 255, 0.72);
}
.cta-band h2 {
  color: #fff;
}
@media (max-width: 960px) {
  .hero,
  .grid3,
  .steps ol,
  .plans,
  .row {
    grid-template-columns: 1fr;
  }
  .chrome {
    grid-template-columns: 1fr;
  }
  .chrome aside {
    display: none;
  }
  .city {
    display: none;
  }
}
</style>
