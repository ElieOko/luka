package elieoko.mobile.luka.data.platform

import elieoko.mobile.luka.core.AppConfig
import elieoko.mobile.luka.core.CrashReporter
import io.sentry.kotlin.multiplatform.Sentry

class SentryCrashReporter(private val config: AppConfig) : CrashReporter {
    override fun initialize() {
        val dsn = config.sentryDsn
        if (dsn.isBlank()) return
        Sentry.init { options ->
            options.dsn = dsn
        }
    }

    override fun capture(throwable: Throwable) {
        if (config.sentryDsn.isBlank()) return
        Sentry.captureException(throwable)
    }

    override fun breadcrumb(message: String) {
        if (config.sentryDsn.isBlank()) return
        Sentry.captureMessage(message)
    }
}
