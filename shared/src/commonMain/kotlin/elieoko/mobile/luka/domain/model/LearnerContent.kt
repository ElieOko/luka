package elieoko.mobile.luka.domain.model

data class CareerAdvice(
    val id: String,
    val title: String,
    val body: String,
)

object LearnerContent {
    val mitNews = listOf(
        NewsItem(
            id = "mit-ai-work",
            title = "L’IA change déjà les métiers techniques",
            excerpt = "Les labos du MIT suivent comment l’apprentissage automatique redessine le travail : ceux qui savent dialoguer avec les outils gagnent du terrain.",
            source = "MIT News",
            imageName = "profession_software",
            publishedAtEpochMs = 1_746_400_000_000,
            url = "https://news.mit.edu/topic/artificial-intelligence2",
        ),
        NewsItem(
            id = "mit-energy",
            title = "Énergie propre : les pistes que le MIT priorise",
            excerpt = "Stockage, réseaux et solaire : les équipes du MIT Energy Initiative publient les leviers concrets pour les pays qui électrifient vite.",
            source = "MIT News",
            imageName = "profession_other",
            publishedAtEpochMs = 1_745_500_000_000,
            url = "https://news.mit.edu/topic/energy",
        ),
        NewsItem(
            id = "mit-education",
            title = "Apprendre autrement : ce que retient Open Learning",
            excerpt = "Cours ouverts, labs à distance, compétences plutôt que diplômes seuls : le MIT documente ce qui marche pour les étudiants hors campus.",
            source = "MIT News",
            imageName = "profession_other",
            publishedAtEpochMs = 1_744_700_000_000,
            url = "https://news.mit.edu/topic/education",
        ),
        NewsItem(
            id = "mit-entrepreneur",
            title = "De l’idée au prototype : la méthode Martin Trust Center",
            excerpt = "Le centre d’entrepreneuriat du MIT insiste sur un cycle court : problème réel, client parlé, version minimale, puis itération.",
            source = "MIT News",
            imageName = "profession_management",
            publishedAtEpochMs = 1_743_900_000_000,
            url = "https://news.mit.edu/topic/entrepreneurship",
        ),
    )

    val advice = listOf(
        CareerAdvice(
            "adv-1",
            "Une compétence visible par semaine",
            "Choisis un geste concret (un tableau Excel, un schéma électrique, un commit) et montre-le. Les recruteurs croient ce qu’ils voient.",
        ),
        CareerAdvice(
            "adv-2",
            "Lis une offre comme un cahier des charges",
            "Surligne les 5 mots répétés. Ton CV et ta lettre doivent les reprendre, dans le même langage que l’employeur.",
        ),
        CareerAdvice(
            "adv-3",
            "Réseau local d’abord",
            "Un message clair à 3 personnes de ton quartier, église, campus ou chantier vaut mieux que 40 candidatures anonymes.",
        ),
        CareerAdvice(
            "adv-4",
            "Le français écrit compte",
            "Une phrase nette, sans faute, ouvre plus de portes à Kinshasa qu’un jargon copié. Relis à voix haute avant d’envoyer.",
        ),
        CareerAdvice(
            "adv-5",
            "Suis un métier, pas une rumeur",
            "Regarde les tendances Luka : si un domaine monte 3 semaines de suite, c’est un signal. Ajuste ta formation là-dessus.",
        ),
    )
}
