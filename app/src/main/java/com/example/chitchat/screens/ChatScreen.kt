package com.example.chitchat.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chitchat.R
import com.example.chitchat.models.Conversation
import com.example.chitchat.ui.theme.ChitChatTheme
import com.example.chitchat.ui.theme.Purple40
import com.example.chitchat.models.Message
import com.example.chitchat.ui.theme.Purple80
import com.example.chitchat.viewModels.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = viewModel(),
    chatId: String?,
    navBack:() -> Unit,
    modifier: Modifier = Modifier
) {

    var newMessage by remember {
        mutableStateOf("")
    }

    var allMessages = viewModel?.messageList ?: listOf<Message>()

    val currentUserFrom = "K"

    val dummyData = listOf<com.example.chitchat.models.Message>(
        com.example.chitchat.models.Message(
            from = "Respectable citizen",
            message = "Happier than words can describe",
            fromUserId = "K"
        ),
        com.example.chitchat.models.Message(
            from = "Me",
            message = "A message from me A message from me A message from me A message from me",
            fromUserId = "Android"
        ),
        com.example.chitchat.models.Message(
            from = "Respectable citizen",
            message = "Hello hello hello this is a message to you from a respectable citizen",
            fromUserId = "K"
        ),
        com.example.chitchat.models.Message(
            from = "Me",
            message = "A message from me A message from me A message from me A message from me",
            fromUserId = "Android"
        ),
        com.example.chitchat.models.Message(
            from = "Respectable citizen",
            message = "Hello hello hello this is a message to you from a respectable citizen",
            fromUserId = "K"
        ),
        com.example.chitchat.models.Message(
            from = "Me",
            message = "A message from me A message from me A message from me A message from me",
            fromUserId = "Android"
        ),
        com.example.chitchat.models.Message(
            from = "Respectable citizen",
            message = "Hello hello hello this is a message to you from a respectable citizen",
            fromUserId = "K"
        ),
        com.example.chitchat.models.Message(
            from = "Me",
            message = "A message from me A message from me A message from me A message from me",
            fromUserId = "Android"
        )
    )

    val isChatIdNotBlank = chatId.isNullOrBlank()

    LaunchedEffect(key1 = Unit) {
        if (!isChatIdNotBlank) {
            viewModel.getRealtimeMessages(chatId ?: "")
        } else {
            Log.d("AAA chat id error", ".........")
        }
    }


    val message = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .clickable { navBack.invoke() },
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Text(
            text = chatId ?: " ",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(20.dp)
        )
        LazyColumn(
            modifier = modifier.weight(1f),
            reverseLayout = true) {
            items(allMessages) {message ->
                if (viewModel.currentUserId == message.fromUserId) {
                    MessageToBubble(message)
                } else {
                    MessageFromBubble(message)
                }
            }
        }
        Row(modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedTextField(
                value = newMessage,
                onValueChange = { newMessage = it},
                label = { Text("New Message")},
                modifier = Modifier.weight(0.8f)
            )

            Spacer(modifier = modifier.size(8.dp))
            Button(
                onClick = { viewModel.sendNewMessage(newMessage, chatId ?: "");
                    newMessage = ""
                },
                modifier = Modifier.height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFED4F5C))
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_send), contentDescription = null)
            }
        }

    }
}

@Composable
fun MessageFromBubble(
    message: Message, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(10.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_user),
            contentDescription = null,
            modifier = modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        )

        Column(modifier = modifier.padding(start = 8.dp, end = 16.dp)) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFE7EAED), // changed background color to #E7EAED
                        shape = RoundedCornerShape(
                            topStart = 15.dp,
                            topEnd = 15.dp,
                            bottomEnd = 15.dp
                        )
                    )
            ) {
                Text(text = message.from,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black, // changed text color to Black
                    modifier = modifier.padding(start = 10.dp, top = 10.dp)
                )

                Text(
                    text = message.message,
                    color = Color.Black, // changed text color to Black
                    modifier = modifier.padding(start = 10.dp, top = 2.dp)
                )
            }
        }
    }
}


@Composable
fun MessageToBubble(
    message: Message, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(10.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Column(modifier = modifier.padding(start = 8.dp, end = 16.dp)) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFED4F5C),
                        shape = RoundedCornerShape(
                            topStart = 15.dp,
                            topEnd = 15.dp,
                            bottomStart = 15.dp
                        )
                    )
            ) {
                Text(
                    text = message.message,
                    color = Color.White,
                    modifier = modifier.padding(start = 10.dp, top = 10.dp)
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun prevChatScreen(){
    ChitChatTheme() {
        ChatScreen(navBack = { /*TODO*/ }, chatId = "chat1234")
    }
}
