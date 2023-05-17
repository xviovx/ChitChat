package com.example.chitchat

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chitchat.screens.LoginScreen
import com.example.chitchat.screens.RegisterScreen

enum class AuthRoutes {
    Login,
    Register
}

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AuthRoutes.Login.name){

        composable(route = AuthRoutes.Login.name){
            LoginScreen(
                navToRegister = {navController.navigate(AuthRoutes.Register.name){
                    launchSingleTop = true
                } }
            )
        }

        composable(route = AuthRoutes.Register.name){
            RegisterScreen(navToLogin = {
                    navController.navigate(AuthRoutes.Login.name){
                        launchSingleTop = true
                    }
                })
        }
    }
}