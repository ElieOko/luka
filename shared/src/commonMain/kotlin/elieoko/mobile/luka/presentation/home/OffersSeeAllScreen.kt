package elieoko.mobile.luka.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.presentation.components.FilterBottomSheet
import elieoko.mobile.luka.presentation.components.OfferCard
import elieoko.mobile.luka.presentation.theme.LukaRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OffersSeeAllScreen(
    onBack: () -> Unit,
    viewModel: HomeViewModel,
) {
    val state by viewModel.state.collectAsState()
    val uriHandler = LocalUriHandler.current
    var showFilters by rememberSaveable { mutableStateOf(false) }
    if (showFilters) {
        FilterBottomSheet(
            filters = state.filters,
            query = state.query,
            onQuery = viewModel::onQuery,
            onCity = viewModel::onCity,
            onProfession = viewModel::onProfession,
            onDismiss = { showFilters = false },
        )
    }
    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Column {
                    Text("Toutes les offres", fontWeight = FontWeight.Bold)
                    Text(
                        "${state.allOffers.size} résultats",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Retour")
                }
            },
            actions = {
                IconButton(onClick = { showFilters = true }) {
                    Icon(Icons.Outlined.Search, contentDescription = "Recherche et filtres", tint = LukaRed)
                }
            },
        )
        LazyColumn(
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            itemsIndexed(state.allOffers, key = { _, o -> o.id }) { index, offer ->
                OfferCard(offer, index) {
                    runCatching { uriHandler.openUri(offer.applyUrl) }
                }
            }
        }
    }
}
