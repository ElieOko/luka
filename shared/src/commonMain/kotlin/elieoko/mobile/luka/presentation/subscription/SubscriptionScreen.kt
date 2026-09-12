package elieoko.mobile.luka.presentation.subscription

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    val scroll = rememberScrollState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.ime.union(WindowInsets.navigationBars))
            .verticalScroll(scroll)
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            "Étudiant pour s’orienter. Pro pour l’emploi.",
            color = LukaMuted,
            style = MaterialTheme.typography.bodySmall,
        )

        TabRow(
            selectedTabIndex = tabIndex,
            containerColor = Color.Transparent,
            contentColor = LukaRed,
            indicator = { positions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(positions[tabIndex]),
                    color = LukaRed,
                    height = 2.dp,
                )
            },
        ) {
            Tab(
                selected = tabIndex == 0,
                onClick = { onSelectApiId(1L) },
                selectedContentColor = LukaRed,
                unselectedContentColor = LukaMuted,
                text = { Text("Étudiant", style = MaterialTheme.typography.labelLarge) },
            )
            Tab(
                selected = tabIndex == 1,
                onClick = { onSelectApiId(2L) },
                selectedContentColor = LukaRed,
                unselectedContentColor = LukaMuted,
                text = { Text("Professionnel", style = MaterialTheme.typography.labelLarge) },
            )
        }

        if (state.loadingCatalog) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally).size(22.dp),
                color = LukaRed,
                strokeWidth = 2.dp,
            )
        }

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = BorderStroke(1.dp, LukaRed),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(Modifier.padding(horizontal = 12.dp, vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(plan.name, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleMedium, color = LukaInk)
                    Spacer(Modifier.weight(1f))
                    Text(
                        "${state.formattedPrice} ${plan.period}".trim(),
                        color = LukaRed,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                plan.perks.forEach { perk ->
                    Text("·  $perk", color = LukaInk.copy(alpha = 0.75f), style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            state.currencies.forEach { currency ->
                val selected = currency.code.equals(state.currency.code, true)
                Surface(
                    onClick = { onSelectCurrency(currency) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = if (selected) LukaMist else Color.White,
                    border = BorderStroke(
                        width = if (selected) 1.5.dp else 1.dp,
                        color = if (selected) LukaRed else Color(0xFFE8D6D7),
                    ),
                ) {
                    Column(Modifier.padding(horizontal = 10.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            if (currency.code.equals("CDF", true)) "FC" else "USD",
                            style = MaterialTheme.typography.labelLarge,
                            color = LukaInk,
                        )
                        Text(
                            currency.format(plan.usdAmount),
                            color = LukaRed,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        if (currency.code.equals("CDF", true)) {
                            Text(
                                "1 $ = ${currency.tauxLocal.toInt()} FC",
                                style = MaterialTheme.typography.bodySmall,
                                color = LukaMuted,
                            )
                        }
                    }
                }
            }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PaymentChoiceCard(
                title = "Mobile Money",
                icon = Icons.Outlined.PhoneAndroid,
                selected = state.method == PaymentMethod.MobileMoney,
                onClick = { onSelectMethod(PaymentMethod.MobileMoney) },
                modifier = Modifier.weight(1f),
            )
            PaymentChoiceCard(
                title = "Carte",
                icon = Icons.Outlined.CreditCard,
                selected = state.method == PaymentMethod.Card,
                onClick = { onSelectMethod(PaymentMethod.Card) },
                modifier = Modifier.weight(1f),
            )
        }

        AnimatedVisibility(visible = state.method == PaymentMethod.MobileMoney) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CongoMno.entries.forEach { mno ->
                    OperatorLogo(
                        operator = mno,
                        selected = mno == operator,
                        modifier = Modifier.size(36.dp),
                    )
                }
                if (operator != null) {
                    Text(
                        operator.label,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = LukaInk,
                    )
                }
            }
        }

        OutlinedTextField(
            value = state.phoneNational,
            onValueChange = onPhoneChange,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focus ->
                    if (focus.isFocused) {
                        scope.launch {
                            delay(280)
                            scroll.animateScrollTo(scroll.maxValue)
                        }
                    }
                },
            prefix = {
                Text(
                    "+243",
                    fontWeight = FontWeight.Bold,
                    color = LukaInk,
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            placeholder = { Text("827824163", style = MaterialTheme.typography.bodyMedium) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            textStyle = MaterialTheme.typography.bodyMedium,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LukaRed,
                cursorColor = LukaRed,
            ),
        )

        AnimatedVisibility(visible = state.method == PaymentMethod.Card) {
            Text(
                "Tu seras redirigé vers FlexPay.",
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
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun OperatorLogo(
    operator: CongoMno,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(10.dp)
    Surface(
        modifier = modifier,
        shape = shape,
        color = Color.White,
        border = BorderStroke(
            width = if (selected) 1.5.dp else 1.dp,
            color = if (selected) LukaRed else Color(0xFFE8D6D7),
        ),
    ) {
        val inset = when (operator) {
            CongoMno.Orange, CongoMno.Vodacom -> 0.dp
            else -> 4.dp
        }
        Image(
            painter = painterResource(operator.image()),
            contentDescription = operator.label,
            modifier = Modifier.fillMaxSize().padding(inset),
            contentScale = ContentScale.Fit,
        )
    }
}

@Composable
private fun PaymentChoiceCard(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = if (selected) LukaMist else Color.White,
        border = BorderStroke(
            width = if (selected) 1.5.dp else 1.dp,
            color = if (selected) LukaRed else Color(0xFFE8D6D7),
        ),
    ) {
        Row(
            Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (selected) LukaRed else LukaMuted,
                modifier = Modifier.size(18.dp),
            )
            Text(title, fontWeight = FontWeight.SemiBold, color = LukaInk, style = MaterialTheme.typography.bodySmall)
        }
    }
}
