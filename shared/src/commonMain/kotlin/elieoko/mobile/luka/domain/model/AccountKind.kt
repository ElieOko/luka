package elieoko.mobile.luka.domain.model

enum class AccountKind(
    val id: String,
    val title: String,
    val subtitle: String,
) {
    LEARNER(
        "learner",
        "Apprenant",
        "Tendances, news MIT, orientation et conseils pour étudier ou te réorienter.",
    ),
    PROFESSIONAL(
        "professional",
        "Professionnel",
        "Offres, analyse de CV, analyse des offres et marché en temps réel.",
    );

    companion object {
        fun fromId(raw: String?, planId: String = LukaPlans.STARTER): AccountKind = when (raw?.lowercase()) {
            LEARNER.id, "apprenant", "student" -> LEARNER
            PROFESSIONAL.id, "pro" -> PROFESSIONAL
            else -> if (LukaPlans.isStudent(planId)) LEARNER else PROFESSIONAL
        }
    }
}
