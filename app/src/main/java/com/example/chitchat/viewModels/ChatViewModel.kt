package com.example.chitchat.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitchat.models.Message
import com.example.chitchat.models.User
import com.example.chitchat.repositories.AuthRepository
import com.example.chitchat.repositories.FirestoreRepository
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: FirestoreRepository = FirestoreRepository()
): ViewModel() {
    private val _messageList = mutableStateListOf<Message>()
    val messageList: List<Message> = _messageList

    var messageListener: ListenerRegistration? = null

    private var currentUser: User? = null
    var currentUserId = ""

    init {
        getCurrentProfile()
    }

    private fun getCurrentProfile() = viewModelScope.launch {
        currentUserId = AuthRepository().getUserId()

        if (currentUserId.isNotBlank()) {
            repository.getUserProfile(currentUserId) {
                currentUser = it

            }
        }
    }

    fun sendNewMessage(body: String, chatId: String) = viewModelScope.launch {
        if(body.isNotBlank() && chatId.isNotBlank()) {

            var sentMessage = Message(
                message = body,
                from = currentUser?.username ?: "",
                fromUserId = currentUserId,
                fromUserProfilePic = currentUser?.profileImage ?: ""
            )

            repository.addNewMessage(
                newMessage = sentMessage,
                chatId = chatId
            ) {
                Log.d("Added message success", it.toString())
            }
        }
    }

    fun getRealtimeMessages(chatId: String) {
        val collectionRef = Firebase.firestore
            .collection("conversations")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(50)

        messageListener = collectionRef.addSnapshotListener {snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null) {
                _messageList.clear()
                for (document in snapshot){
                    _messageList.add(document.toObject(Message::class.java))
                }
                Log.d("RECEIVED NEW MESSAGES: ", snapshot.toString())
            }
        }
    }

    override fun onCleared() {
        messageListener?.remove()
        messageListener = null
    }

}
