package edu.washington.weng2k17.awty

import android.content.Context
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val timer = Timer("toast", true)

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
                if (phoneNumber != "" && timeInterval != "" && message != "") {
                    startBtn.text = "Stop"
                    timer.scheduleAtFixedRate(TimerCallback(phoneNumber, message, this), 0, timeInterval.toLong() * 60 * 1000)
                }
            } else {
                timer.cancel()
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
                val toast = Toast.makeText(this.context, "$phoneNumber:$message", Toast.LENGTH_LONG).show()
            })
        }

    }
}
