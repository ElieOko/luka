package elieoko.mobile.luka.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual fun createLukaDatabaseBuilder(): RoomDatabase.Builder<LukaDatabase> {
    val dir = File(System.getProperty("user.home"), ".luka")
    dir.mkdirs()
    return Room.databaseBuilder<LukaDatabase>(
        name = File(dir, "luka.db").absolutePath,
    )
}
