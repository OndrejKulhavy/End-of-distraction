package com.example.nerusme

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.SensorManager
import android.os.Binder
import android.os.IBinder
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class ExampleService : Service() {
    /*
    Kanal pro komunikaci s volajicimi aktivitami
     */
    private val channel = MyChannelToService()

    var kontrolor : KontrolorPolohy? = null

    override fun onCreate() {
        kontrolor = KontrolorPolohy(
            getSystemService(Context.SENSOR_SERVICE) as SensorManager,
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator,
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager,
            this
        )
        kontrolor?.register()
    }

    override fun onDestroy() {
        kontrolor?.unregister()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(this, NerusmeApp.CHANNEL_ID)
            .setContentTitle("Nerus me")
            .setContentText("Started")
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? {
        return channel
    }

    inner class MyChannelToService : Binder() {
        fun getService(): ExampleService {
            return this@ExampleService
        }
    }

    fun showNotification(message : String) {
        val manager = NotificationManagerCompat.from(this)
        val notification = NotificationCompat.Builder(this, NerusmeApp.CHANNEL_ID)
            .setContentTitle("Nerus me")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .build()

        manager.notify(1, notification)
    }

    fun sendMessageToActivity(velikost: String, popisOrientace: String) {
        val intent = Intent("service.to.activity.transfer")
        intent.putExtra("velikost", velikost)
        intent.putExtra("popisOrientace", popisOrientace)
        sendBroadcast(intent)
    }
}
