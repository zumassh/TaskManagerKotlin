package com.example.taskmanager

import UrgentTaskWorker
import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import androidx.work.*
import com.example.taskmanager.navigation.AppNavGraph
import com.example.taskmanager.presentation.theme.TaskManagerTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            }
        }

        val request = PeriodicWorkRequestBuilder<UrgentTaskWorker>(1, TimeUnit.HOURS).build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "urgent_task_check",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )

        setContent {
            TaskManagerTheme {
                val navController = rememberNavController()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFFFFCF5))
                ) {
                    AppNavGraph(navController = navController)
                }
            }
        }
    }
}