package elieoko.mobile.luka

class JvmPlatform : Platform {
    override val name: String = "Desktop ${System.getProperty("os.name")}"
}

actual fun getPlatform(): Platform = JvmPlatform()
