package elieoko.mobile.luka.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import elieoko.mobile.luka.AndroidRuntime
import java.io.File

actual fun createSessionDataStore(): DataStore<Preferences> {
    val file = File(AndroidRuntime.context.applicationContext.filesDir, DATASTORE_FILE)
    return createDataStore { file.absolutePath }
}
