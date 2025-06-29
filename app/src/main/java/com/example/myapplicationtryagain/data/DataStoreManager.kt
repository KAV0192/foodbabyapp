package com.example.myapplicationtryagain.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.growthDataStore by preferencesDataStore("growth_records")

@Serializable
data class GrowthRecord(
    val date: String,
    val height: String,
    val weight: String
)

// Можно вынести в отдельный файл
object GrowthDataStore {
    private val KEY_RECORDS = stringPreferencesKey("growth_records_list")

    suspend fun saveRecords(context: Context, records: List<GrowthRecord>) {
        val json = Json.encodeToString(records)
        context.growthDataStore.edit { prefs ->
            prefs[KEY_RECORDS] = json
        }
    }

    fun getRecords(context: Context): Flow<List<GrowthRecord>> =
        context.growthDataStore.data.map { prefs ->
            val json = prefs[KEY_RECORDS] ?: "[]"
            try {
                Json.decodeFromString(json)
            } catch (e: Exception) {
                emptyList()
            }
        }
}