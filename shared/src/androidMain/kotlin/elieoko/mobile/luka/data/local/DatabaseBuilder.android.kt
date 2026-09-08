package elieoko.mobile.luka.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import elieoko.mobile.luka.AndroidRuntime

actual fun createLukaDatabaseBuilder(): RoomDatabase.Builder<LukaDatabase> {
    val context = AndroidRuntime.context.applicationContext
    val dbFile = context.getDatabasePath("luka.db")
    return Room.databaseBuilder<LukaDatabase>(
        context = context,
        name = dbFile.absolutePath,
    )
}
