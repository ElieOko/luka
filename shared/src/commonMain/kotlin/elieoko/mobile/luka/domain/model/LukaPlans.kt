package elieoko.mobile.luka.domain.model

object LukaPlans {
    const val STARTER = "starter"
    const val STUDENT = "student"
    const val PROFESSIONAL = "professional"

    val starter = SubscriptionPlan(
        id = STARTER,
        name = "Gratuit",
        priceLabel = "0 $",
        period = "toujours",
        highlight = false,
        professionSlots = 1,
        recruiterVisible = false,
        orientationPlus = false,
        perks = listOf(
            "1 métier suivi",
            "Offres RDC",
            "Alertes des nouvelles opportunités",
        ),
    )

    val student = SubscriptionPlan(
        id = STUDENT,
        name = "Étudiant",
        priceLabel = "3 $",
        period = "/ mois",
        highlight = false,
        professionSlots = 1,
        recruiterVisible = false,
        orientationPlus = true,
        perks = listOf(
            "Orientation",
            "Tendances",
            "News des universités mondiales et américaines",
            "Revues scientifiques",
        ),
        apiId = 1,
        usdAmount = 3.0,
    )

    val professional = SubscriptionPlan(
        id = PROFESSIONAL,
        name = "Professionnel",
        priceLabel = "5 $",
        period = "/ mois",
        highlight = true,
        professionSlots = Profession.entries.size,
        recruiterVisible = true,
        orientationPlus = false,
        perks = listOf(
            "Recherche d’emploi",
            "Optimisation du profil",
            "Amélioration du profil",
            "Proposition du profil dans certaines entreprises",
            "Profil actif au moins 3 mois",
        ),
        apiId = 2,
        usdAmount = 5.0,
    )

    val all = listOf(starter, student, professional)
    val paid = listOf(student, professional)

    fun byId(id: String): SubscriptionPlan = when (id) {
        STUDENT -> student
        PROFESSIONAL, "plus", "pro", "elite" -> professional
        else -> starter
    }

    fun byApiId(id: Long): SubscriptionPlan = paid.firstOrNull { it.apiId == id } ?: student

    fun isProfessional(id: String) = byId(id).id == PROFESSIONAL
    fun isStudent(id: String) = byId(id).id == STUDENT
}
