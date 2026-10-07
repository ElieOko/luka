import type { CareerAdvice, NewsItem, OrientationPersona, PlatformAd } from '@/domain/models'

export const ADVICE: CareerAdvice[] = [
  {
    id: 'adv-1',
    title: 'Une compétence visible par semaine',
    body: 'Choisis un geste concret (un tableau Excel, un schéma électrique, un commit) et montre-le. Les recruteurs croient ce qu’ils voient.',
  },
  {
    id: 'adv-2',
    title: 'Lis une offre comme un cahier des charges',
    body: 'Surligne les 5 mots répétés. Ton CV et ta lettre doivent les reprendre, dans le même langage que l’employeur.',
  },
  {
    id: 'adv-3',
    title: 'Réseau local d’abord',
    body: 'Un message clair à 3 personnes de ton quartier, église, campus ou chantier vaut mieux que 40 candidatures anonymes.',
  },
  {
    id: 'adv-4',
    title: 'Le français écrit compte',
    body: 'Une phrase nette, sans faute, ouvre plus de portes à Kinshasa qu’un jargon copié. Relis à voix haute avant d’envoyer.',
  },
  {
    id: 'adv-5',
    title: 'Suis un métier, pas une rumeur',
    body: 'Regarde les tendances Luka : si un domaine monte 3 semaines de suite, c’est un signal. Ajuste ta formation là-dessus.',
  },
]

function article(
  partial: Omit<NewsItem, 'source'> & { source?: string },
): NewsItem {
  return {
    ...partial,
    source: partial.source ?? (partial.subscribed ? 'Luka Souscrit' : 'Luka News'),
  }
}

export const NEWS: NewsItem[] = [
  article({
    id: 'ia-outils-metier',
    domain: 'IA',
    title: 'L’IA change déjà les gestes techniques',
    excerpt: 'Ceux qui savent dialoguer avec un outil d’aide gagnent du temps. Ce n’est pas magique : c’est une nouvelle compétence, comme Excel l’a été.',
    author: 'Rédaction Luka',
    imageName: 'profession_software',
    publishedAtEpochMs: 1_789_430_400_000,
    subscribed: false,
    body: 'À Kinshasa comme ailleurs, un dessinateur, un développeur ou un comptable n’est plus seul face à la page blanche. Les assistants d’écriture et de calcul proposent un premier jet. Le métier, lui, reste de juger si le jet tient.\n\nLes labos du MIT documentent depuis des mois ce déplacement : ce n’est pas « l’IA qui prend le poste », c’est le poste qui exige un dialogue avec la machine. Celui qui ne sait pas formuler une consigne claire perd du temps. Celui qui relit, corrige et signe gagne.\n\nPour un apprenant en RDC, le geste concret est simple. Choisis un outil gratuit. Pose-lui le vrai problème de ton cours ou de ton stage — pas une question vague. Compare la réponse avec un manuel, un aîné, un professeur. Garde ce qui est juste, jette le reste.',
  }),
  article({
    id: 'ia-prompt-utile',
    domain: 'IA',
    title: 'Comment poser une question utile à un outil',
    excerpt: 'Une consigne floue donne une réponse floue. Le métier s’apprend en décrivant le contexte, la contrainte et le livrable.',
    author: 'Grace Mwamba',
    imageName: 'profession_software',
    publishedAtEpochMs: 1_789_257_600_000,
    subscribed: false,
    body: '« Écris-moi un CV » ne mène nulle part. « CV d’électricien industriel à Lubumbashi, 3 ans de chantier, visé un poste SNEL, une page, français simple » donne quelque chose qu’on peut corriger.\n\nTrois morceaux suffisent. Le contexte : qui tu es, où tu es. La contrainte : longueur, langue, public. Le livrable : tableau, lettre, schéma, code.\n\nEnsuite tu relis à voix haute. Si une phrase sonne fausse à Kinshasa, elle est fausse. L’outil ne connaît pas ton quartier, ton école, ton chef de chantier. Toi, si.',
  }),
  article({
    id: 'energie-priorites',
    domain: 'Énergie',
    title: 'Énergie propre : ce que les labos priorisent',
    excerpt: 'Stockage, réseaux et solaire : les leviers concrets pour un pays qui électrifie vite, sans attendre un réseau parfait.',
    author: 'Patrick Ilunga',
    imageName: 'profession_other',
    publishedAtEpochMs: 1_789_084_800_000,
    subscribed: false,
    body: 'L’électrification en RDC ne ressemble pas à celle d’un pays déjà maillé. Les coupures font partie du quotidien. Les équipes du MIT Energy Initiative insistent sur trois leviers : produire près de la demande, stocker un peu, et réparer le réseau existant avant d’en rêver un autre.\n\nLe solaire individuel et mini-réseau n’est plus un gadget d’ONG. C’est un métier : pose, maintenance, batterie, client. Un technicien qui sait diagnostiquer un onduleur vaut plus qu’un discours sur « le climat ».',
  }),
  article({
    id: 'energie-metiers-rdc',
    domain: 'Énergie',
    title: 'Les métiers qui allument vraiment le pays',
    excerpt: 'Moins de titres ronflants, plus de gens capables d’intervenir sur un poste, un groupe, un toit.',
    author: 'Rédaction Luka',
    imageName: 'profession_other',
    publishedAtEpochMs: 1_788_912_000_000,
    subscribed: false,
    body: 'Un titre d’ingénieur sans terrain pèse peu face à un technicien qui a déjà changé un disjoncteur sous la pluie. Les employeurs le disent sans détour.\n\nTrois familles recrutent : électricité bâtiment et industriel, maintenance de groupes, et solaire décentralisé. Chacune a un geste visible. Un schéma unifilaire. Un carnet d’interventions. Une photo datée d’une installation (avec accord).',
  }),
  article({
    id: 'formation-ouverte',
    domain: 'Formation',
    title: 'Apprendre autrement, hors campus',
    excerpt: 'Cours ouverts, labs à distance, compétences plutôt que diplômes seuls : ce qui marche quand on n’est pas à Boston.',
    author: 'Aimee Kabila',
    imageName: 'onboarding_learn',
    publishedAtEpochMs: 1_788_739_200_000,
    subscribed: false,
    body: 'Open Learning au MIT a documenté une évidence : on retient ce qu’on fabrique. Un cours regardé passivement glisse. Un mini-projet, même imparfait, reste.\n\nEn RDC, le campus n’est pas le seul lieu. Une connexion instable, un cybercafé, un téléphone : ça suffit pour un module court, pas pour dix heures de streaming. Découpe. Télécharge. Relis hors-ligne.',
  }),
  article({
    id: 'formation-francais',
    domain: 'Formation',
    title: 'Le français écrit, compétence invisible',
    excerpt: 'Une phrase nette, sans faute, ouvre plus de portes à Kinshasa qu’un jargon copié-collé.',
    author: 'Rédaction Luka',
    imageName: 'profession_other',
    publishedAtEpochMs: 1_788_566_400_000,
    subscribed: false,
    body: 'Les recruteurs jettent une lettre en trois secondes. Une faute sur le nom de l’entreprise, un « je me permets de » trop long, et c’est fini.\n\nRelis à voix haute. Coupe les adverbes. Garde une idée par phrase. Le français de bureau n’est pas le français des réseaux. Les deux ont leur place, pas dans le même message.',
  }),
  article({
    id: 'entrepreneur-cycle-court',
    domain: 'Entrepreneuriat',
    title: 'De l’idée au prototype, cycle court',
    excerpt: 'Problème réel, client parlé, version minimale, puis on itère. La méthode n’a pas besoin d’un campus américain.',
    author: 'Jean Kasongo',
    imageName: 'profession_management',
    publishedAtEpochMs: 1_788_393_600_000,
    subscribed: false,
    body: 'Le Martin Trust Center répète un cycle : un problème vécu par quelqu’un de précis, une conversation avec ce quelqu’un, une version assez moche pour tester, puis on recommence.\n\nÀ Kinshasa, le client est dans la rue, l’église, le marché. Pas dans un slide. Si tu ne peux pas citer trois personnes qui ont le problème, tu n’as pas encore d’idée — tu as un souhait.',
  }),
  article({
    id: 'marche-lire-offre',
    domain: 'Marché',
    title: 'Lis une offre comme un cahier des charges',
    excerpt: 'Les cinq mots répétés sont le vrai poste. Ton CV doit les parler, dans le même langage.',
    author: 'Rédaction Luka',
    imageName: 'profession_hr',
    publishedAtEpochMs: 1_788_048_000_000,
    subscribed: false,
    body: 'Une offre n’est pas un poème. C’est une liste de risques que l’employeur veut couvrir. S’il répète « terrain », « Excel », « français », il dit ce qu’il va tester dès le premier jour.\n\nImprime ou copie le texte. Surligne. Si tu ne peux pas coller trois preuves (stage, cours, chantier) sur ces mots, ne candidate pas encore : forme-toi une semaine sur le trou le plus gros.',
  }),
  article({
    id: 'souscrit-brief-kin',
    domain: 'Souscrit',
    title: 'Brief de la semaine : où ça recrute à Kin et au Katanga',
    excerpt: 'Les familles de métiers qui bougent vraiment, hors rumeurs de groupes. Réservé aux comptes abonnés.',
    author: 'Cellule marché Luka',
    imageName: 'profession_finance',
    publishedAtEpochMs: 1_789_430_400_000,
    subscribed: true,
    body: 'Cette semaine, les ouvertures se concentrent sur trois grappes : support informatique et réseaux, électricité industrielle, et commercial sédentaire avec un vrai suivi client. Ce n’est pas « le digital » en général. Ce sont des intitulés précis.\n\nKinshasa reste le volume. Lubumbashi et Kolwezi pèsent dès qu’on parle mines, maintenance et HSE. Goma recrute plus court, souvent des missions.',
  }),
  article({
    id: 'souscrit-contrats',
    domain: 'Souscrit',
    title: 'Contrats, durées, pièges : ce que les offres cachent',
    excerpt: 'CDD renouvelable, prestataire, stage déguisé. Comment lire la ligne « type de contrat » sans se faire avoir.',
    author: 'Cellule marché Luka',
    imageName: 'profession_hr',
    publishedAtEpochMs: 1_789_171_200_000,
    subscribed: true,
    body: '« 3 mois renouvelables » n’est pas un CDI. C’est un test. Tu peux l’accepter si tu apprends un geste rare. Tu le refuses si on te promet la même chose depuis un an.\n\nLes mots « prestataire », « consultant », « journalier » déplacent le risque vers toi : pas de fiche, parfois pas de transport. Demande le lieu exact, l’heure, qui paie le taxi.',
  }),
  article({
    id: 'souscrit-pipeline',
    domain: 'Souscrit',
    title: 'Stages et pipelines : comment un campus devient un premier job',
    excerpt: 'Les entreprises qui reprennent les stagiaires le disent rarement en public. Voici comment les repérer.',
    author: 'Cellule orientation Luka',
    imageName: 'profession_management',
    publishedAtEpochMs: 1_788_912_000_000,
    subscribed: true,
    body: 'Un bon stage a un livrable. Un rapport, un schéma, une base. Si au bout de deux semaines tu n’as toujours que du café, change de question : « Quel fichier dois-je rendre vendredi ? »\n\nLes pipelines se voient dans les offres : la même enseigne qui publie stagiaire, puis junior, puis confirmé. Suis cette enseigne dans Luka.',
  }),
  article({
    id: 'souscrit-filtre-rh',
    domain: 'Souscrit',
    title: 'Ce que les RH filtrent en trente secondes',
    excerpt: 'Ville, téléphone, métier, une preuve. Le reste est du bruit. Version abonnés, avec exemples congolais.',
    author: 'Cellule orientation Luka',
    imageName: 'profession_hr',
    publishedAtEpochMs: 1_788_652_800_000,
    subscribed: true,
    body: 'Trente secondes. C’est le temps d’un premier tri sur téléphone. Ils cherchent : es-tu dans la ville de l’offre, joignable, et aligné sur le métier nommé — pas « ouvert à toute opportunité ».\n\nUne photo de profil soignée aide. Un CV de quinze pages nuit. Un PDF nommé `CV_Nom_Metier_Ville.pdf` passe mieux que `document(1).pdf`.',
  }),
]

export const PUBLIC_NEWS = NEWS.filter((item) => !item.subscribed)
export const SUBSCRIBED_NEWS = NEWS.filter((item) => item.subscribed)

export const PERSONAS: OrientationPersona[] = [
  {
    id: 'eleve',
    title: 'Élève',
    subtitle: 'Je suis au secondaire',
    lens: 'études',
    resultTitle: 'Au niveau national, voici les études supérieures les plus demandées',
  },
  {
    id: 'etudiant',
    title: 'Étudiant',
    subtitle: 'Je cherche un premier emploi',
    lens: 'emploi',
    resultTitle: 'Au niveau national, voici ce que les chiffres disent de l’emploi après les études',
  },
  {
    id: 'employe',
    title: 'Employé',
    subtitle: 'Je veux un autre emploi',
    lens: 'reconversion',
    resultTitle: 'Au niveau national, voici les autres emplois vers lesquels les salariés basculent',
  },
  {
    id: 'employeur',
    title: 'Employeur',
    subtitle: 'Je cherche où investir',
    lens: 'investissement',
    resultTitle: 'Au niveau national, voici les domaines où investir en RDC',
  },
]

export const PARTNER_ADS: PlatformAd[] = [
  {
    id: 'ad-news',
    title: 'Luka News',
    subtitle: 'Les briefs du marché, lus ici, pas sur WhatsApp.',
    cta: 'Lire',
    imageName: 'ad_news',
  },
  {
    id: 'ad-platform',
    title: 'L’emploi vient à toi',
    subtitle: 'Banques, telcos, mines — Luka scrute la RDC.',
    cta: 'Découvrir',
    imageName: 'ad_platform',
  },
  {
    id: 'ad-career',
    title: 'Un métier, un radar',
    subtitle: 'Choisis un métier. Luka analyse sans s’arrêter.',
    cta: 'Commencer',
    imageName: 'hero_career',
  },
]

export function formatNewsDate(epochMs: number): string {
  return new Intl.DateTimeFormat('fr-FR', { day: 'numeric', month: 'long', year: 'numeric' }).format(
    new Date(epochMs),
  )
}

export function newsTabs(items: NewsItem[]): string[] {
  const domains = [...new Set(items.filter((item) => !item.subscribed).map((item) => item.domain))]
  return ['Favoris', ...domains, 'Souscrit']
}
