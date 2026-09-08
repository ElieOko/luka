package elieoko.mobile.luka.presentation.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.presentation.components.LukaLogo
import elieoko.mobile.luka.presentation.components.LukaPrimaryButton
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthScreen(viewModel: AuthViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            LukaLogo()
            Spacer(Modifier.height(28.dp))
            Text(
                if (state.step == AuthUiState.Step.Identifier) "Entre sans mot de passe." else "Confirme que c’est toi.",
                style = MaterialTheme.typography.headlineLarge,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                if (state.step == AuthUiState.Step.Identifier)
                    "Numéro Congolais ou e-mail. Comme WhatsApp, tu restes connecté même après avoir quitté Luka."
                else
                    "Code envoyé à ${state.identifier?.value}. Démo : 123456",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(28.dp))
            if (state.step == AuthUiState.Step.Identifier) {
                OutlinedTextField(
                    value = state.input,
                    onValueChange = viewModel::onInput,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Téléphone ou e-mail") },
                    placeholder = { Text("+243 81 000 0000") },
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                )
            } else {
                OutlinedTextField(
                    value = state.otp,
                    onValueChange = viewModel::onOtp,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Code à 6 chiffres") },
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                )
            }
            AnimatedVisibility(state.error != null) {
                Text(
                    state.error.orEmpty(),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 12.dp),
                )
            }
        }
        LukaPrimaryButton(
            text = when {
                state.loading -> "Un instant…"
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
