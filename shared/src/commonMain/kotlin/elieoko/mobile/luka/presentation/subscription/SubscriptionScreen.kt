package elieoko.mobile.luka.presentation.subscription

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import elieoko.mobile.luka.core.CongoMno
import elieoko.mobile.luka.domain.model.PaymentCurrency
import elieoko.mobile.luka.domain.model.PaymentMethod
import elieoko.mobile.luka.presentation.components.LukaPrimaryButton
import elieoko.mobile.luka.presentation.theme.LukaInk
import elieoko.mobile.luka.presentation.theme.LukaMist
import elieoko.mobile.luka.presentation.theme.LukaMuted
import elieoko.mobile.luka.presentation.theme.LukaRed
import elieoko.mobile.luka.presentation.theme.image
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SubscriptionScreen(
    viewModel: SubscriptionViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(state.checkoutUrl) {
        val url = state.checkoutUrl ?: return@LaunchedEffect
        runCatching { uriHandler.openUri(url) }
        viewModel.consumeCheckoutUrl()
    }

    SubscriptionCheckoutContent(
        state = state,
        onSelectApiId = viewModel::selectPlanTab,
        onSelectMethod = viewModel::selectMethod,
        onPhoneChange = viewModel::onPhone,
        onSelectCurrency = { viewModel.selectCurrency(it.code) },
        onPay = viewModel::pay,
    )
}

@Composable
fun SubscriptionCheckoutContent(
    state: SubscriptionUiState,
    onSelectApiId: (Long) -> Unit,
    onSelectMethod: (PaymentMethod) -> Unit,
    onPhoneChange: (String) -> Unit,
    onSelectCurrency: (PaymentCurrency) -> Unit,
    onPay: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val plan = state.plan
    val tabIndex = if (state.selectedApiId == 2L) 1 else 0
    val operator = CongoMno.detect(state.phoneNational)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            "Deux formules",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = LukaInk,
        )
        Text(
            "Étudiant pour s’orienter. Professionnel pour trouver un emploi.",
            color = LukaMuted,
            style = MaterialTheme.typography.bodyLarge,
        )

        TabRow(
            selectedTabIndex = tabIndex,
            containerColor = Color.Transparent,
            contentColor = LukaRed,
            indicator = { positions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(positions[tabIndex]),
                    color = LukaRed,
                )
            },
        ) {
            Tab(
                selected = tabIndex == 0,
                onClick = { onSelectApiId(1L) },
                selectedContentColor = LukaRed,
                unselectedContentColor = LukaMuted,
                text = { Text("Étudiant", fontWeight = FontWeight.SemiBold) },
            )
            Tab(
                selected = tabIndex == 1,
                onClick = { onSelectApiId(2L) },
                selectedContentColor = LukaRed,
                unselectedContentColor = LukaMuted,
                text = { Text("Professionnel", fontWeight = FontWeight.SemiBold) },
            )
        }

        if (state.loadingCatalog) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally).size(28.dp),
                color = LukaRed,
                strokeWidth = 2.dp,
            )
        }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = BorderStroke(1.5.dp, LukaRed),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(plan.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge, color = LukaInk)
                    Spacer(Modifier.weight(1f))
                    Text(
                        "${state.formattedPrice} ${plan.period}".trim(),
                        color = LukaRed,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                plan.perks.forEach { perk ->
                    Text("·  $perk", color = LukaInk.copy(alpha = 0.78f), style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Text("Devise", style = MaterialTheme.typography.titleMedium, color = LukaInk)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            state.currencies.forEach { currency ->
                val selected = currency.code.equals(state.currency.code, true)
                Surface(
                    onClick = { onSelectCurrency(currency) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    color = if (selected) LukaMist else Color.White,
                    border = BorderStroke(
                        width = if (selected) 1.5.dp else 1.dp,
                        color = if (selected) LukaRed else Color(0xFFE8D6D7),
                    ),
                ) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            if (currency.code.equals("CDF", true)) "Francs (FC)" else "Dollars (USD)",
                            fontWeight = FontWeight.SemiBold,
                            color = LukaInk,
                        )
                        Text(
                            currency.format(plan.usdAmount),
                            color = LukaRed,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            if (currency.code.equals("CDF", true)) {
                                "1 $ = ${currency.tauxLocal.toInt()} FC"
                            } else {
                                "Devise de référence"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = LukaMuted,
                        )
                    }
                }
            }
        }

        Text("Paiement", style = MaterialTheme.typography.titleMedium, color = LukaInk)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            PaymentChoiceCard(
                title = "Mobile Money",
                subtitle = "Vodacom, Orange, Airtel, Africell",
                icon = Icons.Outlined.PhoneAndroid,
                selected = state.method == PaymentMethod.MobileMoney,
                onClick = { onSelectMethod(PaymentMethod.MobileMoney) },
                modifier = Modifier.weight(1f),
            )
            PaymentChoiceCard(
                title = "Carte",
                subtitle = "Visa, Mastercard",
                icon = Icons.Outlined.CreditCard,
                selected = state.method == PaymentMethod.Card,
                onClick = { onSelectMethod(PaymentMethod.Card) },
                modifier = Modifier.weight(1f),
            )
        }

        Text("Numéro à débiter", style = MaterialTheme.typography.titleMedium, color = LukaInk)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = LukaMist,
                border = BorderStroke(1.dp, Color(0xFFE8D6D7)),
            ) {
                Text(
                    "+243",
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 18.dp),
                    fontWeight = FontWeight.Bold,
                    color = LukaInk,
                )
            }
            OutlinedTextField(
                value = state.phoneNational,
                onValueChange = onPhoneChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("827824163") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            )
        }

        AnimatedVisibility(visible = operator != null) {
            if (operator != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Image(
                        painter = painterResource(operator.image()),
                        contentDescription = operator.label,
                        modifier = Modifier.size(44.dp).clip(CircleShape),
                    )
                    Column {
                        Text(operator.label, fontWeight = FontWeight.SemiBold, color = LukaInk)
                        Text(
                            "Réseau détecté",
                            style = MaterialTheme.typography.bodySmall,
                            color = LukaMuted,
                        )
                    }
                }
            }
        }

        AnimatedVisibility(visible = state.method == PaymentMethod.Card) {
            Text(
                "Après validation, tu seras redirigé vers FlexPay pour payer par carte.",
                style = MaterialTheme.typography.bodySmall,
                color = LukaMuted,
            )
        }

        AnimatedVisibility(visible = state.error != null) {
            Text(state.error.orEmpty(), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        AnimatedVisibility(visible = state.info != null && state.error == null) {
            Text(state.info.orEmpty(), color = LukaInk, style = MaterialTheme.typography.bodySmall)
        }

        LukaPrimaryButton(
            text = when {
                state.paying -> "Un instant…"
                state.method == PaymentMethod.MobileMoney -> "Payer ${state.formattedPrice} en Mobile Money"
                else -> "Payer ${state.formattedPrice} par carte"
            },
            onClick = onPay,
            enabled = state.canPay,
        )
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun PaymentChoiceCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = if (selected) LukaMist else Color.White,
        border = BorderStroke(
            width = if (selected) 1.5.dp else 1.dp,
            color = if (selected) LukaRed else Color(0xFFE8D6D7),
        ),
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Icon(icon, contentDescription = null, tint = if (selected) LukaRed else LukaMuted)
            Text(title, fontWeight = FontWeight.SemiBold, color = LukaInk)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = LukaMuted)
        }
    }
}
