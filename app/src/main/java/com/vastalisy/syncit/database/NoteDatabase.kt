package com.vastalisy.syncit.database

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vastalisy.syncit.LOCAL_DB_NAME
import com.vastalisy.syncit.dao.NoteDao
import com.vastalisy.syncit.dao.NoteOperationDao
import com.vastalisy.syncit.database.feature_encryption.DatabasePreference
import com.vastalisy.syncit.model.Note
import com.vastalisy.syncit.model.NoteOperation
import net.sqlcipher.database.SQLiteDatabase

@Database(entities = [Note::class, NoteOperation::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun noteOpDao(): NoteOperationDao

    companion object {
        private lateinit var database: NoteDatabase

        fun getInstance(context: Context): NoteDatabase {
            return initDb(context)
        }

        fun getInstance(application: Application): NoteDatabase {
            return if (::database.isInitialized) {
                database
            } else {
                initDb(application)
            }
        }

        private fun initDb(context: Context): NoteDatabase {
            return Room.databaseBuilder(context, NoteDatabase::class.java, LOCAL_DB_NAME)
                .build()
//                .openHelperFactory(SupportFactory(getPassPhrase(application)))
//                .build()
        }

        private fun initDb(appContext: Application): NoteDatabase {
            return Room.databaseBuilder(appContext, NoteDatabase::class.java, LOCAL_DB_NAME)
                .build()
//                .openHelperFactory(SupportFactory(getPassPhrase(application)))
//                .build()
        }

        private fun getPassPhrase(app: Application): ByteArray {
            val key =
                DatabasePreference(app).getKey() ?: throw RuntimeException("NULL CIPHER DB KEY")
            return SQLiteDatabase.getBytes(key.toCharArray())
        }

    }

}