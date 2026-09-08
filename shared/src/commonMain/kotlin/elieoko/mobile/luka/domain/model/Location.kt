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

data class City(
    val id: String,
    val name: String,
    val regionId: String,
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
        Region("haut-katanga", "Haut-Katanga", "Lubumbashi"),
        Region("nord-kivu", "Nord-Kivu", "Goma"),
        Region("sud-kivu", "Sud-Kivu", "Bukavu"),
        Region("tshopo", "Tshopo", "Kisangani"),
        Region("kongo-central", "Kongo-Central", "Matadi"),
        Region("lualaba", "Lualaba", "Kolwezi"),
        Region("kasai-oriental", "Kasaï-Oriental", "Mbuji-Mayi"),
        Region("kasai-central", "Kasaï-Central", "Kananga"),
        Region("equateur", "Équateur", "Mbandaka"),
    )

    val cities = listOf(
        City("gombe", "Gombe", "kinshasa"),
        City("limete", "Limete", "kinshasa"),
        City("ngaliema", "Ngaliema", "kinshasa"),
        City("kinshasa-centre", "Kinshasa", "kinshasa"),
        City("lubumbashi", "Lubumbashi", "haut-katanga"),
        City("likasi", "Likasi", "haut-katanga"),
        City("goma", "Goma", "nord-kivu"),
        City("butembo", "Butembo", "nord-kivu"),
        City("bukavu", "Bukavu", "sud-kivu"),
        City("kisangani", "Kisangani", "tshopo"),
        City("matadi", "Matadi", "kongo-central"),
        City("boma", "Boma", "kongo-central"),
        City("kolwezi", "Kolwezi", "lualaba"),
        City("mbuji-mayi", "Mbuji-Mayi", "kasai-oriental"),
        City("kananga", "Kananga", "kasai-central"),
        City("mbandaka", "Mbandaka", "equateur"),
    )

    fun citiesIn(regionId: String?) = if (regionId.isNullOrBlank()) cities else cities.filter { it.regionId == regionId }
}
