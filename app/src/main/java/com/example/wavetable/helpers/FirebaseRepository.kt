package com.example.wavetable.helpers

import com.example.wavetable.model.Item
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object FirebaseRepository {

    private val DatabaseUrl = "https://wavetable-467f5-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private val database = Firebase.database(DatabaseUrl)
    private val itemsRef = database.getReference("items")

    suspend fun getItemByKey(firebaseKey: String): Item? {
        return try {
            val snapshot = itemsRef.child(firebaseKey).get().await()
            snapshot.getValue(Item::class.java)?.copy(id = firebaseKey)
        } catch (e: Exception) {
            println("Error getting item by key: ${e.message}")
            null
        }
    }

    suspend fun getAllItems(): List<Item> {
        return try {
            val snapshot = itemsRef.get().await()
            snapshot.children.mapNotNull {
                it.getValue(Item::class.java)?.copy(id = it.key ?: "")
            }
        } catch (e: Exception) {
            println("Error getting items: ${e.message}")
            emptyList()
        }
    }
}
