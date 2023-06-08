package com.example.chitchat.models

import com.google.firebase.Timestamp

data class Message(
    val from: String = "",
    val fromUserId: String = "",
    val fromUserProfilePic: String = "",
    val message: String = "",
    val timestamp: Timestamp = Timestamp.now()
)
