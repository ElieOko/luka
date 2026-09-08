package elieoko.mobile.luka.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NorthEast
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import elieoko.mobile.luka.domain.model.CongoCatalog
import elieoko.mobile.luka.domain.model.JobOffer
import elieoko.mobile.luka.presentation.components.PulseDot
import elieoko.mobile.luka.presentation.components.RedHeroGradient
import elieoko.mobile.luka.presentation.theme.LukaGold
import elieoko.mobile.luka.presentation.theme.LukaMist
import elieoko.mobile.luka.presentation.theme.LukaRed
import elieoko.mobile.luka.presentation.theme.imageByName
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val feed = state.feed
    val profile = state.profile
    val uriHandler = LocalUriHandler.current
    val regionName = CongoCatalog.regions.firstOrNull { it.id == profile?.regionId }?.name ?: "RDC"

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item {
            Column(Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PulseDot()
                    Spacer(Modifier.width(8.dp))
                    Text("Analyses infinies actives", color = LukaRed, style = MaterialTheme.typography.labelLarge)
                }
                Spacer(Modifier.height(8.dp))
                Text("Bonjour ${profile?.displayName ?: ""}", style = MaterialTheme.typography.headlineMedium)
                Text("Offres pour ${profile?.profession?.title ?: "toi"} · $regionName", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(14.dp))
                OutlinedTextField(
                    value = state.query,
                    onValueChange = viewModel::onQuery,
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                    placeholder = { Text("Offre, entreprise, ville…") },
                    shape = RoundedCornerShape(18.dp),
                    singleLine = true,
                )
            }
        }
        item {
            AnimatedVisibility(state.liveOffer != null) {
                val live = state.liveOffer
                if (live != null) {
                    Surface(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = LukaRed,
                        shape = RoundedCornerShape(18.dp),
                    ) {
                        Text(
                            "Nouvelle offre STOMP · ${live.title} chez ${live.company}",
                            color = Color.White,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }
            }
        }
        if (feed?.topProfession != null) {
            item {
                Surface(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = LukaMist,
                    shape = RoundedCornerShape(22.dp),
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.TrendingUp, contentDescription = null, tint = LukaRed)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("Métier le plus demandé en RDC", style = MaterialTheme.typography.labelLarge, color = LukaRed)
                            Text(
                                "${feed.topProfession.profession.title} · ${feed.topProfession.openings} ouvertures · ${feed.topProfession.trend}",
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                    }
                }
            }
        }
        item {
            SectionTitle("Offres trouvées")
        }
        items(feed?.offers.orEmpty(), key = { it.id }) { offer ->
            OfferCard(offer) { uriHandler.openUri(offer.applyUrl) }
        }
        item { SectionTitle("Pubs & nouveautés Luka") }
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(feed?.ads.orEmpty()) { ad ->
                    Box(
                        Modifier
                            .width(260.dp)
                            .height(160.dp)
                            .clip(RoundedCornerShape(22.dp)),
                    ) {
                        Image(painterResource(imageByName(ad.imageName)), ad.title, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                        RedHeroGradient(Modifier.fillMaxSize())
                        Column(Modifier.align(Alignment.BottomStart).padding(14.dp)) {
                            Text(ad.title, color = Color.White, style = MaterialTheme.typography.titleLarge)
                            Text(ad.subtitle, color = Color.White.copy(0.9f), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
        items(feed?.news.orEmpty()) { news ->
            Row(
                Modifier
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable { uriHandler.openUri(news.url) }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painterResource(imageByName(news.imageName)),
                    news.title,
                    Modifier.size(72.dp).clip(RoundedCornerShape(14.dp)),
                    contentScale = ContentScale.Crop,
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(news.source, color = LukaRed, style = MaterialTheme.typography.labelLarge)
                    Text(news.title, style = MaterialTheme.typography.titleMedium)
                    Text(news.excerpt, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
                }
            }
        }
        item { SectionTitle("Luka t’oriente") }
        items(feed?.orientation.orEmpty()) { path ->
            Surface(
                modifier = Modifier.padding(horizontal = 20.dp),
                shape = RoundedCornerShape(22.dp),
                color = MaterialTheme.colorScheme.surface,
            ) {
                Row(Modifier.padding(14.dp)) {
                    Image(
                        painterResource(imageByName(path.imageName)),
                        path.title,
                        Modifier.size(88.dp).clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("${path.domain} · ${path.duration}", color = LukaGold, style = MaterialTheme.typography.labelLarge)
                        Text(path.title, style = MaterialTheme.typography.titleMedium)
                        Text(path.why, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Suite : ${path.nextStep}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
        item {
            Row(
                Modifier.padding(horizontal = 20.dp).horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                feed?.stats.orEmpty().forEach { stat ->
                    Surface(shape = RoundedCornerShape(16.dp), color = LukaMist) {
                        Column(Modifier.padding(12.dp)) {
                            Text("${stat.sharePercent} %", color = LukaRed, style = MaterialTheme.typography.titleLarge)
                            Text(stat.profession.title, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(horizontal = 20.dp))
}

@Composable
private fun OfferCard(offer: JobOffer, onOpen: () -> Unit) {
    Surface(
        modifier = Modifier.padding(horizontal = 20.dp).clickable(onClick = onOpen),
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
            Text("${offer.contract} · ${offer.salary}", color = LukaRed, style = MaterialTheme.typography.labelLarge)
        }
    }
}
