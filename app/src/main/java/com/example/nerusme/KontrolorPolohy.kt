package com.example.nerusme

import android.app.NotificationManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Vibrator
import android.util.Log

class KontrolorPolohy (
    val sensorManager: SensorManager,
    val vibrator: Vibrator,
    val notificationManager: NotificationManager,
    val service : ExampleService
) : SensorEventListener {

    var uzJsemVibroval = false

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int){}
    override fun onSensorChanged(event : SensorEvent?) {

        val x = event!!.values[0]
        val y = event.values[1]
        val z = event.values[2]
        val velikost = "x  =  ${x}\n\n "+ "y = ${y}\n\n" + "z = ${z}"
//        Log.d("Kontrolor", "x  =  ${x}\n\n "+ "y = ${y}\n\n" + "z = ${z}")
        val uhel = z
        if(uhel > 8f){
            Log.d("Kontrolor", "Nahoru")
            service.sendMessageToActivity(velikost, "Nahoru")
            service.showNotification("Nahoru")
            uzJsemVibroval = false

            if (notificationManager.isNotificationPolicyAccessGranted){
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
            }

        }
        if (uhel < -8f){
            Log.d("Kontrolor", "Dolu")
            if (uzJsemVibroval == false){
                zavibruj()
                service.sendMessageToActivity(velikost, "dolů")
                service.showNotification("Dolů")
            }
            if (notificationManager.isNotificationPolicyAccessGranted){
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
            }
        }
        if (uhel > -8 && uhel < 8){
            Log.d("Kontrolor", "Otaceni")
            service.sendMessageToActivity(velikost, "otáčení...")
            service.showNotification("Otáčení")
            if (notificationManager.isNotificationPolicyAccessGranted){
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
            }
        }
    }
    private fun zavibruj(){
        uzJsemVibroval = true
        vibrator.vibrate(40)
    }

    fun register() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
    }
    fun unregister() {
        sensorManager.unregisterListener(this)
    }

}