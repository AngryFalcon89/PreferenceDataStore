package com.example.preferencedatastore.userPref

import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow


val USER_KEY= stringPreferencesKey("user_name")

interface UserPref {

    fun getName(): Flow<String>

    suspend fun saveName(name:String)

}