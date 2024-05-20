package com.example.simpletimerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simpletimerapp.ui.theme.SimpleTimerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleTimerAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TimerScreen()
                }
            }
        }
    }
}

@Composable
fun TimerScreen(timerViewModel: TimerViewModel = viewModel()) {
    val timeLeft by timerViewModel.timeLeft.observeAsState(0L)
    val isRunning by timerViewModel.isRunning.observeAsState(false)

    var input by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Minutes") },

            modifier = Modifier.padding(16.dp)
//            modifier = Modifier.width(120.dp).height(IntrinsicSize.Min).padding(all=10.dp) // Restricts height for compactness
        )
        Button(
            onClick = {
                val minutes = input.text.toLongOrNull() ?: 0
                timerViewModel.startTimer(minutes)
            },
            enabled = !isRunning,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Start")
        }
        Text(
            text = String.format(
                "%02d:%02d",
                (timeLeft / 1000) / 60,
                (timeLeft / 1000) % 60
            ),
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(16.dp)
        )
        Button(
            onClick = { timerViewModel.resetTimer() },
            enabled = isRunning,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Reset")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimerScreenPreview() {
    SimpleTimerAppTheme {
        TimerScreen()
    }
}
