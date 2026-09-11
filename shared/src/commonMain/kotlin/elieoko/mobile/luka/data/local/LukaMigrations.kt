package elieoko.mobile.luka.data.local

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

/** Schema unchanged; wipe locally-seeded fake catalog rows. */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("DELETE FROM ads")
        connection.execSQL("DELETE FROM news")
        connection.execSQL("DELETE FROM professionals")
        connection.execSQL("DELETE FROM orientation")
        connection.execSQL("DELETE FROM demand_stats")
        connection.execSQL("DELETE FROM offers")
        connection.execSQL("DELETE FROM meta")
    }
}
