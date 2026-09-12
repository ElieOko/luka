package elieoko.mobile.luka.core

import platform.UIKit.UIDevice

actual fun createDeviceSerial(): DeviceSerial = DeviceSerial {
    val vendor = UIDevice.currentDevice.identifierForVendor?.UUIDString ?: "unknown"
    "luka-ios-$vendor"
}
