package com.dewabrata.simpledatabase.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dewabrata.simpledatabase.MainActivity

@Database(entities = [UserEntity::class], version = 1)
abstract  class UserDatabase: RoomDatabase(){
    abstract fun userDao(): UserDAO

    companion object{
        @Volatile
        private var INSTANCE: UserDatabase? = null
        fun getInstance(context:Context):UserDatabase{
            synchronized(this){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        "user.db"
                    ).build()
                }
                return INSTANCE as UserDatabase
            }

        }

    }


}