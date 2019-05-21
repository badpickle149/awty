package edu.washington.weng2k17.awty

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.lang.Exception
import java.util.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private var timer = Timer("AnnoyingMessage", true)
    private val TAG = "MainActivity"

    companion object {
        val REQUEST_SMS_SEND_PERMISSION = 1234
        val REQUEST_CALL_PHONE_PERMISSION = 5678
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startBtn = findViewById(R.id.startBtn) as Button

        startBtn.setOnClickListener {
            val btnText = startBtn.text.toString()
            if (btnText == "Start") {
                val phoneNumber = findViewById<EditText>(R.id.phoneNumber).text.toString()
                val timeInterval = findViewById<EditText>(R.id.timeInterval).text.toString()
                val message = findViewById<EditText>(R.id.message).text.toString()
                Log.i(TAG, "trying to schedule message...")
                timer = Timer("AnnoyingMessage", true)
                if (phoneNumber != "" && timeInterval != "" && message != "") {
                    startBtn.text = "Stop"
                    timer.scheduleAtFixedRate(TimerCallback(phoneNumber, message, this), 0, timeInterval.toLong() * 60 * 1000)
                }
            } else {
                try {
                    timer.cancel()
                } catch(e: Exception) {
                    Log.i(TAG, e.toString())
                }
                startBtn.text = "Start"
                Toast.makeText(this, "Stopped the spam", Toast.LENGTH_LONG).show()
            }

        }
    }

    inner class TimerCallback(phoneNumber: String, message: String, context: Context): TimerTask() {
        private val phoneNumber = phoneNumber
        private val message = message
        private val context = context

        override fun run() {
            runOnUiThread(Runnable {
                Log.i("MainActivity", "Running in background!")
                if (checkSelfPermission(context, android.Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {

                    // Need to request SEND_SMS permission
                    requestPermissions(
                        context as Activity,
                        arrayOf(android.Manifest.permission.SEND_SMS),
                        REQUEST_SMS_SEND_PERMISSION)

                } else {

                    // Has Permissions, Send away!

                    val smsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(
                        phoneNumber,
                        null,
                        message,
                        null,
                        null)
                }
            })
        }

    }
}
