package elieoko.mobile.luka.core

import android.provider.Settings
import elieoko.mobile.luka.AndroidRuntime

actual fun createDeviceSerial(): DeviceSerial = DeviceSerial {
    val androidId = Settings.Secure.getString(
        AndroidRuntime.context.contentResolver,
        Settings.Secure.ANDROID_ID,
    ).orEmpty().ifBlank { "unknown" }
    "luka-and-$androidId"
}
