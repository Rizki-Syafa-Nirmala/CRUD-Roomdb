package com.example.roomdb_rizkisyafa_24.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Movie::class],
    version = 1
)

abstract class MovieDb : RoomDatabase(){
    abstract fun movieDao() : MovieDao
    companion object{
        @Volatile private var instance : MovieDb? = null
        private val Lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(Lock){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            MovieDb::class.java,
            "movie12345.db"
        ).build()
    }
}