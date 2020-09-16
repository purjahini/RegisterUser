package net.bedev.registeruser.database

import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM userinfo ORDER BY id DESC ")
    fun getAllUserInfo(): List<UserEntity>?

    @Insert
    fun insertUser (User : UserEntity?)

    @Delete
    fun deleteUser (User: UserEntity?)

    @Update
    fun updateUser (User: UserEntity?)

}