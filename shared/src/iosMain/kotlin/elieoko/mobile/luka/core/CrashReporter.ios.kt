package elieoko.mobile.luka.core

actual fun createCrashReporter(config: AppConfig): CrashReporter = NoOpCrashReporter

private object NoOpCrashReporter : CrashReporter {
    override fun initialize() = Unit
    override fun capture(throwable: Throwable) = Unit
    override fun breadcrumb(message: String) = Unit
}
