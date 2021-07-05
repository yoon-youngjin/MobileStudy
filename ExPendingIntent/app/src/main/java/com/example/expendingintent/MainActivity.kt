package com.example.expendingintent

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.expendingintent.databinding.ActivityMainBinding
import com.example.expendingintent.databinding.MypickerdlgBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    var memo = ""
    var hour =0
    var minute = 0
    var message =""
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }


    private fun init() {
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val dlgBinding = MypickerdlgBinding.inflate(layoutInflater)
            val dlgBuilder = AlertDialog.Builder(this)
            dlgBuilder.setView(dlgBinding.root)
                .setPositiveButton("확인") { _, _ ->
                    memo = dlgBinding.editText.text.toString()
                    hour = dlgBinding.mypicker.hour
                    minute = dlgBinding.mypicker.minute
                    message = hour.toString() + "시" + minute.toString() + "분 : " + memo
                    val timerTask = object : TimerTask() {
                        override fun run() {
                            makeNotification()
                        }

                    }
                    val timer = Timer()
                    timer.schedule(timerTask, 2000)
                }

                .setNegativeButton("취소") { _, _ ->
                }
                .show()
        }


    }
    fun makeNotification() {
        val id = "MyChannel"
        val name = "TimeCheckChannel"
        val notificationChannel = NotificationChannel(id,name,NotificationManager.IMPORTANCE_DEFAULT)



        val builder = NotificationCompat.Builder(this,id)
            .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
            .setContentText(message)
        val notification = builder.build()
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(notificationChannel)
        manager.notify(10,notification)
    }


}