package elieoko.mobile.luka.core

import elieoko.mobile.luka.data.platform.SentryCrashReporter

actual fun createCrashReporter(config: AppConfig): CrashReporter = SentryCrashReporter(config)
