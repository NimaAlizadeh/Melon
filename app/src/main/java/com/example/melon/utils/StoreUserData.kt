package com.example.melon.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StoreUserData @Inject constructor(@ApplicationContext val context: Context)
{
    companion object{
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.DATASTORE_NAME_USER)

        val userToken = stringPreferencesKey(Constants.DATASTORE_TOKEN_USER)
    }

    suspend fun setUserToken(token: String)
    {
        //here we just save our string value in the datastore as a string (actually we are editing the value that have been saved before)
        context.dataStore.edit {
            it[userToken] = token
        }
    }

    fun getUserToken() = context.dataStore.data.map {
        //give me the userToken from datastore and if it was empty just give me an empty string
        it[userToken] ?: ""
    }
}