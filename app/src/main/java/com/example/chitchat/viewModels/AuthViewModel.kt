package com.example.chitchat.viewModels

import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitchat.models.User
import com.example.chitchat.repositories.AuthRepository
import com.example.chitchat.repositories.FirestoreRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
): ViewModel() {
    private val _currentUser = MutableStateFlow<User?>(null) // Initialize with null
    val currentUser: StateFlow<User?> = _currentUser

    init {
        updateCurrentUser()
    }

    suspend fun convertFirebaseUserToUser(firebaseUser: FirebaseUser): User? {
        val firestoreRepository = FirestoreRepository()
        var user: User? = null

        try {
            user = firestoreRepository.getUser(firebaseUser.uid)
            Log.d("AuthViewModel", "Converted FirebaseUser to User: $user")
        } catch (e: Exception) {
            Log.d("Error converting user: ", e.localizedMessage)
            e.printStackTrace()
        }

        return user
    }

    fun updateCurrentUser() {
        viewModelScope.launch {
            val firebaseUser = repository.currentUser
            if (firebaseUser != null) {
                // You'll need to write a method to convert from FirebaseUser to your User class
                _currentUser.value = convertFirebaseUserToUser(firebaseUser)
                Log.d("AuthViewModel", "Updated current user: ${_currentUser.value}")
            } else {
                _currentUser.value = null
            }
        }
    }
    val hasUser: Boolean
        get() = repository.hasUser()

    var authUiState by mutableStateOf(AuthUiState())
        private set

    //function to handle the value updates of my states
    fun handleInputStateChanges(target: String, value: String){
        if (target == "loginEmail"){
            authUiState = authUiState.copy(loginEmail = value)
        } else if (target == "loginPassword") {
            authUiState = authUiState.copy(loginPassword = value)
        } else if (target == "registerUsername") {
            authUiState = authUiState.copy(registerUsername = value)
        } else if (target == "registerEmail") {
            authUiState = authUiState.copy(registerEmail = value)
        } else if (target == "registerPassword") {
            authUiState = authUiState.copy(registerPassword = value)
        }
    }

    //register functionality
    fun createNewUser(context: Context) = viewModelScope.launch {
        authUiState = authUiState.copy(errorMessage = "")
        try {
            if (authUiState.registerUsername.isBlank() || authUiState.registerEmail.isBlank() || authUiState.registerPassword.isBlank()) {
                authUiState = authUiState.copy(errorMessage = "Please fill out all fields")
            } else {
                authUiState = authUiState.copy(isLoading = true)

                repository.registerNewUser(
                    authUiState.registerEmail,
                    authUiState.registerPassword
                ){userId ->
                    if (userId.isNotBlank()){
                        //success
                        FirestoreRepository().createUserInDatabase(
                            uid = userId,
                            username = authUiState.registerUsername,
                            email = authUiState.registerEmail
                        ){
                            if(it) {
                                Log.d("Successfully registered!", userId)

                                Toast.makeText(
                                    context,
                                    "Registration Completed",
                                    Toast.LENGTH_SHORT).show()

                                authUiState = authUiState.copy(authSuccess = true)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Something went horribly wrong",
                                    Toast.LENGTH_SHORT).show()

                                authUiState = authUiState.copy(authSuccess = false)
                            }
                        }

                    } else { //register failed
                        Log.d("Error registering", "Something went horribly wrong")

                        Toast.makeText(
                            context,
                            "Registration Failed",
                            Toast.LENGTH_SHORT).show()
                        authUiState = authUiState.copy(authSuccess = false)
                        authUiState = authUiState.copy(errorMessage = "Invalid email and/or password")
                    }
                }
            }
        } catch (e: Exception){
            Log.d("Error during registration ", e.localizedMessage)
            e.printStackTrace()
        } finally {
            authUiState = authUiState.copy(isLoading = false)
        }
    }

    //login functionality
    fun loginUser(context: Context) = viewModelScope.launch {
        authUiState = authUiState.copy(errorMessage = "")
        try {
            if (authUiState.loginEmail.isBlank() || authUiState.loginPassword.isBlank()) {
                authUiState = authUiState.copy(errorMessage = "Please fill out all fields")
            }
         else {
                authUiState = authUiState.copy(isLoading = true)

                repository.loginUser(
                    authUiState.loginEmail,
                    authUiState.loginPassword
                ){isCompleted ->
                    if (isCompleted){
                        //success
                        Log.d("Login Success", "Ok")

                        Toast.makeText(
                            context,
                            "Login Completed",
                            Toast.LENGTH_SHORT).show()

                        authUiState = authUiState.copy(authSuccess = true)
                    } else { //register failed
                        Log.d("Error Logging IN", "Something went horribly wrong")

                        Toast.makeText(
                            context,
                            "Login Failed",
                            Toast.LENGTH_SHORT).show()
                        authUiState = authUiState.copy(authSuccess = false)
                        authUiState = authUiState.copy(errorMessage = "Invalid email and/or password")
                    }
                }
            }
        } catch (e: Exception){
            Log.d("Login Error: ", e.localizedMessage)
            e.printStackTrace()
        } finally {
            authUiState = authUiState.copy(isLoading = false)
        }
    }
}

//values for front-end state mangement
data class AuthUiState (
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val authSuccess: Boolean = false,

    val loginEmail: String = "",
    val loginPassword: String = "",

    val registerUsername: String = "",
    val registerEmail: String = "",
    val registerPassword: String = ""
)