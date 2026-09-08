package elieoko.mobile.luka.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NorthEast
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import coil3.compose.AsyncImage
import elieoko.mobile.luka.domain.model.City
import elieoko.mobile.luka.domain.model.CongoCatalog
import elieoko.mobile.luka.domain.model.JobOffer
import elieoko.mobile.luka.domain.model.Profession
import elieoko.mobile.luka.domain.usecase.OfferFilters
import elieoko.mobile.luka.presentation.theme.LukaMist
import elieoko.mobile.luka.presentation.theme.LukaRed

@Composable
fun OfferCard(offer: JobOffer, index: Int = 0, onOpen: () -> Unit) {
    var ready by remember(offer.id) { mutableStateOf(false) }
    val scale by animateFloatAsState(if (ready) 1f else 0.94f, tween(320 + index * 50), label = "offer")
    LaunchedEffect(offer.id) {
        delay(index * 40L)
        ready = true
    }
    Surface(
        modifier = Modifier.fillMaxWidth().scale(scale).clickable(onClick = onOpen),
        shape = RoundedCornerShape(22.dp),
        shadowElevation = 1.dp,
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = offer.companyLogoUrl,
                    contentDescription = offer.company,
                    modifier = Modifier.size(44.dp).clip(CircleShape).background(LukaMist),
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(offer.title, style = MaterialTheme.typography.titleMedium)
                    Text("${offer.company} · ${offer.city}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Icon(Icons.Rounded.NorthEast, contentDescription = "Ouvrir l’offre", tint = LukaRed)
            }
            Spacer(Modifier.height(10.dp))
            Text(offer.summary, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(10.dp))
            Text("${offer.profession.emoji}  ${offer.contract} · ${offer.salary}", color = LukaRed, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun FilterStrip(
    filters: OfferFilters,
    onRegion: (String?) -> Unit,
    onCity: (String?) -> Unit,
    onProfession: (String?) -> Unit,
) {
    val cities = CongoCatalog.citiesIn(filters.regionId)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(horizontal = 20.dp)) {
            item { FilterPill("Toutes régions", filters.regionId == null) { onRegion(null) } }
            items(CongoCatalog.regions) { region ->
                FilterPill(region.name, filters.regionId == region.id) { onRegion(region.id) }
            }
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(horizontal = 20.dp)) {
            item { FilterPill("Toutes villes", filters.city == null) { onCity(null) } }
            items(cities) { city: City ->
                FilterPill(city.name, filters.city == city.name) { onCity(city.name) }
            }
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(horizontal = 20.dp)) {
            item { FilterPill("Tous métiers", filters.professionId == null) { onProfession(null) } }
            items(Profession.entries) { profession ->
                FilterPill("${profession.emoji} ${profession.title}", filters.professionId == profession.id) {
                    onProfession(profession.id)
                }
            }
        }
    }
}

@Composable
fun FilterPill(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        shape = RoundedCornerShape(20.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = LukaRed,
            selectedLabelColor = androidx.compose.ui.graphics.Color.White,
        ),
    )
}
