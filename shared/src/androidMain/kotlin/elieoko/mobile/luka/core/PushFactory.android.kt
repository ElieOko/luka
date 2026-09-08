package elieoko.mobile.luka.core

import elieoko.mobile.luka.AndroidRuntime

actual fun createPushNotifier(config: AppConfig): PushNotifier = AndroidOneSignalNotifier(config, AndroidRuntime.context)
