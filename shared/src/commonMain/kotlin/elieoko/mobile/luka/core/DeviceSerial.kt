package elieoko.mobile.luka.core

/**
 * Identifiant d’appareil stable, unique par téléphone même après
 * réinstallation de l’app (ANDROID_ID / Keychain / machine-id).
 * Envoyé au backend dans le header `buildSerial`.
 */
fun interface DeviceSerial {
    fun value(): String
}

fun DeviceSerial.memoized(): DeviceSerial {
    val cached = value()
    return DeviceSerial { cached }
}

expect fun createDeviceSerial(): DeviceSerial

