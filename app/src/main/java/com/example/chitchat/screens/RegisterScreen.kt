package com.example.chitchat.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chitchat.R
import com.example.chitchat.ui.theme.ChitChatTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.chitchat.viewModels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navToLogin:() -> Unit,
    navToHome:() -> Unit,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier) {
    val montserratXB = FontFamily(Font(R.font.mont_xb))

    val authUiState = authViewModel?.authUiState
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .background(color = Color(0xFFED4F5C))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Sign Up",
                    style = MaterialTheme.typography.displaySmall.copy(
                        color = Color.White,
                        fontFamily = montserratXB,
                    ),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.size(25.dp))
                Text(
                    text = "Create an account",
                    style = MaterialTheme.typography.displaySmall.copy(
                        color = Color.White,
                        fontFamily = FontFamily.Default,
                        fontSize = 16.sp
                    ),
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.size(30.dp))

        OutlinedTextField(
            value = authUiState?.registerEmail ?: "",
            onValueChange = { authViewModel?.handleInputStateChanges("registerEmail", it)},
            label = { Text(text = "Email")},
            leadingIcon = {
                Icon(imageVector = Icons.Default.Email,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.size(10.dp))

        OutlinedTextField(
            value = authUiState?.registerUsername ?: "",
            onValueChange = { authViewModel?.handleInputStateChanges("registerUsername", it)},
            label = { Text(text = "Username")},
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.size(10.dp))

        OutlinedTextField(
            value = authUiState?.registerPassword ?: "",
            onValueChange = { authViewModel?.handleInputStateChanges("registerPassword", it)},
            label = { Text(text = "Password")},
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.size(30.dp))

        Button(
            onClick = { authViewModel.createNewUser(context = context) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFED4F5C))
        ) {
            Text(text = "REGISTER",
                fontSize = 18.sp,
                modifier = Modifier.padding(10.dp)
            )
        }

        Spacer(modifier = Modifier.size(15.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Already have an account?",
                color = Color.Black
            )

            TextButton(
                onClick = { navToLogin.invoke() },
                modifier = Modifier.padding(start = 4.dp),
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFED4F5C))
            ) {
                Text(text = "Login")
            }
        }
    }
    LaunchedEffect(key1 = authViewModel.hasUser) {
        if(authViewModel.hasUser) {
            navToHome.invoke()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewRegisterScreen() {
    ChitChatTheme {
        RegisterScreen(navToLogin = {}, navToHome = {}, authViewModel = AuthViewModel())
    }
}
