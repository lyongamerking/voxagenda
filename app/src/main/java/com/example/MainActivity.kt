package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.receiver.AlarmReceiver
import com.example.ui.AgendaMainScreen
import com.example.ui.AgendaViewModel
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val viewModel: AgendaViewModel = viewModel()

                // Request notification permission on Android 13+
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { /* handle granted or denied gracefully */ }

                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val hasPermission = ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                        if (!hasPermission) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }

                    // Check if launched with alarm extras
                    val eventId = intent?.getLongExtra(AlarmReceiver.EXTRA_EVENT_ID, -1L) ?: -1L
                    if (eventId != -1L) {
                        val matchedEvent = viewModel.allEvents.value.find { it.id == eventId }
                        if (matchedEvent != null) {
                            viewModel.triggerAlarmModal(matchedEvent)
                        }
                    }
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    AgendaMainScreen(viewModel = viewModel)
                }
            }
        }
    }
}
