package net.bedev.registeruser.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [UserEntity::class], version = 1)
abstract class RoomRegisterDb () :RoomDatabase(){
    abstract fun userDao(): UserDao?

    companion object{
        private var INSTANCE: RoomRegisterDb? = null

        fun getAppDatabase (context: Context) : RoomRegisterDb? {
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder<RoomRegisterDb>(
                    context.applicationContext, RoomRegisterDb::class.java, "RegisterDB"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }
    }
}