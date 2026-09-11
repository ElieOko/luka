package elieoko.mobile.luka.core

object PhoneNumbers {
    fun normalize(raw: String): String {
        val digits = raw.filter { it.isDigit() }
        val national = when {
            digits.startsWith("243") && digits.length >= 12 -> digits
            digits.startsWith("0") && digits.length >= 10 -> "243${digits.drop(1)}"
            digits.length in 9..10 && (digits.startsWith("8") || digits.startsWith("9")) -> "243$digits"
            else -> digits
        }
        require(national.length >= 12) { "Numéro trop court. Exemple : +243 81 000 0000" }
        return "+$national"
    }
}
