package com.example.myapplicationtryagain.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapplicationtryagain.ui.profile.ChildProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

val Context.profileDataStore by preferencesDataStore(name = "profile_prefs")

object ProfileDataStore {
    private val PROFILE_KEY = stringPreferencesKey("profile")

    suspend fun saveProfile(context: Context, profile: ChildProfile) {
        val json = Json.encodeToString(profile)
        context.profileDataStore.edit { prefs ->
            prefs[PROFILE_KEY] = Json.encodeToString(profile)
        }
    }

    fun getProfileFlow(context: Context): Flow<ChildProfile?> =
        context.profileDataStore.data.map { prefs ->
            prefs[PROFILE_KEY]?.let { Json.decodeFromString<ChildProfile>(it) }
        }

    suspend fun loadProfile(context: Context): ChildProfile? {
        val data = context.profileDataStore.data.first()[PROFILE_KEY]
        return data?.let { Json.decodeFromString<ChildProfile>(it) }
    }
}
