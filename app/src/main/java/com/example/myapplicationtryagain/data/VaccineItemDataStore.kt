package com.example.myapplicationtryagain.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

// --- DataStore declaration ---
val Context.vaccineDataStore by preferencesDataStore(name = "vaccine_prefs")

// --- Модели для хранения категорий и прививок ---

@Serializable
data class VaccineItem(
    val name: String,
    val date: String? = null // дата в формате "2025-06-17" или null если не указано
)

@Serializable
data class VaccineCategory(
    val title: String,
    val items: List<VaccineItem>
)

object VaccineDataStore {
    private val VACCINE_KEY = stringPreferencesKey("vaccine_categories_list")

    // Сохраняем весь список категорий
    suspend fun saveCategories(context: Context, categories: List<VaccineCategory>) {
        val json = Json.encodeToString(categories)
        context.vaccineDataStore.edit { prefs ->
            prefs[VACCINE_KEY] = json
        }
    }

    // Загружаем поток категорий
    fun getCategories(context: Context): Flow<List<VaccineCategory>> =
        context.vaccineDataStore.data.map { prefs ->
            val json = prefs[VACCINE_KEY] ?: "[]"
            try {
                Json.decodeFromString(json)
            } catch (e: Exception) {
                emptyList()
            }
        }
}
