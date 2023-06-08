package com.example.chitchat.repositories

import android.provider.Telephony.Sms.Conversations
import android.util.Log
import com.example.chitchat.models.Conversation
import com.example.chitchat.models.Message
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
        Log.d("AAA getting convos in repo..", "UesQ")
        val conversations = arrayListOf<Conversation>()

        conversationRef.orderBy("title").get()
            .addOnSuccessListener {
                for(document in it) {
                    conversations.add(
                        Conversation(
                            id = document.id,
                            title = document.data["title"].toString(),
                            image = document.data["image"].toString()
                        )
                    )
                }
                Log.d("AAA Conversation Data: ", conversations.toString())
                onSuccess(conversations)
            }
            .addOnFailureListener {
                Log.d("error while trying to retrieve data: ", it.localizedMessage)
                onSuccess(null)
            }.await()
    }

    suspend fun addNewMessage(
        newMessage: Message,
        chatId: String,
        onSuccess: (Boolean) -> Unit
    ) {
        conversationRef.document(chatId).collection("messages")
            .add(newMessage)
            .addOnSuccessListener {
                Log.d("AAA new message sent", it.id)
                onSuccess.invoke(true)
            }
            .addOnFailureListener{
                Log.d("AAA problem adding: ", it.localizedMessage)
                it.printStackTrace()
                onSuccess.invoke(false)
            }.await()
    }
    suspend fun getUserProfile(
        uid: String,
        onSuccess: (User?) -> Unit
    ) {
        Log.d("AAA getting new user: ", uid)
        userRef.document(uid).get()
            .addOnSuccessListener {
                if (it != null) {
                    onSuccess.invoke(it?.toObject(User::class.java))
                } else {
                    onSuccess.invoke(null)
                }
            }
            .addOnFailureListener{
                onSuccess.invoke(null)
            }.await()
    }


}