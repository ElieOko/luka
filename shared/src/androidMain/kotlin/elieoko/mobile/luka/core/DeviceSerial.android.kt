package elieoko.mobile.luka.core

import android.provider.Settings
import elieoko.mobile.luka.AndroidRuntime

actual fun createDeviceSerial(): DeviceSerial = DeviceSerial {
    val androidId = runCatching {
        Settings.Secure.getString(
            AndroidRuntime.context.contentResolver,
            Settings.Secure.ANDROID_ID,
        )
    }.getOrNull().orEmpty().ifBlank { "unknown" }
    "luka-and-$androidId"
}
