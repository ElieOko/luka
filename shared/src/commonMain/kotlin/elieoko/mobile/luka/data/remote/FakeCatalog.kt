package elieoko.mobile.luka.data.remote

import elieoko.mobile.luka.domain.model.DemandStat
import elieoko.mobile.luka.domain.model.JobOffer
import elieoko.mobile.luka.domain.model.NewsItem
import elieoko.mobile.luka.domain.model.OrientationPath
import elieoko.mobile.luka.domain.model.PlatformAd
import elieoko.mobile.luka.domain.model.Profession
import elieoko.mobile.luka.domain.model.Professional
import elieoko.mobile.luka.domain.model.SubscriptionPlan
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
object FakeCatalog {
    private fun ago(days: Long): Long =
        (Clock.System.now() - days.days).toEpochMilliseconds()

    val plans = listOf(
        SubscriptionPlan(
            id = "starter",
            name = "Luka Starter",
            priceLabel = "Gratuit",
            period = "toujours",
            highlight = false,
            professionSlots = 1,
            recruiterVisible = false,
            orientationPlus = false,
            perks = listOf(
                "1 métier analysé en continu",
                "Offres RDC + liens de candidature",
                "Alertes OneSignal des nouvelles opportunités",
            ),
        ),
        SubscriptionPlan(
            id = "plus",
            name = "Luka Plus",
            priceLabel = "9 USD",
            period = "/ mois",
            highlight = false,
            professionSlots = 2,
            recruiterVisible = false,
            orientationPlus = false,
            perks = listOf(
                "2 métiers au choix",
                "Filtrage par ville",
                "Actualités Luka en avant-première",
            ),
        ),
        SubscriptionPlan(
            id = "pro",
            name = "Luka Pro",
            priceLabel = "19 USD",
            period = "/ mois",
            highlight = true,
            professionSlots = Profession.entries.size,
            recruiterVisible = true,
            orientationPlus = false,
            perks = listOf(
                "Tous les métiers",
                "Profil visible par les recruteurs",
                "Statistiques de demande au Congo",
            ),
        ),
        SubscriptionPlan(
            id = "elite",
            name = "Luka Elite",
            priceLabel = "39 USD",
            period = "/ mois",
            highlight = false,
            professionSlots = Profession.entries.size,
            recruiterVisible = true,
            orientationPlus = true,
            perks = listOf(
                "Tout Luka Pro",
                "Moteur d’orientation numérique",
                "Mise en avant auprès des entreprises",
            ),
        ),
    )

    val offers = listOf(
        JobOffer("o1", "Ingénieur logiciel Kotlin", "Rawbank", "https://ui-avatars.com/api/?name=Rawbank&background=E31B23&color=fff", Profession.SOFTWARE_ENGINEERING, "kinshasa", "Kinshasa", "CDI", "1 800 – 2 400 USD", "Construire les APIs mobile banking utilisées par des millions de Congolais.", "https://www.rawbank.cd/careers", ago(1), false),
        JobOffer("o2", "Développeur Full-Stack", "Orange RDC", "https://ui-avatars.com/api/?name=Orange&background=FF7900&color=fff", Profession.SOFTWARE_ENGINEERING, "kinshasa", "Kinshasa", "CDI", "1 500 – 2 100 USD", "Applications self-care, USSD et portails clients.", "https://www.orange.cd/emploi", ago(2), false),
        JobOffer("o3", "Mobile Engineer Flutter", "Maxicash", "https://ui-avatars.com/api/?name=Maxicash&background=1B5E20&color=fff", Profession.SOFTWARE_ENGINEERING, "kinshasa", "Kinshasa", "CDI", "1 600 – 2 200 USD", "Wallet mobile, QR et intégrations fintech.", "https://www.maxicash.cd/jobs", ago(3), true),
        JobOffer("o4", "Ingénieur backend Java", "Vodacom DRC", "https://ui-avatars.com/api/?name=Vodacom&background=E60000&color=fff", Profession.SOFTWARE_ENGINEERING, "kinshasa", "Kinshasa", "CDI", "2 000 – 2 800 USD", "Plateforme M-Pesa et microservices à haute disponibilité.", "https://www.vodacom.cd/careers", ago(4), false),
        JobOffer("o5", "Analyste cybersécurité SOC", "Equity BCDC", "https://ui-avatars.com/api/?name=BCDC&background=0D47A1&color=fff", Profession.CYBER_SECURITY, "kinshasa", "Kinshasa", "CDI", "1 700 – 2 300 USD", "Supervision 24/7, réponse à incident et durcissement SI.", "https://www.equitybcdc.cd/carriere", ago(1), false),
        JobOffer("o6", "Pentester junior", "Airtel Congo", "https://ui-avatars.com/api/?name=Airtel&background=ED1C24&color=fff", Profession.CYBER_SECURITY, "kinshasa", "Kinshasa", "Freelance", "80 – 120 USD / j", "Tests d’intrusion sur apps mobiles et API partenaires.", "https://www.airtel.cd/careers", ago(5), true),
        JobOffer("o7", "RSSIe adjoint", "Régideso", "https://ui-avatars.com/api/?name=Regideso&background=01579B&color=fff", Profession.CYBER_SECURITY, "kinshasa", "Kinshasa", "CDI", "2 200 – 3 000 USD", "Gouvernance ISO 27001 et sensibilisation des métiers.", "https://www.regideso.cd/emploi", ago(6), false),
        JobOffer("o8", "Chef de projet digital", "UNICEF RDC", "https://ui-avatars.com/api/?name=UNICEF&background=00AEEF&color=fff", Profession.MANAGEMENT, "kinshasa", "Kinshasa", "CDD", "2 400 – 3 200 USD", "Piloter des programmes d’inclusion numérique des jeunes.", "https://jobs.unicef.org", ago(2), false),
        JobOffer("o9", "Product Manager", "Illicocash", "https://ui-avatars.com/api/?name=Illicocash&background=6A1B9A&color=fff", Profession.MANAGEMENT, "kinshasa", "Kinshasa", "CDI", "1 900 – 2 600 USD", "Roadmap wallet et expérience marchands.", "https://www.illicocash.cd/jobs", ago(3), false),
        JobOffer("o10", "Manager opérations mines", "TFM", "https://ui-avatars.com/api/?name=TFM&background=37474F&color=fff", Profession.MANAGEMENT, "lualaba", "Kolwezi", "CDI", "3 000 – 4 500 USD", "Coordination des équipes terrain et KPIs production.", "https://www.tfm.cd/careers", ago(4), false),
        JobOffer("o11", "Contrôleur de gestion", "Bracongo", "https://ui-avatars.com/api/?name=Bracongo&background=C62828&color=fff", Profession.FINANCE, "kinshasa", "Kinshasa", "CDI", "1 400 – 1 900 USD", "Budgets, reporting et analyse de marges.", "https://www.bracongo.cd/carriere", ago(2), false),
        JobOffer("o12", "Analyste crédit PME", "TMB", "https://ui-avatars.com/api/?name=TMB&background=AD1457&color=fff", Profession.FINANCE, "haut-katanga", "Lubumbashi", "CDI", "1 200 – 1 700 USD", "Étude des dossiers et scoring des entrepreneurs du Katanga.", "https://www.tmb.cd/jobs", ago(5), false),
        JobOffer("o13", "Comptable senior", "SNEL", "https://ui-avatars.com/api/?name=SNEL&background=F9A825&color=111", Profession.FINANCE, "kinshasa", "Kinshasa", "CDI", "1 100 – 1 500 USD", "Clôture mensuelle, IFRS et relations audit.", "https://www.snel.cd/carriere", ago(7), false),
        JobOffer("o14", "Responsable talent acquisition", "Celtel/Airtel", "https://ui-avatars.com/api/?name=TA&background=D32F2F&color=fff", Profession.HUMAN_RESOURCES, "kinshasa", "Kinshasa", "CDI", "1 300 – 1 800 USD", "Sourcing tech et campagnes campus Kinshasa.", "https://www.airtel.cd/jobs", ago(1), false),
        JobOffer("o15", "Chargé de formation", "Banque Commerciale du Congo", "https://ui-avatars.com/api/?name=BCDC&background=1565C0&color=fff", Profession.HUMAN_RESOURCES, "kinshasa", "Kinshasa", "CDI", "1 000 – 1 400 USD", "Parcours d’onboarding et académie digitale interne.", "https://www.bcdc.cd/carriere", ago(8), false),
        JobOffer("o16", "HRBP mines", "Kamoa Copper", "https://ui-avatars.com/api/?name=Kamoa&background=4E342E&color=fff", Profession.HUMAN_RESOURCES, "lualaba", "Kolwezi", "CDI", "1 800 – 2 500 USD", "Accompagnement des managers et climat social.", "https://www.kamoacopper.com/careers", ago(3), false),
        JobOffer("o17", "Logisticien last-mile", "Jumia RDC", "https://ui-avatars.com/api/?name=Jumia&background=F57C00&color=fff", Profession.OTHER, "kinshasa", "Kinshasa", "CDI", "700 – 1 000 USD", "Optimiser les tournées Kinshasa et Lubumbashi.", "https://group.jumia.com/careers", ago(2), false),
        JobOffer("o18", "Infirmier coordinateur", "MSF", "https://ui-avatars.com/api/?name=MSF&background=FFFFFF&color=E31B23", Profession.OTHER, "nord-kivu", "Goma", "CDD", "Indemnités + package", "Coordination clinique et formation des équipes locales.", "https://www.msf.org/jobs", ago(4), false),
        JobOffer("o19", "Community manager", "Digital Congo", "https://ui-avatars.com/api/?name=DC&background=111111&color=fff", Profession.OTHER, "kinshasa", "Kinshasa", "CDI", "600 – 900 USD", "Récits des jeunes talents et campagnes Luka.", "https://www.digitalcongo.net/jobs", ago(6), true),
        JobOffer("o20", "Data analyst junior", "INS RDC", "https://ui-avatars.com/api/?name=INS&background=1B5E20&color=fff", Profession.SOFTWARE_ENGINEERING, "kinshasa", "Gombe", "CDD", "900 – 1 300 USD", "Tableaux de bord emploi et statistiques provinciales.", "https://www.ins.cd/carriere", ago(9), false),
        JobOffer("o21", "Électricien industriel", "SNEL", "https://ui-avatars.com/api/?name=SNEL&background=F9A825&color=111", Profession.ELECTRICITY, "kinshasa", "Limete", "CDI", "800 – 1 200 USD", "Maintenance HT/BT des postes urbains de Kinshasa.", "https://www.snel.cd/carriere", ago(2), false),
        JobOffer("o22", "Technicien solaire", "BBOXX", "https://ui-avatars.com/api/?name=BBOXX&background=F57C00&color=fff", Profession.ENERGY, "nord-kivu", "Goma", "CDI", "700 – 1 100 USD", "Installations mini-grids et SAV dans l’Est.", "https://www.bboxx.com/careers", ago(3), false),
        JobOffer("o23", "Géologue junior", "Kamoa Copper", "https://ui-avatars.com/api/?name=Kamoa&background=4E342E&color=fff", Profession.MINING, "lualaba", "Kolwezi", "CDI", "1 600 – 2 200 USD", "Cartographie et suivi de production.", "https://www.kamoacopper.com/careers", ago(1), false),
        JobOffer("o24", "Conducteur de travaux", "Rawji Construction", "https://ui-avatars.com/api/?name=Rawji&background=37474F&color=fff", Profession.CIVIL_ENGINEERING, "kinshasa", "Ngaliema", "CDI", "1 200 – 1 800 USD", "Chantiers voirie et bâtiments Gombe / Ngaliema.", "https://www.rawji.cd/jobs", ago(4), false),
        JobOffer("o25", "Mécanicien engins", "TFM", "https://ui-avatars.com/api/?name=TFM&background=455A64&color=fff", Profession.MECHANICS, "lualaba", "Kolwezi", "CDI", "1 100 – 1 600 USD", "Atelier mines, hydraulique et diagnostic.", "https://www.tfm.cd/careers", ago(5), false),
        JobOffer("o26", "Infirmier coordinateur", "MSF", "https://ui-avatars.com/api/?name=MSF&background=FFFFFF&color=E31B23", Profession.HEALTH, "nord-kivu", "Goma", "CDD", "Indemnités + package", "Coordination clinique et formation des équipes locales.", "https://www.msf.org/jobs", ago(4), false),
        JobOffer("o27", "Juriste minier", "Cabinet Kalala", "https://ui-avatars.com/api/?name=JK&background=1A237E&color=fff", Profession.LEGAL, "haut-katanga", "Lubumbashi", "CDI", "1 400 – 2 000 USD", "Contrats d’exploitation et compliance OHADA.", "https://www.kalala.cd/jobs", ago(6), false),
        JobOffer("o28", "Ingénieur data", "Vodacom DRC", "https://ui-avatars.com/api/?name=Voda&background=E60000&color=fff", Profession.DATA_AI, "kinshasa", "Gombe", "CDI", "1 800 – 2 500 USD", "Scoring crédit et fraude M-Pesa.", "https://www.vodacom.cd/careers", ago(2), false),
        JobOffer("o29", "UX designer", "Maxicash", "https://ui-avatars.com/api/?name=UX&background=6A1B9A&color=fff", Profession.DESIGN, "kinshasa", "Gombe", "CDI", "1 200 – 1 700 USD", "Parcours wallet et accessibilité low-tech.", "https://www.maxicash.cd/jobs", ago(3), true),
        JobOffer("o30", "Commercial B2B fibre", "Airtel Congo", "https://ui-avatars.com/api/?name=Fibre&background=ED1C24&color=fff", Profession.SALES, "haut-katanga", "Lubumbashi", "CDI", "900 – 1 400 USD + primes", "Grandes entreprises du Copperbelt.", "https://www.airtel.cd/careers", ago(1), false),
    )

    val ads = listOf(
        PlatformAd("ad1", "Luka Pro", "Les recruteurs voient enfin ton profil.", "Découvrir", "ad_platform", "plan:pro"),
        PlatformAd("ad2", "Académie Luka", "12 semaines pour basculer vers le numérique.", "S’orienter", "orientation_digital", "orientation"),
        PlatformAd("ad3", "Kinshasa recrute", "Boulevard du 30 Juin, les telcos n’attendent plus.", "Voir", "onboarding_kinshasa_1", "offers"),
        PlatformAd("ad4", "Equity & banques", "Les sièges Gombe ouvrent des postes data.", "Postuler", "onboarding_kinshasa_2", "plan:plus"),
        PlatformAd("ad5", "Justice & lex", "Les juristes d’affaires manquent à Lubumbashi.", "Explorer", "onboarding_kinshasa_3", "trends"),
    )

    val news = listOf(
        NewsItem("n1", "Kinshasa concentre 48 % des offres tech", "Les telcos et banques digitalisent plus vite que le reste du pays.", "Luka Insights", "onboarding_kinshasa_1", ago(1), "https://luka.cd/news/kinshasa-tech"),
        NewsItem("n2", "La cybersécurité, métier le plus sous-tendu", "Moins de 400 profils formés pour plus de 1 200 postes ouverts.", "Luka Lab", "profession_security", ago(3), "https://luka.cd/news/cyber"),
        NewsItem("n3", "Fibre et 5G : Airtel et Vodacom accélèrent", "Les déploiements créent des postes radio, data et commercial B2B.", "Luka Connect", "onboarding_kinshasa_2", ago(2), "https://luka.cd/news/fibre"),
        NewsItem("n4", "L’IA arrive dans le scoring M-Pesa", "Les banques cherchent des profils data qui parlent Lingala et SQL.", "Luka Plus", "profession_software", ago(4), "https://luka.cd/news/ia"),
        NewsItem("n5", "Solaire à Goma : mini-grids et techniciens", "L’énergie décentralisée recrute plus vite que le diesel.", "Luka Orient", "onboarding_kinshasa_3", ago(5), "https://luka.cd/news/solaire"),
        NewsItem("n6", "UX pour le low-tech", "Les wallets congolais ont besoin de designers qui comprennent USSD.", "Luka Design", "profession_other", ago(6), "https://luka.cd/news/ux"),
        NewsItem("n7", "Les jeunes du Kivu se forment en ligne", "Goma et Bukavu voient émerger des hubs numériques.", "Luka Orient", "onboarding_learn", ago(7), "https://luka.cd/news/kivu"),
        NewsItem("n8", "Finance : les fintechs recrutent des comptables data", "Le couple Excel + SQL devient un standard à Kinshasa.", "Luka Plus", "profession_finance", ago(8), "https://luka.cd/news/fintech"),
    )

    val professionals = listOf(
        Professional("p1", "Grace Mwamba", "Lead Android", Profession.SOFTWARE_ENGINEERING, "Kinshasa", "8 ans d’apps bancaires. J’aide les équipes à livrer en Kotlin.", "avatar_grace", 4.9, 32, true),
        Professional("p2", "Patrick Ilunga", "Expert SOC", Profession.CYBER_SECURITY, "Kinshasa", "Mise en place de SOC pour banques et ministères.", "avatar_patrick", 4.8, 21, true),
        Professional("p3", "Aimée Kasongo", "Coach produit", Profession.MANAGEMENT, "Lubumbashi", "J’accompagne les PM junior des opérateurs miniers.", "avatar_aimee", 4.7, 18, true),
        Professional("p4", "Jean Kalala", "CFO à temps partagé", Profession.FINANCE, "Kinshasa", "Structuration financière des startups congolaises.", "avatar_jean", 4.6, 27, false),
        Professional("p5", "Sarah Ngalula", "RH digital", Profession.HUMAN_RESOURCES, "Goma", "Marque employeur et recrutement tech dans l’Est.", "avatar_grace", 4.8, 14, true),
        Professional("p6", "David Tshilombo", "Architecte cloud", Profession.SOFTWARE_ENGINEERING, "Kinshasa", "AWS / GCP pour les scale-ups de Gombe.", "avatar_patrick", 4.9, 40, true),
    )

    val orientation = listOf(
        OrientationPath("or1", "Devenir développeur mobile", "Numérique", "6 mois", "Débutant", "Le mobile est le premier canal d’emploi et de paiement en RDC.", "Parcours Kotlin + un projet wallet", "orientation_digital", 96),
        OrientationPath("or2", "Analyste cybersécurité", "Numérique", "8 mois", "Intermédiaire", "Les banques n’arrivent pas à pourvoir les postes SOC.", "Certification + stage SOC simulé", "profession_security", 91),
        OrientationPath("or3", "Product management", "Management", "4 mois", "Débutant", "Les telcos cherchent des PM qui comprennent le terrain.", "Cas M-Pesa et un portfolio de 3 features", "profession_management", 78),
        OrientationPath("or4", "Comptabilité + data", "Finance", "5 mois", "Débutant", "La fintech a besoin de profils qui parlent chiffres et SQL.", "Excel avancé, Power BI, IFRS local", "profession_finance", 74),
        OrientationPath("or5", "Recruteur tech", "Ressources humaines", "3 mois", "Débutant", "Sourcing des talents numériques, un métier rare à Kinshasa.", "Boolean search + communauté Luka", "profession_hr", 69),
        OrientationPath("or6", "Logistique e-commerce", "Autres", "4 mois", "Débutant", "Jumia et les dark stores recrutent des ops de terrain.", "Stage last-mile + Excel ops", "profession_other", 61),
        OrientationPath("or7", "Électricien du bâtiment au solaire", "Industrie", "5 mois", "Débutant", "La SNEL et les mini-grids manquent de techniciens certifiés.", "Câblage + PV + habilitation", "profession_other", 72),
        OrientationPath("or8", "Data analyst Congo", "Numérique", "6 mois", "Débutant", "Les telcos paient le SQL mieux que le diplôme seul.", "Excel, SQL, un dashboard emploi", "profession_software", 88),
    )

    val stats = listOf(
        DemandStat(Profession.SOFTWARE_ENGINEERING, 1860, 22, "+18 %"),
        DemandStat(Profession.ELECTRICITY, 1540, 18, "+14 %"),
        DemandStat(Profession.FINANCE, 1210, 14, "+9 %"),
        DemandStat(Profession.MINING, 1100, 13, "+11 %"),
        DemandStat(Profession.CYBER_SECURITY, 980, 12, "+24 %"),
        DemandStat(Profession.CIVIL_ENGINEERING, 740, 9, "+7 %"),
        DemandStat(Profession.DATA_AI, 610, 7, "+21 %"),
        DemandStat(Profession.HEALTH, 510, 6, "+4 %"),
        DemandStat(Profession.MANAGEMENT, 480, 5, "+6 %"),
        DemandStat(Profession.HUMAN_RESOURCES, 320, 4, "+3 %"),
    )
}
