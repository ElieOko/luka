package elieoko.mobile.luka.domain.model

data class PaymentCurrency(
    val code: String,
    val name: String,
    val tauxLocal: Double,
) {
    fun amountFor(usd: Double): Double = usd * tauxLocal

    fun format(usd: Double): String {
        val local = amountFor(usd)
        return when (code.uppercase()) {
            "USD" -> "${trimAmount(local)} $"
            "CDF" -> "${local.toLong()} FC"
            else -> "${trimAmount(local)} $code"
        }
    }

    private fun trimAmount(value: Double): String =
        if (value % 1.0 == 0.0) value.toLong().toString() else value.toString()

    companion object {
        val Usd = PaymentCurrency("USD", "Dollar US", 1.0)
        val Cdf = PaymentCurrency("CDF", "Franc congolais", 2250.0)
        val fallback = listOf(Usd, Cdf)
    }
}

enum class PaymentMethod { MobileMoney, Card }

data class PaymentCheckoutResult(
    val message: String? = null,
    val url: String? = null,
    val paymentAccepted: Boolean = false,
    val orderNumber: String? = null,
)
