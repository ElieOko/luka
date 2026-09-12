package elieoko.mobile.luka.domain.repository

import elieoko.mobile.luka.domain.model.PaymentCheckoutResult
import elieoko.mobile.luka.domain.model.PaymentCurrency
import elieoko.mobile.luka.domain.model.SubscriptionPlan

interface PaymentRepository {
    suspend fun plans(): List<SubscriptionPlan>
    suspend fun currencies(): List<PaymentCurrency>
    suspend fun payMobileMoney(abonnementId: Long, devise: String, phone: String): PaymentCheckoutResult
    suspend fun payCard(abonnementId: Long, devise: String, phone: String): PaymentCheckoutResult
}
