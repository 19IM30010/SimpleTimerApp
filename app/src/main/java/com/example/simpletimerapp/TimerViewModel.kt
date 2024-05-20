package com.example.simpletimerapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.CountDownTimer
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TimerViewModel(application: Application) : AndroidViewModel(application) {

    private var countDownTimer: CountDownTimer? = null
    private val _timeLeft = MutableLiveData<Long>()
    val timeLeft: LiveData<Long> = _timeLeft

    private val _isRunning = MutableLiveData<Boolean>()
    val isRunning: LiveData<Boolean> = _isRunning

    init {
        createNotificationChannel()
    }

    fun startTimer(minutes: Long) {
        countDownTimer?.cancel()
        val millisInput = minutes * 60000
        countDownTimer = object : CountDownTimer(millisInput, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timeLeft.value = millisUntilFinished
            }

            override fun onFinish() {
                _timeLeft.value = 0
                _isRunning.value = false
                sendNotification()
            }
        }.start()
        _isRunning.value = true
    }

    fun resetTimer() {
        countDownTimer?.cancel()
        _timeLeft.value = 0
        _isRunning.value = false
    }

    private fun sendNotification() {
        val notification = NotificationCompat.Builder(getApplication(), "timerChannel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Timer Finished")
            .setContentText("Your timer is up!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        with(NotificationManagerCompat.from(getApplication())) {
            notify(1, notification)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Timer Channel"
            val descriptionText = "Channel for timer notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("timerChannel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getApplication<Application>().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}
