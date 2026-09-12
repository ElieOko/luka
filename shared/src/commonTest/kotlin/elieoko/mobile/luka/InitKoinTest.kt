package elieoko.mobile.luka

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
}
