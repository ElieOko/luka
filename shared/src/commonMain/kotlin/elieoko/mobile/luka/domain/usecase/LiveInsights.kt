package elieoko.mobile.luka.domain.usecase

import elieoko.mobile.luka.domain.model.DemandStat
import elieoko.mobile.luka.domain.model.JobOffer
import elieoko.mobile.luka.domain.model.NationalInsight
import elieoko.mobile.luka.domain.model.OrientationPersona

object LiveInsights {
    fun demandFrom(offers: List<JobOffer>): List<DemandStat> {
        if (offers.isEmpty()) return emptyList()
        val grouped = offers.groupBy { it.profession }
        val total = offers.size
        return grouped.entries
            .sortedByDescending { it.value.size }
            .map { (profession, list) ->
                val share = (list.size * 100) / total
                DemandStat(
                    profession = profession,
                    openings = list.size,
                    sharePercent = share.coerceAtLeast(1),
                    trend = "${list.size} offres",
                )
            }
    }

    fun forPersona(persona: OrientationPersona, offers: List<JobOffer>): List<NationalInsight> =
        demandFrom(offers).take(7).mapIndexed { index, stat ->
            NationalInsight(
                persona = persona,
                rank = index + 1,
                label = stat.profession.title,
                sharePercent = stat.sharePercent,
                detail = "${stat.openings} offres ouvertes — ${persona.lens}.",
            )
        }
}
