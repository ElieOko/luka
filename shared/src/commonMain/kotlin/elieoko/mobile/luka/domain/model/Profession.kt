package elieoko.mobile.luka.domain.model

enum class Profession(
    val id: String,
    val title: String,
    val tagline: String,
    val emoji: String,
    val family: String,
    val imageName: String,
) {
    SOFTWARE_ENGINEERING("software", "Ingénierie logiciel", "Apps, APIs, produits numériques", "💻", "Numérique", "profession_software"),
    CYBER_SECURITY("security", "Sécurité informatique", "SOC, pentest, gouvernance", "🛡️", "Numérique", "profession_security"),
    DATA_AI("data", "Data & IA", "Analytique, ML, tableaux de bord", "📊", "Numérique", "profession_software"),
    DESIGN("design", "Design & UX", "Produit, interface, marque", "🎨", "Numérique", "profession_other"),
    TELECOM("telecom", "Télécoms", "Réseaux, fibre, radio", "📡", "Numérique", "profession_security"),
    MANAGEMENT("management", "Management", "Pilotage d’équipes et de projets", "🧭", "Business", "profession_management"),
    FINANCE("finance", "Finance", "Compta, audit, fintech", "💹", "Business", "profession_finance"),
    HUMAN_RESOURCES("hr", "Ressources humaines", "Recrutement et talents", "🤝", "Business", "profession_hr"),
    SALES("sales", "Commercial", "Vente, grands comptes, retail", "🏷️", "Business", "profession_management"),
    MARKETING("marketing", "Marketing", "Communication, digital, marque", "📣", "Business", "profession_other"),
    ELECTRICITY("electricity", "Électricité", "Bâtiments, industriels, SNEL", "⚡", "Industrie", "profession_other"),
    ENERGY("energy", "Énergie", "Hydro, solaire, réseaux", "🔆", "Industrie", "profession_other"),
    MECHANICS("mechanics", "Mécanique", "Maintenance, atelier, engins", "🔧", "Industrie", "profession_other"),
    CIVIL_ENGINEERING("civil", "BTP & génie civil", "Chantiers, routes, mines", "🏗️", "Industrie", "profession_other"),
    MINING("mining", "Mines", "Géologie, process, HSE", "⛏️", "Industrie", "profession_other"),
    LOGISTICS("logistics", "Logistique", "Supply, last-mile, entrepôts", "📦", "Services", "profession_other"),
    TRANSPORT("transport", "Transport", "Flotte, aérien, transit", "🚚", "Services", "profession_other"),
    HEALTH("health", "Santé", "Soins, pharma, coordination", "🩺", "Services", "profession_other"),
    EDUCATION("education", "Éducation", "Enseignement, formation", "📚", "Services", "profession_other"),
    LEGAL("legal", "Juridique", "Droit des affaires, compliance", "⚖️", "Services", "profession_other"),
    AGRICULTURE("agriculture", "Agriculture", "Agribusiness, agronomie", "🌾", "Services", "profession_other"),
    HOSPITALITY("hospitality", "Hôtellerie", "Hotels, restauration, events", "🏨", "Services", "profession_other"),
    PLUMBING("plumbing", "Plomberie", "Sanitaire, réseaux, chantiers", "🚿", "Industrie", "profession_other"),
    ENVIRONMENT("environment", "Environnement", "Climat, déchets, RSE", "🌿", "Industrie", "profession_other"),
    PUBLIC_ADMIN("admin", "Administration publique", "État, collectivités, ONG", "🏛️", "Services", "profession_other"),
    JOURNALISM("journalism", "Journalisme", "Médias, radio, digital news", "🎙️", "Services", "profession_other"),
    FASHION("fashion", "Mode & stylisme", "Création, retail, ateliers", "👗", "Services", "profession_other"),
    OTHER("other", "Autres", "Tous les autres métiers", "✨", "Services", "profession_other");

    companion object {
        fun fromId(id: String?): Profession =
            entries.firstOrNull { it.id == id } ?: SOFTWARE_ENGINEERING
    }
}
