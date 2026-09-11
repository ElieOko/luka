package elieoko.mobile.luka.domain.model

object LukaPlans {
    val all = listOf(
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

    fun byId(id: String) = all.firstOrNull { it.id == id } ?: all.first()
}
