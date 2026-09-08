package elieoko.mobile.luka.core

interface PushNotifier {
    fun initialize()
    fun login(externalId: String)
    fun logout()
}
