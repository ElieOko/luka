package elieoko.mobile.luka.presentation.news

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import elieoko.mobile.luka.core.AppBackHandler
import elieoko.mobile.luka.core.rememberShareAction
import elieoko.mobile.luka.domain.model.formatNewsDate
import elieoko.mobile.luka.presentation.components.LockedFeatureCard
import elieoko.mobile.luka.presentation.components.LukaPrimaryButton
import elieoko.mobile.luka.presentation.theme.LukaCream
import elieoko.mobile.luka.presentation.theme.LukaInk
import elieoko.mobile.luka.presentation.theme.LukaMuted
import elieoko.mobile.luka.presentation.theme.LukaRed
import elieoko.mobile.luka.presentation.theme.imageByName
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NewsArticleScreen(
    articleId: String,
    onBack: () -> Unit,
    onUnlock: () -> Unit,
    viewModel: NewsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val item = state.article(articleId)
    val share = rememberShareAction()
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    AppBackHandler(onBack = onBack)
    Box(Modifier.fillMaxSize().background(LukaCream).statusBarsPadding().navigationBarsPadding()) {
        when {
            item == null -> {
                Column(Modifier.padding(24.dp)) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Retour")
                    }
                    Text("Article introuvable.", color = LukaInk)
                }
            }
            item.subscribed && !state.unlocked -> {
                Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Retour")
                    }
                    LockedFeatureCard(
                        title = "Dossier Souscrit",
                        body = "Cet article fait partie des infos précieuses réservées à l’abonnement.",
                        onUnlock = onUnlock,
                    )
                }
            }
            else -> {
                Column(Modifier.fillMaxSize()) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Retour", tint = LukaInk)
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { viewModel.toggleFavorite(item.id) }) {
                    Icon(
                        if (item.id in state.favorites) Icons.Rounded.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Favori",
                        tint = LukaRed,
                    )
                }
                IconButton(
                    onClick = {
                        val outcome = share(item.title, item.sharePayload())
                        scope.launch {
                            snackbar.showSnackbar(
                                if (outcome.copiedToClipboard) {
                                    "Texte copié. Colle-le dans WhatsApp, Mail ou une autre app."
                                } else {
                                    "Choisis une app pour partager l’article."
                                },
                            )
                        }
                    },
                ) {
                    Icon(Icons.Outlined.Share, contentDescription = "Partager l’article", tint = LukaInk)
                }
            }
            Column(
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp)
                    .padding(bottom = 28.dp),
            ) {
                Text(
                    item.domain.uppercase(),
                    color = LukaRed,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    item.title,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Black,
                    color = LukaInk,
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    item.author,
                    fontWeight = FontWeight.SemiBold,
                    color = LukaInk,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    "Publié le ${formatNewsDate(item.publishedAtEpochMs)} · ${item.source}",
                    color = LukaMuted,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.height(16.dp))
                Image(
                    painterResource(imageByName(item.imageName)),
                    item.title,
                    Modifier.fillMaxWidth().height(210.dp).clip(RoundedCornerShape(18.dp)),
                    contentScale = ContentScale.Crop,
                )
                Spacer(Modifier.height(20.dp))
                item.body.split("\n\n").filter { it.isNotBlank() }.forEach { paragraph ->
                    Text(
                        paragraph.trim(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = LukaInk,
                        modifier = Modifier.padding(bottom = 16.dp),
                    )
                }
                Spacer(Modifier.height(8.dp))
                LukaPrimaryButton(
                    "Partager l’article",
                    onClick = {
                        val outcome = share(item.title, item.sharePayload())
                        scope.launch {
                            snackbar.showSnackbar(
                                if (outcome.copiedToClipboard) {
                                    "Texte copié. Colle-le dans WhatsApp, Mail ou une autre app."
                                } else {
                                    "Choisis une app pour partager l’article."
                                },
                            )
                        }
                    },
                )
                }
                }
            }
        }
        SnackbarHost(hostState = snackbar, modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp))
    }
}
