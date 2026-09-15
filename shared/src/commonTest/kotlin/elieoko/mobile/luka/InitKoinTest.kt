package elieoko.mobile.luka

import elieoko.mobile.luka.core.AppConfig
import elieoko.mobile.luka.core.createCrashReporter
import elieoko.mobile.luka.di.initKoin
import org.koin.core.context.stopKoin
import org.koin.mp.KoinPlatform
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class InitKoinTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun initKoinCanBeCalledTwice() {
        initKoin()
        initKoin()
        assertNotNull(KoinPlatform.getKoinOrNull())
    }

    @Test
    fun crashReporterIsSafeWithoutDsn() {
        val reporter = createCrashReporter(AppConfig())
        reporter.initialize()
        reporter.breadcrumb("ok")
        reporter.capture(IllegalStateException("test"))
    }
}
