package net.bedev.registeruser

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import net.bedev.registeruser.database.RoomRegisterDb
import net.bedev.registeruser.database.UserEntity

class UserActivityViewModel (app : Application): AndroidViewModel(app) {
    var allUsers: MutableLiveData<List<UserEntity>> = MutableLiveData()

    init {
        getAllUsers()

    }
    fun getAllUsersObservers(): MutableLiveData<List<UserEntity>> {
        return allUsers
    }

     fun getAllUsers() {
        val userDao = RoomRegisterDb.getAppDatabase((getApplication()))?.userDao()
         val list = userDao?.getAllUserInfo()
         allUsers.postValue(list)
    }

    fun insertUserInfo (entity: UserEntity){
        val userDao = RoomRegisterDb.getAppDatabase(getApplication())?.userDao()
        userDao?.insertUser(entity)
    }
    fun updateUserInfo(entity: UserEntity){
        val userDao = RoomRegisterDb.getAppDatabase(getApplication())?.userDao()
        userDao?.updateUser(entity)
        getAllUsers()
    }
    fun deleteUserInfo (entity: UserEntity){
        val userDao = RoomRegisterDb.getAppDatabase(getApplication())?.userDao()
        userDao?.deleteUser(entity)
        getAllUsers()
    }
}