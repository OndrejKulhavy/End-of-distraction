package com.example.nerusme

import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), SensorEventListener{
    lateinit var sensorManager: SensorManager
    lateinit var vibrator: Vibrator
    lateinit var mNotificationManager: NotificationManager
    var uzJsemVibroval = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("Neruš mě")
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)


        if (!(getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).isNotificationPolicyAccessGranted) {
                    AlertDialog.Builder(this)
                        .setTitle("Přístup k režimu NERUŠIT")
                        .setMessage("Tato aplikace potřebuje přístup k režimu nerušit pro svojí funkčnost. Tlačítko POVOLIT PŘÍSTUP tě přenese do nastavení a tam povol tuto aplikaci, poté se vrať zpět.")
                        .setPositiveButton("Povolit přístup", DialogInterface.OnClickListener{dialog, which ->
                            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                            startActivity(intent)
                        })
                        .show()
        }


    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int){}
    override fun onSensorChanged(event : SensorEvent?) {

        val x = event!!.values[0]
        val y = event.values[1]
        val z = event.values[2]
        velikost.text = "x  =  ${x}\n\n "+ "y = ${y}\n\n" + "z = ${z}"
        val uhel = z
                            if(uhel > 8f){
                                popisOrientace.text = "nahoru"
                                uzJsemVibroval = false

                                if ((getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).isNotificationPolicyAccessGranted){
                                    mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
                                }

                            }
                            if (uhel < -8f){
                                popisOrientace.text = "dolů"
                                    if (uzJsemVibroval == false){
                                        zavibruj()
                                    }
                                if ((getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).isNotificationPolicyAccessGranted){
                                    mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
                                }
                            }
                            if (uhel > -8 && uhel < 8){
                                popisOrientace.text = "otáčení..."
                            }
    }
    fun zavibruj(){
        uzJsemVibroval = true
        vibrator.vibrate(50)
    }



}
