package com.example.nerusme

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class NerusmeApp : Application() {
    companion object {
        val CHANNEL_ID = "nerusmeServiceChannel"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID, "Nerus Me Service Channel", NotificationManager.IMPORTANCE_LOW)
            serviceChannel.description = "Zprávy od služby Neruš mě"
            val manager = getSystemService(NotificationManager::class.java)
            if(manager!=null) {
                manager.createNotificationChannel(serviceChannel)
            }
        }
    }
}