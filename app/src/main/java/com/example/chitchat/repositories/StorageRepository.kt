package com.example.chitchat.repositories

import android.net.Uri
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class StorageRepository {

    val storageRef = Firebase.storage.reference

    suspend fun uploadImageToStorage(
        imageUri: Uri,
        fileName: String,
        onSuccess: (downloadUrl: String) -> Unit
    ) {
        try {
            val downloadUrl = storageRef.child("profiles/$fileName")
                .putFile(imageUri).await()
                .storage.downloadUrl.await()

            onSuccess(downloadUrl.toString())

        }catch (e: Exception) {
            e.printStackTrace()
            onSuccess("")
        }
    }
}