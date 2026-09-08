package elieoko.mobile.luka.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

actual fun createLukaDatabaseBuilder(): RoomDatabase.Builder<LukaDatabase> {
    val dbFilePath = NSHomeDirectory() + "/luka.db"
    return Room.databaseBuilder<LukaDatabase>(name = dbFilePath)
}
