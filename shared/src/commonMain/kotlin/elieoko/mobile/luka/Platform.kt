package elieoko.mobile.luka

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform