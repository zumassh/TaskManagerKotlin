package com.example.taskmanager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskmanager.presentation.auth.AuthScreen
import com.example.taskmanager.presentation.tasks.TaskScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "auth",
        modifier = modifier
    ) {
        composable("auth") {
            AuthScreen(
                onAuthenticated = {
                    navController.navigate("tasks") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }
        composable("tasks") {
            TaskScreen()
        }
        // сюда позже добавим calendar, stats и модальное окно
    }
}
