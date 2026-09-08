package elieoko.mobile.luka.core

actual fun createPushNotifier(config: AppConfig): PushNotifier = object : PushNotifier {
    override fun initialize() = Unit
    override fun login(externalId: String) = Unit
    override fun logout() = Unit
}
