package elieoko.mobile.luka.domain.model

enum class Profession(
    val id: String,
    val title: String,
    val tagline: String,
    val imageName: String,
) {
    SOFTWARE_ENGINEERING(
        id = "software",
        title = "Ingénierie logiciel",
        tagline = "Construire les produits numériques de la RDC",
        imageName = "profession_software",
    ),
    CYBER_SECURITY(
        id = "security",
        title = "Sécurité informatique",
        tagline = "Protéger les banques, telcos et administrations",
        imageName = "profession_security",
    ),
    MANAGEMENT(
        id = "management",
        title = "Management",
        tagline = "Piloter les équipes et les projets stratégiques",
        imageName = "profession_management",
    ),
    FINANCE(
        id = "finance",
        title = "Finance",
        tagline = "Comptabilité, audit, fintech et marchés",
        imageName = "profession_finance",
    ),
    HUMAN_RESOURCES(
        id = "hr",
        title = "Ressources humaines",
        tagline = "Recruter et accompagner les talents",
        imageName = "profession_hr",
    ),
    OTHER(
        id = "other",
        title = "Autres",
        tagline = "Santé, logistique, juridique, communication…",
        imageName = "profession_other",
    );

    companion object {
        fun fromId(id: String?): Profession =
            entries.firstOrNull { it.id == id } ?: SOFTWARE_ENGINEERING
    }
}
