package elieoko.mobile.luka.core

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.parameter

expect fun createHttpClient(): HttpClient

fun HttpClient.withDeviceSerial(serial: DeviceSerial): HttpClient {
    val value = serial.value().trim()
    if (value.isBlank()) return this
    return config {
        install(DefaultRequest) {
            header("buildSerial", value)
            url.parameters.append("buildSerial", value)
        }
    }
}

fun HttpRequestBuilder.applyDeviceSerialHeaders(value: String) {
    val serial = value.trim()
    if (serial.isBlank()) return
    header("buildSerial", serial)
    parameter("buildSerial", serial)
}
