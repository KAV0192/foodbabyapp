package com.example.myapplicationtryagain.repository

import com.example.myapplicationtryagain.model.FoodDiaryEntry
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FoodDiaryRepository {

    private val database = FirebaseDatabase.getInstance()

    // Получение всех записей на конкретную дату
    suspend fun getAllEntriesForDate(userId: String, date: String): List<FoodDiaryEntry> = withContext(Dispatchers.IO) {
        val ref = database.getReference("food_diary/$userId/$date")
        return@withContext try {
            val snapshot = ref.get().await()
            val entries = mutableListOf<FoodDiaryEntry>()
            for (mealSnapshot in snapshot.children) {
                for (entrySnapshot in mealSnapshot.children) {
                    val entry = entrySnapshot.getValue(FoodDiaryEntry::class.java)
                    if (entry != null) {
                        // mealSnapshot.key — это тип приёма пищи, например "breakfast"
                        entries.add(entry.copy(mealtype = mealSnapshot.key ?: "unknown"))
                    }
                }
            }
            entries
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // Добавление новой записи
    suspend fun addEntry(userId: String, date: String, mealType: String, entry: FoodDiaryEntry) = withContext(Dispatchers.IO) {
        val id = entry.productId
        val ref = database.getReference("food_diary/$userId/$date/$mealType/$id")
        ref.setValue(entry).await()
    }

    // Получение записей по приёму пищи (если нужно отдельно)
    suspend fun getEntries(userId: String, date: String, mealType: String): List<FoodDiaryEntry> = withContext(Dispatchers.IO) {
        val ref = database.getReference("food_diary/$userId/$date/$mealType")
        return@withContext try {
            val snapshot = ref.get().await()
            snapshot.children.mapNotNull { it.getValue(FoodDiaryEntry::class.java) }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
