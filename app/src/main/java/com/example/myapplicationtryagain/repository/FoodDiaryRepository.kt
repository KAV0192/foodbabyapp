package com.example.myapplicationtryagain.repository

import com.example.myapplicationtryagain.model.FoodDiaryEntry
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FoodDiaryRepository {
    private val database = FirebaseDatabase.getInstance()

    // Получение всех записей на конкретную дату
    suspend fun getAllEntriesForDate(userId: String, date: String,): List<FoodDiaryEntry> = withContext(Dispatchers.IO) {
        val ref = database.getReference("food_diary/$userId/$date")
        return@withContext try {
            val snapshot = ref.get().await()
            val entries = mutableListOf<FoodDiaryEntry>()
            for (mealSnapshot in snapshot.children) {
                val mealType = mealSnapshot.key ?: continue
                for (entrySnapshot in mealSnapshot.children) {
                    val entry = entrySnapshot.getValue(FoodDiaryEntry::class.java)
                    if (entry != null) {
                        entries.add(entry.copy(mealtype = mealType))
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
    suspend fun addEntry(
        userId: String,
        date: String,
        mealType: String,
        entry: FoodDiaryEntry
    ) = withContext(Dispatchers.IO) {
        val key = entry.id
        val ref = database.getReference("food_diary/$userId/$date/$mealType/$key")
        ref.setValue(entry).await()
    }

    // Удаление записи
    suspend fun deleteEntry(
        userId: String,
        date: String,
        mealType: String,
        entryId: String
    ) = withContext(Dispatchers.IO) {
        val ref = database.getReference("food_diary/$userId/$date/$mealType/$entryId")
        ref.removeValue().await()
    }
}
