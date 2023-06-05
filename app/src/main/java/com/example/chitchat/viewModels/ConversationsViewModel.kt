package com.example.chitchat.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitchat.models.Conversation
import com.example.chitchat.repositories.FirestoreRepository
import kotlinx.coroutines.launch

class ConversationsViewModel (
    private val repository: FirestoreRepository = FirestoreRepository()
):ViewModel() {


    private val _convoLists = mutableStateListOf<Conversation>()
    val convoList: List<Conversation> = _convoLists

    //get conversations on initialisation
    init {
        getConversations()
    }

    fun getConversations() = viewModelScope.launch {
        repository.getAllConversations() {
            data ->
            if (data!= null) {
                for(document in data) {
                    _convoLists.add(document)
                }
            }
        }
    }
}