package com.example.taskmanager.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.taskmanager.presentation.auth.AuthScreen
import com.example.taskmanager.presentation.tasks.TaskScreen
import com.example.taskmanager.R
import com.example.taskmanager.presentation.theme.lightAccent
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute != "auth"

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "auth",
            modifier = Modifier.padding(innerPadding)
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

            composable("calendar") {
                // пока можно просто временный текст
                Text("Календарь")
            }

            composable("stats") {
                Text("Статистика")
            }

            composable("exit") {
                Text("Выход")
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem(
            route = "tasks",
            label = "Задачи",
            iconRes = R.drawable.ic_tasks,
            iconActiveRes = R.drawable.ic_tasks_active
        ),
        BottomNavItem(
            route = "calendar",
            label = "Календарь",
            iconRes = R.drawable.ic_calendar,
            iconActiveRes = R.drawable.ic_calendar_active
        ),
        BottomNavItem(
            route = "stats",
            label = "Статистика",
            iconRes = R.drawable.ic_stats,
            iconActiveRes = R.drawable.ic_stats_active
        ),
        BottomNavItem(
            route = "auth",
            label = "Выход",
            iconRes = R.drawable.ic_exit,
            iconActiveRes = R.drawable.ic_exit_active,
            isExit = true
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(containerColor = lightAccent) {
        items.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
            val iconId = if (item.isExit || !selected) item.iconRes else item.iconActiveRes ?: item.iconRes

            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = iconId),
                        contentDescription = item.label,
                        tint = Color.Unspecified
                    )
                },
                label = { Text(item.label) },
                selected = selected,
                onClick = {
                    if (item.isExit) {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("auth") {
                            popUpTo(0)
                        }
                    } else {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo("tasks") { saveState = true }
                        }
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val iconRes: Int,
    val iconActiveRes: Int? = null,
    val isExit: Boolean = false
)
