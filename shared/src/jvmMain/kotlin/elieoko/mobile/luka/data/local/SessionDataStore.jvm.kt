package elieoko.mobile.luka.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

actual fun createSessionDataStore(): DataStore<Preferences> {
    val dir = File(System.getProperty("user.home"), ".luka")
    dir.mkdirs()
    return createDataStore { File(dir, DATASTORE_FILE).absolutePath }
}
