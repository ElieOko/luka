package elieoko.mobile.luka

import android.app.Application
import elieoko.mobile.luka.di.initKoin

class LukaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidRuntime.context = this
        initKoin()
    }
}
