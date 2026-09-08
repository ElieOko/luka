package elieoko.mobile.luka.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import platform.Foundation.NSHomeDirectory

actual fun createSessionDataStore(): DataStore<Preferences> {
    val path = NSHomeDirectory() + "/$DATASTORE_FILE"
    return createDataStore { path }
}
