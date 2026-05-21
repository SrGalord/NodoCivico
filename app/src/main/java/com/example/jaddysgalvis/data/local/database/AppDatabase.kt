package com.example.jaddysgalvis.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.jaddysgalvis.data.local.dao.ReportDao
import com.example.jaddysgalvis.data.local.dao.UserDao
import com.example.jaddysgalvis.data.local.entity.ReportEntity
import com.example.jaddysgalvis.data.local.entity.UserEntity

@Database(
    entities = [
        ReportEntity::class,
        UserEntity::class
    ],
    version = 5, // 🔥 IMPORTANTE: subido por cambios en schema
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun reportDao(): ReportDao
    abstract fun userDao(): UserDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reports_db"
                )
                    // 🔥 borra datos viejos automáticamente en dev
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}