package elieoko.mobile.luka.core

import android.content.Context
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel

class AndroidOneSignalNotifier(
    private val config: AppConfig,
    private val context: Context,
) : PushNotifier {
    override fun initialize() {
        val appId = config.oneSignalAppId
        if (appId.isBlank()) return
        OneSignal.Debug.logLevel = LogLevel.WARN
        OneSignal.initWithContext(context, appId)
    }

    override fun login(externalId: String) {
        if (config.oneSignalAppId.isBlank()) return
        OneSignal.login(externalId)
    }

    override fun logout() {
        if (config.oneSignalAppId.isBlank()) return
        OneSignal.logout()
    }
}
