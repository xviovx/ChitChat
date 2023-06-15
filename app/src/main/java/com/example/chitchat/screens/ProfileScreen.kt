package com.example.chitchat.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.chitchat.R
import com.example.chitchat.repositories.AuthRepository
import com.example.chitchat.ui.theme.ChitChatTheme
import com.example.chitchat.viewModels.AuthViewModel
import com.example.chitchat.viewModels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    navOnSignOut: () -> Unit,
    navBack:() -> Unit,
    modifier: Modifier = Modifier
) {

    val profileUiState = viewModel?.profileUiState


//    var pickedImage: Uri? by remember { mutableStateOf(null) }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {uri ->
            if (uri != null) {
                viewModel.handleProfileImageChange(uri)
            }
        }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
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

        Text(
            text = "Profile",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(profileUiState?.profileImage ?: "/")
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.ic_profile),
            modifier = Modifier
                .size(90.dp) // Adjust the size here
                .clip(CircleShape) // Clip the image into a circular shape
                .background(color = Color.LightGray)
        )

//        Image(
//            painter = painterResource(id = R.drawable.ic_profile),
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = modifier
//                .size(70.dp)
//                .background(color = Color.LightGray, shape = CircleShape)
//        )

        Button(
            onClick = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFED4F5C))
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null
            )
        }


        Spacer(modifier = Modifier.size(10.dp))

        OutlinedTextField(
            value = profileUiState?.username ?: "",
            onValueChange = { viewModel.handleUsernameStateChange(it) },
            label = { Text(text = "Username") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = profileUiState?.email ?: "",
            onValueChange = { },
            label = { Text(text = "Email") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            enabled = false
        )

        Spacer(modifier = Modifier.size(20.dp))

        Button(
            onClick = {
                viewModel.saveProfileData()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFFED4F5C)
            ),
            border = BorderStroke(width = 2.dp, color = Color(0xFFED4F5C)) // Set the border color to #ED4F5C and increase the width to 2.dp
        ) {
            Text("Save Profile")
        }


        Spacer(modifier = Modifier.size(20.dp))

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
