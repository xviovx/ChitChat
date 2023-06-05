package com.example.chitchat.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chitchat.R
import com.example.chitchat.repositories.AuthRepository
import com.example.chitchat.ui.theme.ChitChatTheme
import com.example.chitchat.viewModels.AuthViewModel

@Composable
fun ProfileScreen(
    viewModel: AuthViewModel? = null,
    navOnSignOut: () -> Unit,
    navBack:() -> Unit,
    modifier: Modifier = Modifier
) {
    val user = viewModel?.currentUser?.collectAsState()?.value

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier
                    .padding(5.dp)
                    .clickable { navBack.invoke() })

            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(text = user?.username ?: "Loading...") // Display the user's username

        Spacer(modifier = Modifier.height(16.dp)) // add some space before the button

        Button(
            onClick = { AuthRepository().logoutUser(); navOnSignOut.invoke();},
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFED4F5C)) // set your button color here
        ) {
            Text("Sign Out")
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewProfileScreen() {
    ChitChatTheme {
        ProfileScreen(navBack = {}, navOnSignOut = {})
    }
}
