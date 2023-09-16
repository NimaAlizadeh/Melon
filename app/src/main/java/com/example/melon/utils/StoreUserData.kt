package com.example.melon.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StoreUserData @Inject constructor(@ApplicationContext val context: Context)
{
    companion object{
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.DATASTORE_NAME_USER)

        val userToken = stringPreferencesKey(Constants.DATASTORE_USER_TOKEN)
        val userId = stringPreferencesKey(Constants.DATASTORE_USER_ID)
        val followingsCollection = stringSetPreferencesKey(Constants.DATASTORE_USER_FOLLOWINGS_COLLECTION)
        val followersCollection = stringSetPreferencesKey(Constants.DATASTORE_USER_FOLLOWERS_COLLECTION)
        val followingsRequestedCollection = stringSetPreferencesKey(Constants.DATASTORE_USER_FOLLOWING_REQUESTED_COLLECTION)
        val followersRequestedCollection = stringSetPreferencesKey(Constants.DATASTORE_USER_FOLLOWERS_REQUESTED_COLLECTION)
    }

    suspend fun setUserToken(token: String)
    {
        //here we just save our string value in the datastore as a string (actually we are editing the value that have been saved before)
        context.dataStore.edit {
            it[userId] = token
        }
    }

    fun getUserToken() = context.dataStore.data.map {
        //give me the userToken from datastore and if it was empty just give me an empty string
        it[userId] ?: ""
    }

    suspend fun setUserId(token: String)
    {
        //here we just save our string value in the datastore as a string (actually we are editing the value that have been saved before)
        context.dataStore.edit {
            it[userToken] = token
        }
    }

    fun getUserId() = context.dataStore.data.map {
        //give me the userToken from datastore and if it was empty just give me an empty string
        it[userToken] ?: ""
    }

    suspend fun setFollowingsCollection(data: Set<String>){
        context.dataStore.edit {
            it[followingsCollection] = data
        }
    }

    fun getFollowingsCollection() = context.dataStore.data.map {
        it[followingsCollection] ?: setOf()
    }

    suspend fun setFollowersCollection(data: Set<String>){
        context.dataStore.edit {
            it[followersCollection] = data
        }
    }

    fun getFollowersCollection() = context.dataStore.data.map {
        it[followersCollection] ?: setOf()
    }

    suspend fun setFollowingRequestedCollection(followings: Set<String>){
        context.dataStore.edit {
            it[followingsRequestedCollection] = followings
        }
    }

    fun getFollowingRequestedCollection() = context.dataStore.data.map {
        it[followingsRequestedCollection] ?: setOf()
    }

    suspend fun setFollowersRequestedCollection(followings: Set<String>){
        context.dataStore.edit {
            it[followersRequestedCollection] = followings
        }
    }

    fun getFollowersRequestedCollection() = context.dataStore.data.map {
        it[followersRequestedCollection] ?: setOf()
    }
}