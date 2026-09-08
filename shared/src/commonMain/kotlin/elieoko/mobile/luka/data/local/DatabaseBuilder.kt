package elieoko.mobile.luka.data.local

import androidx.room.RoomDatabase

expect fun createLukaDatabaseBuilder(): RoomDatabase.Builder<LukaDatabase>
