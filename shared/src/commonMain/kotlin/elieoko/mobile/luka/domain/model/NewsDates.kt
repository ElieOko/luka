package elieoko.mobile.luka.domain.model

import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

private val FrenchMonths = listOf(
    "janvier", "février", "mars", "avril", "mai", "juin",
    "juillet", "août", "septembre", "octobre", "novembre", "décembre",
)

@OptIn(ExperimentalTime::class)
fun formatNewsDate(epochMs: Long): String {
    val date = Instant.fromEpochMilliseconds(epochMs).toLocalDateTime(TimeZone.UTC).date
    val month = FrenchMonths.getOrNull(date.month.number - 1) ?: ""
    return "${date.day} $month ${date.year}"
}
