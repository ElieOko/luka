package elieoko.mobile.luka.core

import java.io.File
import java.util.UUID

actual fun createDeviceSerial(): DeviceSerial = DeviceSerial {
    val machine = File("/etc/machine-id").takeIf { it.isFile }?.readText()?.trim().orEmpty()
    if (machine.isNotBlank()) return@DeviceSerial "luka-jvm-$machine"
    val stored = File(System.getProperty("user.home"), ".luka/device_serial")
    stored.parentFile?.mkdirs()
    if (stored.isFile) {
        val existing = stored.readText().trim()
        if (existing.isNotBlank()) return@DeviceSerial existing
    }
    val generated = "luka-jvm-${UUID.randomUUID()}"
    stored.writeText(generated)
    generated
}
