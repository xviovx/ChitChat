package com.example.chitchat.viewModels

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chitchat.models.User
import com.example.chitchat.repositories.AuthRepository
import com.example.chitchat.repositories.FirestoreRepository
import com.example.chitchat.repositories.StorageRepository
import kotlinx.coroutines.launch

class ProfileViewModel (
    private val authRepository: AuthRepository = AuthRepository(),
    private val firestoreRepository: FirestoreRepository = FirestoreRepository(),
    private val storageRepository: StorageRepository = StorageRepository()
): ViewModel() {

    private val hasUser = authRepository.hasUser()
    private val currentUserId = authRepository.getUserId()

    init {
        Log.d("AAA profile view model", "INIT")
        getProfileData()
    }

    var profileUiState by mutableStateOf(ProfileUiState())
        private set

    var oldImage: String = ""

    fun handleUsernameStateChange(value: String){
        profileUiState = profileUiState.copy(username = value)
    }

    fun handleProfileImageChange(value: Uri) {
        profileUiState = profileUiState.copy(profileImage = value)
    }

    private fun getProfileData() = viewModelScope.launch {
        if (currentUserId.isNotBlank()) {
            firestoreRepository.getUserProfile(currentUserId) {
                profileUiState = profileUiState.copy(
                    username = it?.username ?: "",
                    email = it?.email ?: "",
                    profileImage = Uri.parse(it?.profileImage)
                )
                oldImage = it?.profileImage ?: ""

            }
        }
    }

    fun saveProfileData() = viewModelScope.launch {
        if (hasUser) {
            var downloadUrl = oldImage

            if (oldImage != profileUiState.profileImage.toString() || oldImage.isBlank()) {

                storageRepository.uploadImageToStorage(
                    imageUri = profileUiState.profileImage,
                    fileName = "$currentUserId-${profileUiState.username}"
                ) {
                    downloadUrl = it
                }
            }

            firestoreRepository.updateProfileInformation(
                user = User(
                    id = currentUserId,
                    username = profileUiState.username,
                    email = profileUiState.email,
                    profileImage = downloadUrl
                )
            ) {
                Log.d("AAA updated user? ", it.toString())
            }
        }
    }
}

data class ProfileUiState (
    val username: String = "",
    val email: String = "",
    val profileImage: Uri = Uri.EMPTY
)

