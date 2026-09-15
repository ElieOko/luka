package elieoko.mobile.luka.presentation.legal

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.presentation.theme.LukaInk
import elieoko.mobile.luka.presentation.theme.LukaMuted

@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Retour")
            }
            Text("Politique de confidentialité", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = LukaInk)
        }
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
        ) {
            Text("Luka", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
            Text("Dernière mise à jour : septembre 2026", color = LukaMuted, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(16.dp))
            PolicyBlock(
                "Qui nous sommes",
                "Luka est une application d’orientation et d’emploi centrée sur la République démocratique du Congo. L’éditeur traite tes données pour te connecter aux offres, aux analyses de marché et aux conseils de carrière.",
            )
            PolicyBlock(
                "Données collectées",
                "Nous enregistrons le numéro de téléphone (authentification par SMS), le prénom ou nom que tu indiques, la ville, le métier choisi, le type de compte (apprenant ou professionnel), un CV si tu le déposes, et les événements techniques nécessaires au fonctionnement (jetons, serial appareil, journaux d’erreur).",
            )
            PolicyBlock(
                "Pourquoi",
                "Créer et sécuriser ton compte, personnaliser les offres et les tendances, analyser un CV ou des offres si tu es professionnel abonné, envoyer des codes OTP, et améliorer le service. Nous ne vendons pas tes données.",
            )
            PolicyBlock(
                "Partage",
                "Les SMS OTP passent par notre serveur (Casanayo). Le paiement d’abonnement, si tu en prends un, transite par le prestataire de paiement. Les crashs peuvent être envoyés à un outil de diagnostic. Aucun partenaire n’utilise tes données pour de la pub tierce.",
            )
            PolicyBlock(
                "Conservation",
                "Le compte reste tant que tu l’utilises. Tu peux te déconnecter à tout moment. Pour une suppression définitive, contacte le support Luka ; nous effaçons alors le profil local et demandons l’effacement côté serveur.",
            )
            PolicyBlock(
                "Tes droits",
                "Tu peux consulter et modifier nom, e-mail, ville et CV dans l’app. Tu peux refuser l’abonnement. L’usage de Luka implique d’avoir 16 ans ou plus, ou l’accord d’un responsable.",
            )
            PolicyBlock(
                "Contact",
                "Pour toute question relative à cette politique : support via l’application Luka, à Kinshasa, RDC.",
            )
        }
    }
}

@Composable
private fun PolicyBlock(title: String, body: String) {
    Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = LukaInk)
    Spacer(Modifier.height(6.dp))
    Text(body, style = MaterialTheme.typography.bodyLarge, color = LukaMuted)
    Spacer(Modifier.height(18.dp))
}
