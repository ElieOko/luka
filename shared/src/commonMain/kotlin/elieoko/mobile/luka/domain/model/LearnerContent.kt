package elieoko.mobile.luka.domain.model

data class CareerAdvice(
    val id: String,
    val title: String,
    val body: String,
)

object LearnerContent {
    val catalog: List<NewsItem> = listOf(
        article(
            id = "ia-outils-metier",
            domain = "IA",
            title = "L’IA change déjà les gestes techniques",
            excerpt = "Ceux qui savent dialoguer avec un outil d’aide gagnent du temps. Ce n’est pas magique : c’est une nouvelle compétence, comme Excel l’a été.",
            author = "Rédaction Luka",
            imageName = "profession_software",
            publishedAtEpochMs = 1_789_430_400_000,
            url = "luka://news/ia-outils-metier",
            body = """
                À Kinshasa comme ailleurs, un dessinateur, un développeur ou un comptable n’est plus seul face à la page blanche. Les assistants d’écriture et de calcul proposent un premier jet. Le métier, lui, reste de juger si le jet tient.

                Les labos du MIT documentent depuis des mois ce déplacement : ce n’est pas « l’IA qui prend le poste », c’est le poste qui exige un dialogue avec la machine. Celui qui ne sait pas formuler une consigne claire perd du temps. Celui qui relit, corrige et signe gagne.

                Pour un apprenant en RDC, le geste concret est simple. Choisis un outil gratuit. Pose-lui le vrai problème de ton cours ou de ton stage — pas une question vague. Compare la réponse avec un manuel, un aîné, un professeur. Garde ce qui est juste, jette le reste.

                Luka te montre les métiers qui bougent. Ajoute l’IA comme un outil de plus, pas comme un diplôme. Les recruteurs croient ce que tu peux montrer : un tableau, un schéma, un petit projet.
            """.trimIndent(),
        ),
        article(
            id = "ia-prompt-utile",
            domain = "IA",
            title = "Comment poser une question utile à un outil",
            excerpt = "Une consigne floue donne une réponse floue. Le métier s’apprend en décrivant le contexte, la contrainte et le livrable.",
            author = "Grace Mwamba",
            imageName = "profession_software",
            publishedAtEpochMs = 1_789_257_600_000,
            url = "luka://news/ia-prompt-utile",
            body = """
                « Écris-moi un CV » ne mène nulle part. « CV d’électricien industriel à Lubumbashi, 3 ans de chantier, visé un poste SNEL, une page, français simple » donne quelque chose qu’on peut corriger.

                Trois morceaux suffisent. Le contexte : qui tu es, où tu es. La contrainte : longueur, langue, public. Le livrable : tableau, lettre, schéma, code.

                Ensuite tu relis à voix haute. Si une phrase sonne fausse à Kinshasa, elle est fausse. L’outil ne connaît pas ton quartier, ton école, ton chef de chantier. Toi, si.

                Enregistre tes meilleures consignes dans un carnet. C’est déjà un portfolio : la preuve que tu sais cadrer un problème.
            """.trimIndent(),
        ),
        article(
            id = "energie-priorites",
            domain = "Énergie",
            title = "Énergie propre : ce que les labos priorisent",
            excerpt = "Stockage, réseaux et solaire : les leviers concrets pour un pays qui électrifie vite, sans attendre un réseau parfait.",
            author = "Patrick Ilunga",
            imageName = "profession_other",
            publishedAtEpochMs = 1_789_084_800_000,
            url = "luka://news/energie-priorites",
            body = """
                L’électrification en RDC ne ressemble pas à celle d’un pays déjà maillé. Les coupures font partie du quotidien. Les équipes du MIT Energy Initiative insistent sur trois leviers : produire près de la demande, stocker un peu, et réparer le réseau existant avant d’en rêver un autre.

                Le solaire individuel et mini-réseau n’est plus un gadget d’ONG. C’est un métier : pose, maintenance, batterie, client. Un technicien qui sait diagnostiquer un onduleur vaut plus qu’un discours sur « le climat ».

                L’hydro reste stratégique. Mais un barrage sans lignes, sans transformateurs, sans équipes de maintenance, n’allume pas les classes. Les métiers d’entretien — électricité, mécanique, HSE — sont le vrai goulot.

                Si tu t’orientes, regarde les offres Luka côté énergie et BTP. Les mots qui reviennent (fibre, poste source, solaire, maintenance) sont le programme de tes soirs.
            """.trimIndent(),
        ),
        article(
            id = "energie-metiers-rdc",
            domain = "Énergie",
            title = "Les métiers qui allument vraiment le pays",
            excerpt = "Moins de titres ronflants, plus de gens capables d’intervenir sur un poste, un groupe, un toit.",
            author = "Rédaction Luka",
            imageName = "profession_other",
            publishedAtEpochMs = 1_788_912_000_000,
            url = "luka://news/energie-metiers-rdc",
            body = """
                Un titre d’ingénieur sans terrain pèse peu face à un technicien qui a déjà changé un disjoncteur sous la pluie. Les employeurs le disent sans détour.

                Trois familles recrutent : électricité bâtiment et industriel, maintenance de groupes, et solaire décentralisé. Chacune a un geste visible. Un schéma unifilaire. Un carnet d’interventions. Une photo datée d’une installation (avec accord).

                La sécurité n’est pas un module optionnel. Un accident sur un chantier coupe la mission et la réputation. HSE, EPI, consignation : apprends-les tôt.

                Luka classe les ouvertures par métier. Si l’électricité monte trois semaines de suite, ce n’est pas une rumeur WhatsApp. C’est un signal.
            """.trimIndent(),
        ),
        article(
            id = "formation-ouverte",
            domain = "Formation",
            title = "Apprendre autrement, hors campus",
            excerpt = "Cours ouverts, labs à distance, compétences plutôt que diplômes seuls : ce qui marche quand on n’est pas à Boston.",
            author = "Aimee Kabila",
            imageName = "profession_other",
            publishedAtEpochMs = 1_788_739_200_000,
            url = "luka://news/formation-ouverte",
            body = """
                Open Learning au MIT a documenté une évidence : on retient ce qu’on fabrique. Un cours regardé passivement glisse. Un mini-projet, même imparfait, reste.

                En RDC, le campus n’est pas le seul lieu. Une connexion instable, un cybercafé, un téléphone : ça suffit pour un module court, pas pour dix heures de streaming. Découpe. Télécharge. Relis hors-ligne.

                Le diplôme ouvre encore des portes dans l’administration et certaines entreprises. À côté, un portfolio — Git, plans, rapports de stage — ouvre les portes du privé pressé.

                Choisis une compétence par mois. Montre-la. Luka sert à voir si le marché la demande encore la semaine suivante.
            """.trimIndent(),
        ),
        article(
            id = "formation-francais",
            domain = "Formation",
            title = "Le français écrit, compétence invisible",
            excerpt = "Une phrase nette, sans faute, ouvre plus de portes à Kinshasa qu’un jargon copié-collé.",
            author = "Rédaction Luka",
            imageName = "profession_other",
            publishedAtEpochMs = 1_788_566_400_000,
            url = "luka://news/formation-francais",
            body = """
                Les recruteurs jettent une lettre en trois secondes. Une faute sur le nom de l’entreprise, un « je me permets de » trop long, et c’est fini.

                Relis à voix haute. Coupe les adverbes. Garde une idée par phrase. Le français de bureau n’est pas le français des réseaux. Les deux ont leur place, pas dans le même message.

                Un exercice : prends une offre Luka, surligne cinq mots répétés, réécris ton paragraphe d’accroche avec ces mots. Tu parles déjà la langue de l’employeur.

                Ce n’est pas de la littérature. C’est de la précision. Comme un schéma électrique : si c’est sale, on n’installe pas.
            """.trimIndent(),
        ),
        article(
            id = "entrepreneur-cycle-court",
            domain = "Entrepreneuriat",
            title = "De l’idée au prototype, cycle court",
            excerpt = "Problème réel, client parlé, version minimale, puis on itère. La méthode n’a pas besoin d’un campus américain.",
            author = "Jean Kasongo",
            imageName = "profession_management",
            publishedAtEpochMs = 1_788_393_600_000,
            url = "luka://news/entrepreneur-cycle-court",
            body = """
                Le Martin Trust Center répète un cycle : un problème vécu par quelqu’un de précis, une conversation avec ce quelqu’un, une version assez moche pour tester, puis on recommence.

                À Kinshasa, le client est dans la rue, l’église, le marché. Pas dans un slide. Si tu ne peux pas citer trois personnes qui ont le problème, tu n’as pas encore d’idée — tu as un souhait.

                La version minimale n’est pas une app. C’est un WhatsApp, un carnet, un service fait à la main dix fois. L’app vient si les dix fois marchent.

                Luka n’est pas un incubateur. C’est un thermomètre du marché de l’emploi. Si personne ne paie pour une compétence, un business qui l’embauche sera fragile.
            """.trimIndent(),
        ),
        article(
            id = "entrepreneur-reseau-local",
            domain = "Entrepreneuriat",
            title = "Le réseau local avant les 40 candidatures",
            excerpt = "Un message clair à trois personnes de ton quartier vaut mieux qu’une rafale anonyme.",
            author = "Rédaction Luka",
            imageName = "profession_management",
            publishedAtEpochMs = 1_788_220_800_000,
            url = "luka://news/entrepreneur-reseau-local",
            body = """
                Les plateformes donnent l’illusion du volume. Le volume sans nom ne répond pas. Un cousin qui connaît un chef de chantier, si. Une ancienne prof, si.

                Prépare une phrase : qui tu es, ce que tu sais faire, ce que tu cherches cette semaine. Envoie-la à trois personnes réelles. Demande un conseil, pas un miracle.

                Note les retours. Si tout le monde dit « il faut un permis » ou « il faut l’anglais », ce n’est plus une opinion, c’est un plan.

                Combine ça avec les tendances Luka. Le réseau ouvre la porte. Le marché te dit vers quelle porte marcher.
            """.trimIndent(),
        ),
        article(
            id = "marche-lire-offre",
            domain = "Marché",
            title = "Lis une offre comme un cahier des charges",
            excerpt = "Les cinq mots répétés sont le vrai poste. Ton CV doit les parler, dans le même langage.",
            author = "Rédaction Luka",
            imageName = "profession_hr",
            publishedAtEpochMs = 1_788_048_000_000,
            url = "luka://news/marche-lire-offre",
            body = """
                Une offre n’est pas un poème. C’est une liste de risques que l’employeur veut couvrir. S’il répète « terrain », « Excel », « français », il dit ce qu’il va tester dès le premier jour.

                Imprime ou copie le texte. Surligne. Si tu ne peux pas coller trois preuves (stage, cours, chantier) sur ces mots, ne candidate pas encore : forme-toi une semaine sur le trou le plus gros.

                Luka te montre le volume d’ouvertures. Une offre isolée est un hasard. Un métier qui revient chaque semaine est une piste d’orientation.

                Garde un tableau : date, entreprise, mots-clés, réponse. C’est déjà une analyse de marché personnelle.
            """.trimIndent(),
        ),
        subscribed(
            id = "souscrit-brief-kin",
            domain = "Souscrit",
            title = "Brief de la semaine : où ça recrute à Kin et au Katanga",
            excerpt = "Les familles de métiers qui bougent vraiment, hors rumeurs de groupes. Réservé aux comptes abonnés.",
            author = "Cellule marché Luka",
            imageName = "profession_finance",
            publishedAtEpochMs = 1_789_430_400_000,
            url = "luka://news/souscrit-brief-kin",
            body = """
                Cette semaine, les ouvertures se concentrent sur trois grappes : support informatique et réseaux, électricité industrielle, et commercial sédentaire avec un vrai suivi client. Ce n’est pas « le digital » en général. Ce sont des intitulés précis.

                Kinshasa reste le volume. Lubumbashi et Kolwezi pèsent dès qu’on parle mines, maintenance et HSE. Goma recrute plus court, souvent des missions.

                Ce que les CV jettent encore : une adresse mail fantaisiste, un numéro qui ne prend pas le WhatsApp pro, zéro ville. Les recruteurs filtrent ça avant les compétences.

                Action de la semaine : une preuve visible (photo de chantier, extrait de tableau, lien de projet) collée sous le métier que tu suis dans Luka. Les abonnés qui le font reçoivent plus de signaux utiles que ceux qui relisent les titres.
            """.trimIndent(),
        ),
        subscribed(
            id = "souscrit-contrats",
            domain = "Souscrit",
            title = "Contrats, durées, pièges : ce que les offres cachent",
            excerpt = "CDD renouvelable, prestataire, stage déguisé. Comment lire la ligne « type de contrat » sans se faire avoir.",
            author = "Cellule marché Luka",
            imageName = "profession_hr",
            publishedAtEpochMs = 1_789_171_200_000,
            url = "luka://news/souscrit-contrats",
            body = """
                « 3 mois renouvelables » n’est pas un CDI. C’est un test. Tu peux l’accepter si tu apprends un geste rare. Tu le refuses si on te promet la même chose depuis un an.

                Les mots « prestataire », « consultant », « journalier » déplacent le risque vers toi : pas de fiche, parfois pas de transport. Demande le lieu exact, l’heure, qui paie le taxi.

                Un stage qui copie un poste d’employé à temps plein, sans tuteur, n’est pas un stage. Note le nom du responsable. Garde les messages.

                Les abonnés Luka voient le mix réel des contrats ouverts — pas un slogan. Ça sert à négocier, pas à rêver.
            """.trimIndent(),
        ),
        subscribed(
            id = "souscrit-pipeline",
            domain = "Souscrit",
            title = "Stages et pipelines : comment un campus devient un premier job",
            excerpt = "Les entreprises qui reprennent les stagiaires le disent rarement en public. Voici comment les repérer.",
            author = "Cellule orientation Luka",
            imageName = "profession_management",
            publishedAtEpochMs = 1_788_912_000_000,
            url = "luka://news/souscrit-pipeline",
            body = """
                Un bon stage a un livrable. Un rapport, un schéma, une base. Si au bout de deux semaines tu n’as toujours que du café, change de question : « Quel fichier dois-je rendre vendredi ? »

                Les pipelines se voient dans les offres : la même enseigne qui publie stagiaire, puis junior, puis confirmé. Suis cette enseigne dans Luka.

                Prépare une page : ce que tu as fait, avec qui, quel outil. C’est plus fort qu’une attestation floue.

                Ce dossier est réservé aux abonnés parce qu’il s’appuie sur le flux réel des offres, pas sur une brochure d’école.
            """.trimIndent(),
        ),
        subscribed(
            id = "souscrit-filtre-rh",
            domain = "Souscrit",
            title = "Ce que les RH filtrent en trente secondes",
            excerpt = "Ville, téléphone, métier, une preuve. Le reste est du bruit. Version abonnés, avec exemples congolais.",
            author = "Cellule orientation Luka",
            imageName = "profession_hr",
            publishedAtEpochMs = 1_788_652_800_000,
            url = "luka://news/souscrit-filtre-rh",
            body = """
                Trente secondes. C’est le temps d’un premier tri sur téléphone. Ils cherchent : es-tu dans la ville de l’offre, joignable, et aligné sur le métier nommé — pas « ouvert à toute opportunité ».

                Une photo de profil soignée aide. Un CV de quinze pages nuit. Un PDF nommé `CV_Nom_Metier_Ville.pdf` passe mieux que `document(1).pdf`.

                Les mots copiés d’internet (« force de proposition synergique ») signalent que tu n’as pas lu l’offre. Les mots de l’offre, eux, signalent que tu as lu.

                Les comptes Souscrit voient quels intitulés reviennent vraiment. Ajuste ton titre Luka là-dessus, pas sur un fantasme de poste.
            """.trimIndent(),
        ),
    )

    val mitNews: List<NewsItem> get() = publicNews

    val publicNews: List<NewsItem> get() = catalog.filter { !it.subscribed }

    val subscribedNews: List<NewsItem> get() = catalog.filter { it.subscribed }

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

    private fun article(
        id: String,
        domain: String,
        title: String,
        excerpt: String,
        author: String,
        imageName: String,
        publishedAtEpochMs: Long,
        url: String,
        body: String,
        subscribed: Boolean = false,
        source: String = if (subscribed) "Luka Souscrit" else "Luka News",
    ) = NewsItem(
        id = id,
        title = title,
        excerpt = excerpt,
        source = source,
        imageName = imageName,
        publishedAtEpochMs = publishedAtEpochMs,
        url = url,
        domain = domain,
        author = author,
        body = body,
        subscribed = subscribed,
    )

    private fun subscribed(
        id: String,
        domain: String,
        title: String,
        excerpt: String,
        author: String,
        imageName: String,
        publishedAtEpochMs: Long,
        url: String,
        body: String,
    ) = article(
        id = id,
        domain = domain,
        title = title,
        excerpt = excerpt,
        author = author,
        imageName = imageName,
        publishedAtEpochMs = publishedAtEpochMs,
        url = url,
        body = body,
        subscribed = true,
    )
}
