package elieoko.mobile.luka.domain.usecase

import elieoko.mobile.luka.domain.model.JobOffer
import elieoko.mobile.luka.domain.model.Profession

data class OfferFilters(
    val regionId: String? = null,
    val city: String? = null,
    val professionId: String? = null,
) {
    val isEmpty: Boolean get() = regionId == null && city == null && professionId == null
}

fun List<JobOffer>.applyFilters(filters: OfferFilters): List<JobOffer> = filter { offer ->
    val regionOk = filters.regionId == null || offer.regionId == filters.regionId
    val cityOk = filters.city == null || offer.city.equals(filters.city, ignoreCase = true)
    val jobOk = filters.professionId == null || offer.profession.id == filters.professionId ||
        offer.profession == Profession.fromId(filters.professionId)
    regionOk && cityOk && jobOk
}
