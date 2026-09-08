package elieoko.mobile.luka.domain.model

data class Country(
    val code: String,
    val name: String,
    val flag: String,
    val offersEnabled: Boolean,
)

data class Region(
    val id: String,
    val name: String,
    val cityHint: String,
)

object CongoCatalog {
    val rdc = Country(
        code = "CD",
        name = "République Démocratique du Congo",
        flag = "🇨🇩",
        offersEnabled = true,
    )

    val regions = listOf(
        Region("kinshasa", "Kinshasa", "Gombe · Limete · Ngaliema"),
        Region("haut-katanga", "Lubumbashi", "Haut-Katanga"),
        Region("nord-kivu", "Goma", "Nord-Kivu"),
        Region("sud-kivu", "Bukavu", "Sud-Kivu"),
        Region("tshopo", "Kisangani", "Tshopo"),
        Region("kongo-central", "Matadi", "Kongo-Central"),
        Region("lualaba", "Kolwezi", "Lualaba"),
        Region("kasai-oriental", "Mbuji-Mayi", "Kasaï-Oriental"),
        Region("kasai-central", "Kananga", "Kasaï-Central"),
        Region("equateur", "Mbandaka", "Équateur"),
    )
}
