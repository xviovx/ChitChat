package com.example.chitchat.repositories

import android.provider.Telephony.Sms.Conversations
import android.util.Log
import com.example.chitchat.models.Conversation
import com.example.chitchat.models.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

const val USER_REF = "users"
const val CONVERSATION_REF = "conversations"

class FirestoreRepository {
    val db = Firebase.firestore

    private val userRef = db.collection(USER_REF)
    private val conversationRef = db.collection(CONVERSATION_REF)

    fun createUserInDatabase(
        uid: String,
        username: String,
        email: String,
        onSuccess: (Boolean) -> Unit
    ) {
        userRef.document(uid)
            .set(
                User(
                    id = uid,
                    username = username,
                    email = email,
                    profileImage = "")
            )
            .addOnSuccessListener {
                Log.d("AAA create user successful: ", "right on!")
                onSuccess.invoke(true)
            }
            .addOnFailureListener {
                Log.d("AAA failed to create user", it.localizedMessage)
                onSuccess.invoke(false)
            }
    }

    suspend fun getUser(uid: String): User? {
        return try {
            val snapshot = userRef.document(uid).get().await()
            val user: User? = if (snapshot.exists()) {
                snapshot.toObject(User::class.java)
            } else {
                null
            }
            Log.d("FirestoreRepository", "Fetched user: $user")
            user
        } catch (e: Exception) {
            Log.d("AAA failed to retrieve user", e.localizedMessage)
            null
        }
    }


    suspend fun getAllConversations(
        onSuccess: (List<Conversation>?) -> Unit
    ) {
        conversationRef.orderBy("title").get()
            .addOnSuccessListener {
                Log.d("AAA Conversation Data: ", it.toString())
                onSuccess(it.toObjects(Conversation::class.java))
            }
            .addOnFailureListener {
                Log.d("error while trying to retrieve data: ", it.localizedMessage)
                onSuccess(null)
            }.await()
    }


}