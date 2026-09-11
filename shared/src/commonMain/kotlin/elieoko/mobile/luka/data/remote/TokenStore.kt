package elieoko.mobile.luka.data.remote

/**
 * In-memory JWT holder used by [LukaApi] on each request.
 * [elieoko.mobile.luka.data.repository.SessionRepositoryImpl] keeps it in sync with DataStore.
 */
class TokenStore {
    @Volatile
    var accessToken: String? = null

    @Volatile
    var refreshToken: String? = null

    fun set(access: String?, refresh: String?) {
        accessToken = access?.takeIf { it.isNotBlank() }
        refreshToken = refresh?.takeIf { it.isNotBlank() }
    }

    fun clear() {
        accessToken = null
        refreshToken = null
    }
}
