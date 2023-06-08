package com.example.chitchat.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.chitchat.R
import com.example.chitchat.models.Conversation
import com.example.chitchat.ui.theme.ChitChatTheme
import com.example.chitchat.viewModels.ConversationsViewModel

@Composable
fun ConversationScreen(
    viewModel: ConversationsViewModel? = viewModel(),
    onNavToProfile: () -> Unit,
    onNavToChat: (chatId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val allConversations = viewModel?.convoList ?: listOf<Conversation>()
    val montserratXB = FontFamily(Font(R.font.mont_xb))
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFFE7EAED))
                .fillMaxWidth()
                .height(90.dp)
                .padding(20.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier
                            .padding(5.dp)
                            .background(Color.Gray)
                            .clickable { onNavToProfile.invoke() })

                    Image(
                        painter = painterResource(id = R.drawable.ic_profile),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Text(
                text = "Conversations",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = montserratXB,
                color = Color(0xFFED4F5C),
            )
            Spacer(modifier = Modifier.height(10.dp))
//                Box(
//                    modifier = Modifier
//                        .background(Color(0xFFD6DCE4), RoundedCornerShape(10.dp))
//                        .padding(horizontal = 16.dp, vertical = 8.dp)
//                        .clickable { /* New chat button clicked */ }
//                ) {
//                    Text(
//                        text = "New Chat",
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold,
//                        fontFamily = montserratXB,
//                        color = Color(0xFFED4F5C),
//                    )
//                }
        }
        LazyColumn(){
            items(allConversations) { conversation ->
                Card(modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable { onNavToChat.invoke(conversation.id)}) {
                    ConversationCard(
                        conversation = conversation,
                        modifier = modifier
                    )
                }
            }
        }
    }
}
@Composable
fun ConversationCard(
    conversation: Conversation,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .height(70.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(conversation.image)
                        .crossfade(true)
                        .build(),
                    contentDescription = conversation.title,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = conversation.title,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .padding(top = 10.dp)
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = conversation.lastMessage,
                modifier = Modifier.padding(start = 25.dp)
            )
        }
    }
}



@Preview(showSystemUi = true)
@Composable
fun prevConversationScreen(){
    ChitChatTheme() {
        ConversationScreen(modifier = Modifier, onNavToProfile = {}, onNavToChat = {})
    }
}
