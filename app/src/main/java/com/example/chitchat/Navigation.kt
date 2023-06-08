package com.example.chitchat

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chitchat.screens.ChatScreen
import com.example.chitchat.screens.ConversationScreen
import com.example.chitchat.screens.LoginScreen
import com.example.chitchat.screens.ProfileScreen
import com.example.chitchat.screens.RegisterScreen
import com.example.chitchat.viewModels.AuthViewModel

enum class AuthRoutes {
    Login,
    Register
}

enum class HomeRoutes {
    Conversations,
    Chat,
    Profile
}

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel
) {

    val startingScreen = if(authViewModel.hasUser){
        HomeRoutes.Conversations.name
    } else {
        AuthRoutes.Login.name
    }

    NavHost(
        navController = navController,
        startDestination = startingScreen){


        //my login screen
        composable(route = AuthRoutes.Login.name){
            LoginScreen(
                navToRegister = {
                    navController.navigate(AuthRoutes.Register.name){
                        launchSingleTop = true
                        popUpTo(route = AuthRoutes.Login.name){
                            inclusive = true
                        }
                    }
                },
                navToHome = {
                            navController.navigate(HomeRoutes.Conversations.name){
                                launchSingleTop = true
                                popUpTo(route = AuthRoutes.Login.name) {
                                    inclusive = true
                                }
                            }
                },
                authViewModel = authViewModel)
        }

        //my register screen
        composable(route = AuthRoutes.Register.name){
            RegisterScreen(
                navToLogin = {
                navController.navigate(AuthRoutes.Login.name){
                    launchSingleTop = true
                    popUpTo(route = AuthRoutes.Register.name){
                        inclusive = true
                    }
                }
            },navToHome = {
                    navController.navigate(HomeRoutes.Conversations.name){
                        launchSingleTop = true
                        popUpTo(route = AuthRoutes.Login.name) {
                            inclusive = true
                        }
                    }
                },
                authViewModel = authViewModel)
        }

        composable(route = HomeRoutes.Conversations.name){
            ConversationScreen(
                onNavToProfile = {
                    navController.navigate(HomeRoutes.Profile.name){
                        launchSingleTop = true
                    }
                },
                onNavToChat = {
                    navController.navigate("${HomeRoutes.Chat.name}/${it}"){
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = HomeRoutes.Profile.name) {
            ProfileScreen(
                navOnSignOut = { navController.navigate(AuthRoutes.Login.name){
                    launchSingleTop = true
                    popUpTo(route = HomeRoutes.Conversations.name){
                        inclusive = true
                    }
                }
                } ,
                navBack = {
                    navController.navigate(HomeRoutes.Conversations.name){
                        launchSingleTop = true
                        popUpTo(route = HomeRoutes.Conversations.name)
                    }
                })
        }

        composable(
            route = "${HomeRoutes.Chat.name}/{chatId}",
            arguments = listOf(navArgument("chatId"){type = NavType.StringType; defaultValue = "chat1234"})
        ) {
            ChatScreen(chatId = it.arguments?.getString("chatId"),

                navBack = { navController.navigate(HomeRoutes.Conversations.name){
                launchSingleTop = true
                popUpTo(route = HomeRoutes.Conversations.name)
            } })
        }



        //TODO: Create links to profile & chat screen (pass data)
        //TODO: Setup our auth to prevent access to specific screens
    }
}