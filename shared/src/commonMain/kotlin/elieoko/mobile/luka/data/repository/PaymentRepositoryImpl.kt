package elieoko.mobile.luka.data.repository

import elieoko.mobile.luka.data.remote.LukaApi
import elieoko.mobile.luka.data.remote.dto.FlexPaymentResponse
import elieoko.mobile.luka.domain.model.LukaPlans
import elieoko.mobile.luka.domain.model.PaymentCheckoutResult
import elieoko.mobile.luka.domain.model.PaymentCurrency
import elieoko.mobile.luka.domain.model.SubscriptionPlan
import elieoko.mobile.luka.domain.repository.PaymentRepository

class PaymentRepositoryImpl(
    private val api: LukaApi,
) : PaymentRepository {
    override suspend fun plans(): List<SubscriptionPlan> {
        val remote = runCatching { api.listAbonnements() }.getOrDefault(emptyList())
        if (remote.isEmpty()) return LukaPlans.paid
        return remote.filter { it.active }.map { dto ->
            val local = LukaPlans.byApiId(dto.id)
            local.copy(
                apiId = dto.id,
                name = dto.name.ifBlank { local.name }
                    .removePrefix("Abonnement ")
                    .replaceFirstChar { if (it.isLowerCase()) it.uppercase() else it.toString() },
                usdAmount = dto.amountUsd.takeIf { it > 0 } ?: local.usdAmount,
                priceLabel = "${(dto.amountUsd.takeIf { it > 0 } ?: local.usdAmount).toInt()} $",
            )
        }.ifEmpty { LukaPlans.paid }
    }

    override suspend fun currencies(): List<PaymentCurrency> {
        val remote = runCatching { api.listDevises() }.getOrDefault(emptyList())
        if (remote.isEmpty()) return PaymentCurrency.fallback
        return remote.map { PaymentCurrency(code = it.code, name = it.name, tauxLocal = it.tauxLocal) }
    }

    override suspend fun payMobileMoney(abonnementId: Long, devise: String, phone: String): PaymentCheckoutResult =
        api.payMobileMoney(abonnementId, devise, phone).toDomain()

    override suspend fun payCard(abonnementId: Long, devise: String, phone: String): PaymentCheckoutResult =
        api.payWithCard(abonnementId, devise, phone).toDomain()
}

private fun FlexPaymentResponse.toDomain() = PaymentCheckoutResult(
    message = message,
    url = url,
    paymentAccepted = paymentAccepted,
    orderNumber = orderNumber,
)
