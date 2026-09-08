package elieoko.mobile.luka.core

data class CvDocument(
    val fileName: String,
    val mimeType: String,
)

fun isSupportedCv(fileName: String): Boolean {
    val name = fileName.lowercase()
    return name.endsWith(".pdf") || name.endsWith(".doc") || name.endsWith(".docx")
}

fun mimeForCv(fileName: String): String = when {
    fileName.lowercase().endsWith(".pdf") -> "application/pdf"
    fileName.lowercase().endsWith(".docx") -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    else -> "application/msword"
}
