package elieoko.mobile.luka.presentation.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import elieoko.mobile.luka.core.CongoMno
import elieoko.mobile.luka.core.PhoneNumbers
import elieoko.mobile.luka.domain.model.LukaPlans
import elieoko.mobile.luka.domain.model.PaymentCurrency
import elieoko.mobile.luka.domain.model.PaymentMethod
import elieoko.mobile.luka.domain.model.SubscriptionPlan
import elieoko.mobile.luka.domain.repository.PaymentRepository
import elieoko.mobile.luka.domain.repository.SessionRepository
import elieoko.mobile.luka.domain.usecase.SelectPlanUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SubscriptionUiState(
    val plans: List<SubscriptionPlan> = LukaPlans.paid,
    val selectedApiId: Long = 1,
    val currencies: List<PaymentCurrency> = PaymentCurrency.fallback,
    val currencyCode: String = "USD",
    val method: PaymentMethod = PaymentMethod.MobileMoney,
    val phoneNational: String = "",
    val loadingCatalog: Boolean = true,
    val paying: Boolean = false,
    val error: String? = null,
    val info: String? = null,
    val checkoutUrl: String? = null,
) {
    val plan: SubscriptionPlan get() = plans.firstOrNull { it.apiId == selectedApiId } ?: LukaPlans.byApiId(selectedApiId)
    val currency: PaymentCurrency
        get() = currencies.firstOrNull { it.code.equals(currencyCode, true) }
            ?: currencies.firstOrNull()
            ?: PaymentCurrency("USD", "Dollar US", 1.0)
    val network: CongoMno? get() = CongoMno.detect(phoneNational)
    val formattedPrice: String get() = currency.format(plan.usdAmount)
    val canPay: Boolean get() = !paying && !loadingCatalog && phoneNational.length == 9
}

class SubscriptionViewModel(
    private val payments: PaymentRepository,
    private val sessions: SessionRepository,
    private val selectPlan: SelectPlanUseCase,
) : ViewModel() {
    private val draft = MutableStateFlow(SubscriptionUiState())

    val state: StateFlow<SubscriptionUiState> = combine(sessions.session, draft) { session, local ->
        val fromProfile = CongoMno.nationalDigits(session?.profile?.identifier?.value.orEmpty())
        local.copy(phoneNational = local.phoneNational.ifBlank { fromProfile })
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SubscriptionUiState())

    init {
        viewModelScope.launch {
            val plans = runCatching { payments.plans() }.getOrDefault(LukaPlans.paid)
            val currencies = runCatching { payments.currencies() }.getOrDefault(PaymentCurrency.fallback)
            draft.update {
                it.copy(
                    loadingCatalog = false,
                    plans = plans.ifEmpty { LukaPlans.paid },
                    currencies = currencies.ifEmpty { PaymentCurrency.fallback },
                    selectedApiId = it.selectedApiId.takeIf { id -> plans.any { plan -> plan.apiId == id } }
                        ?: plans.firstOrNull()?.apiId
                        ?: 1,
                    currencyCode = it.currencyCode.takeIf { code -> currencies.any { c -> c.code.equals(code, true) } }
                        ?: currencies.firstOrNull()?.code
                        ?: "USD",
                )
            }
        }
    }

    fun selectPlanTab(apiId: Long) = draft.update {
        it.copy(selectedApiId = apiId, error = null, info = null, checkoutUrl = null)
    }

    fun selectCurrency(code: String) = draft.update { it.copy(currencyCode = code, error = null) }

    fun selectMethod(method: PaymentMethod) = draft.update { it.copy(method = method, error = null) }

    fun onPhone(value: String) = draft.update {
        it.copy(phoneNational = CongoMno.nationalDigits(value).take(9), error = null)
    }

    fun pay() {
        viewModelScope.launch {
            val current = state.value
            if (sessions.current() == null) {
                draft.update { it.copy(error = "Connecte-toi pour payer.") }
                return@launch
            }
            val phone = runCatching { PhoneNumbers.normalize("243${current.phoneNational}") }.getOrNull()
            if (phone == null || current.phoneNational.length != 9) {
                draft.update { it.copy(error = "Indique un numéro congolais de 9 chiffres.") }
                return@launch
            }
            draft.update { it.copy(paying = true, error = null, info = null, checkoutUrl = null) }
            runCatching {
                when (current.method) {
                    PaymentMethod.MobileMoney ->
                        payments.payMobileMoney(current.plan.apiId, current.currency.code, phone)
                    PaymentMethod.Card ->
                        payments.payCard(current.plan.apiId, current.currency.code, phone)
                }
            }.onSuccess { result ->
                if (result.paymentAccepted) {
                    runCatching { selectPlan(current.plan.id, emptyList()) }
                }
                draft.update {
                    it.copy(
                        paying = false,
                        info = result.message?.takeIf { msg -> msg.isNotBlank() }
                            ?: if (result.paymentAccepted) "Paiement accepté." else "Paiement envoyé.",
                        checkoutUrl = result.url?.takeIf { url -> url.isNotBlank() },
                    )
                }
            }.onFailure { error ->
                draft.update { it.copy(paying = false, error = error.message) }
            }
        }
    }

    fun consumeCheckoutUrl() = draft.update { it.copy(checkoutUrl = null) }
}
