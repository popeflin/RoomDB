package com.dewabrata.simpledatabase.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDAO {

    @Query("SELECT * FROM user")
    fun getAllUser(): List<UserEntity>

    @Insert
     fun insertUser(user: UserEntity) :Long

    @Update
     fun updateUser(user: UserEntity)

    @Delete
     fun deleteUser(user: UserEntity)

}