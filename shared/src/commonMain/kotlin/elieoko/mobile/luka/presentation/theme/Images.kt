package elieoko.mobile.luka.presentation.theme

import elieoko.mobile.luka.domain.model.Profession
import luka.shared.generated.resources.Res
import luka.shared.generated.resources.ad_news
import luka.shared.generated.resources.ad_platform
import luka.shared.generated.resources.avatar_aimee
import luka.shared.generated.resources.avatar_grace
import luka.shared.generated.resources.avatar_jean
import luka.shared.generated.resources.avatar_patrick
import luka.shared.generated.resources.hero_career
import luka.shared.generated.resources.onboarding_city
import luka.shared.generated.resources.onboarding_learn
import luka.shared.generated.resources.onboarding_team
import luka.shared.generated.resources.orientation_digital
import luka.shared.generated.resources.profession_finance
import luka.shared.generated.resources.profession_hr
import luka.shared.generated.resources.profession_management
import luka.shared.generated.resources.profession_other
import luka.shared.generated.resources.profession_security
import luka.shared.generated.resources.profession_software
import org.jetbrains.compose.resources.DrawableResource

fun Profession.image(): DrawableResource = when (this) {
    Profession.SOFTWARE_ENGINEERING -> Res.drawable.profession_software
    Profession.CYBER_SECURITY -> Res.drawable.profession_security
    Profession.MANAGEMENT -> Res.drawable.profession_management
    Profession.FINANCE -> Res.drawable.profession_finance
    Profession.HUMAN_RESOURCES -> Res.drawable.profession_hr
    Profession.OTHER -> Res.drawable.profession_other
}

fun imageByName(name: String): DrawableResource = when (name) {
    "ad_platform" -> Res.drawable.ad_platform
    "ad_news" -> Res.drawable.ad_news
    "orientation_digital" -> Res.drawable.orientation_digital
    "profession_security" -> Res.drawable.profession_security
    "profession_finance" -> Res.drawable.profession_finance
    "profession_management" -> Res.drawable.profession_management
    "profession_hr" -> Res.drawable.profession_hr
    "profession_other" -> Res.drawable.profession_other
    "onboarding_learn" -> Res.drawable.onboarding_learn
    "avatar_grace" -> Res.drawable.avatar_grace
    "avatar_patrick" -> Res.drawable.avatar_patrick
    "avatar_aimee" -> Res.drawable.avatar_aimee
    "avatar_jean" -> Res.drawable.avatar_jean
    "hero_career" -> Res.drawable.hero_career
    "onboarding_city" -> Res.drawable.onboarding_city
    "onboarding_team" -> Res.drawable.onboarding_team
    else -> Res.drawable.hero_career
}
