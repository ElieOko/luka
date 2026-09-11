package elieoko.mobile.luka.presentation.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.presentation.components.BottomCtaBar
import elieoko.mobile.luka.presentation.components.LukaLogo
import elieoko.mobile.luka.presentation.components.LukaPrimaryButton
import elieoko.mobile.luka.presentation.components.OtpBoxes
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthScreen(viewModel: AuthViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
    ) {
        Column(Modifier.weight(1f).padding(24.dp)) {
            LukaLogo()
            Spacer(Modifier.height(28.dp))
            Text(
                if (state.step == AuthUiState.Step.Identifier) {
                    if (state.newAccount) "Crée ton compte." else "Entre sans mot de passe."
                } else {
                    "Confirme que c’est toi."
                },
                style = MaterialTheme.typography.headlineLarge,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                if (state.step == AuthUiState.Step.Identifier)
                    "Numéro congolais. Un code SMS, puis tu restes connecté."
                else
                    "Code envoyé par SMS à ${state.identifier?.value}.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(28.dp))
            if (state.step == AuthUiState.Step.Identifier) {
                OutlinedTextField(
                    value = state.input,
                    onValueChange = viewModel::onInput,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Téléphone") },
                    placeholder = { Text("+243 81 000 0000") },
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                )
                TextButton(onClick = viewModel::toggleNewAccount) {
                    Text(
                        if (state.newAccount) "Déjà inscrit ? Se connecter" else "Pas encore de compte ? Créer un compte",
                    )
                }
            } else {
                Text("Code à 6 chiffres", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                OtpBoxes(
                    value = state.otp,
                    onValueChange = viewModel::onOtp,
                )
                TextButton(onClick = viewModel::resend, enabled = !state.loading) {
                    Text("Renvoyer le code")
                }
            }
            AnimatedVisibility(state.info != null && state.error == null) {
                Text(state.info.orEmpty(), color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 12.dp))
            }
            AnimatedVisibility(state.error != null) {
                Text(state.error.orEmpty(), color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 12.dp))
            }
        }
        BottomCtaBar {
            LukaPrimaryButton(
                text = when {
                    state.loading -> "Un instant…"
                    state.step == AuthUiState.Step.Identifier && state.newAccount -> "Créer le compte"
                    state.step == AuthUiState.Step.Identifier -> "Recevoir le code"
                    else -> "Continuer"
                },
                onClick = {
                    if (state.step == AuthUiState.Step.Identifier) viewModel.submitIdentifier() else viewModel.submitOtp()
                },
                enabled = !state.loading,
            )
        }
    }
}
