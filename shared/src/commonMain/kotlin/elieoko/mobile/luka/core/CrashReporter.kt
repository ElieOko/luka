package elieoko.mobile.luka.core

interface CrashReporter {
    fun initialize()
    fun capture(throwable: Throwable)
    fun breadcrumb(message: String)
}
