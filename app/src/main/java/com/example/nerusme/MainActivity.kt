package com.example.nerusme

import android.app.AlertDialog
import android.app.NotificationManager
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
//    var myService: ExampleService? = null
    var broadcastReceiver : BroadcastReceiver? = null
    var beziAktivita = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (!(getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).isNotificationPolicyAccessGranted) {
            AlertDialog.Builder(this)
                .setTitle("Přístup k režimu NERUŠIT")
                .setMessage("Tato aplikace potřebuje přístup k režimu nerušit pro svojí funkčnost. Tlačítko POVOLIT PŘÍSTUP tě přenese do nastavení a tam povol tuto aplikaci, poté se vrať zpět.")
                .setPositiveButton(
                    "Povolit přístup",
                    DialogInterface.OnClickListener { dialog, which ->
                        val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                        startActivity(intent)
                    })
                .show()
        }

        broadcastReceiver = object:BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val velikostValue = intent?.getStringExtra("velikost")
                val popisOrientaceValue = intent?.getStringExtra("popisOrientace")
//                Log.d("Main activity", "Received " + velikostValue + ":" + popisOrientaceValue)
                velikost.text = velikostValue
                popisOrientace.text = popisOrientaceValue
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction("service.to.activity.transfer")
        registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(broadcastReceiver)
    }

    fun startService(view: View) {
        beziAktivita = true
        val serviceIntent = Intent(this, ExampleService::class.java)
        startService(serviceIntent)
//        bindService(Intent(this, ExampleService::class.java), myConnection, Context.BIND_AUTO_CREATE)
    }
    fun stopService(view: View) {
        beziAktivita = false
        val serviceIntent = Intent(this, ExampleService::class.java)
//        unbindService(myConnection)
        stopService(serviceIntent)
    }

//    private val myConnection = object : ServiceConnection {
//        override fun onServiceConnected(className: ComponentName, service: IBinder) {
//            val channel = service as ExampleService.MyChannelToService
//            myService = channel.getService()
//        }
//
//        override fun onServiceDisconnected(name: ComponentName) {
//            myService = null
//        }
//    }

}
