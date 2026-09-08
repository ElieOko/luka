package elieoko.mobile.luka.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okio.Path.Companion.toPath

internal const val DATASTORE_FILE = "luka_session.preferences_pb"

fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        scope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
        produceFile = { producePath().toPath() },
    )

expect fun createSessionDataStore(): DataStore<Preferences>
