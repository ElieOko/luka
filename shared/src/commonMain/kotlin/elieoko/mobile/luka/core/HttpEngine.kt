package elieoko.mobile.luka.core

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header

expect fun createHttpClient(): HttpClient

fun HttpClient.withDeviceSerial(serial: DeviceSerial): HttpClient {
    val value = serial.value().trim()
    if (value.isBlank()) return this
    return config {
        install(DefaultRequest) {
            header("build_serial", value)
            header("build-serial", value)
        }
    }
}
