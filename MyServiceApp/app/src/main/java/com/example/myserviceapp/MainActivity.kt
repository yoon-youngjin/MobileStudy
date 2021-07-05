package com.example.myserviceapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myserviceapp.databinding.ActivityMainBinding
//<service
//            android:name=".MyService"
//            android:enabled="true"
//            android:exported="true"></service>
// 서비스는 manifest에 반드시 등록되어야 한다.
class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    var thread:Thread?=null
    var num = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun init() {
        binding.btnStartThread.setOnClickListener {

            if(thread==null) {
                thread = object:Thread("MyThread") {
                    override fun run() {
                        // 앱 다시 실행시 기존 쓰레드와 재실행시 생성된 쓰레드는 별개 -> 재실행시 stop버튼 작동x -> 서비스는 가능
                        try {
                            for (i in 0..10) {
                                Log.i("MyThread $num", "Count : $i")
                                Thread.sleep(1000)
                            }
                        }catch (e:InterruptedException) {
                            //현재쓰레드 종료하겠다.
                            Thread.currentThread().interrupt()
                        }
                        thread= null
                    }
                }
                thread!!.start()
                num++
            }


        }
        binding.btnStopThread.setOnClickListener {
            if(thread!=null) {
                thread!!.interrupt()
                // sleep상태에서 interrupt시 에러
            }
            thread=null
        }
        binding.btnStartService.setOnClickListener {
            //intent정보를 통해 서비스 실행
            val intent = Intent(this,MyService::class.java)
            startService(intent)

        }
        binding.btnStopService.setOnClickListener {
            val intent = Intent(this,MyService::class.java)
            stopService(intent)

        }
    }


}