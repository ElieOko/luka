package elieoko.mobile.luka.core

/**
 * Identifiant d’appareil stable, unique par téléphone même après
 * réinstallation de l’app (ANDROID_ID / Keychain / machine-id).
 * Envoyé au backend dans le header `build_serial`.
 */
fun interface DeviceSerial {
    fun value(): String
}

expect fun createDeviceSerial(): DeviceSerial
