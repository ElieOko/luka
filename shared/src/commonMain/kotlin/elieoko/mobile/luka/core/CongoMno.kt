package elieoko.mobile.luka.core

enum class CongoMno(
    val label: String,
    val prefixes: Set<String>,
) {
    Vodacom("Vodacom", setOf("81", "82", "83")),
    Orange("Orange", setOf("84", "85", "89")),
    Airtel("Airtel", setOf("97", "98", "99")),
    Africell("Africell", setOf("90", "91")),
    ;

    companion object {
        fun detect(raw: String): CongoMno? {
            val national = nationalDigits(raw)
            if (national.length < 2) return null
            val prefix = national.take(2)
            return entries.firstOrNull { prefix in it.prefixes }
        }

        fun nationalDigits(raw: String): String {
            val digits = raw.filter { it.isDigit() }
            return when {
                digits.startsWith("243") && digits.length >= 5 -> digits.drop(3)
                digits.startsWith("0") -> digits.drop(1)
                else -> digits
            }
        }
    }
}
