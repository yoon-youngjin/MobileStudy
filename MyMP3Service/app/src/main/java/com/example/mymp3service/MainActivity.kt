package com.example.mymp3service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import com.example.mymp3service.databinding.ActivityMainBinding

//<uses-permission android:name="android.permission.FOREGROUND_SERVICE"/> : 포그라운드 서비스 사용시 메니페스트에 추가, 알림 설정해줘야함
class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding

    lateinit var songlist:Array<String>
    lateinit var song:String
// runThread true일경우만 스레드 실행
    var runThread = false
    //프로그레스바 진행상황 스레드
   var thread:ProgressThread?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    fun init() {
        //초기화작업
        songlist = resources.getStringArray(R.array.songlist)

        song = songlist[0]

        binding.listview.setOnItemClickListener { parent, view, position, id ->
            song = songlist[position]
            startPlay()
        }

        binding.btnplay.setOnClickListener {
            startPlay()
        }
        binding.btnstop.setOnClickListener {
            stopPlay()
        }

        //broadcastreceiver 등록 -> com.example.MP3ACTIVITY이라고하는 브로드캐스트 발생시 receiver수신 -> onreceive 호출

        registerReceiver(receiver, IntentFilter("com.example.MP3ACTIVITY"))

        val intent = Intent(this,MyService::class.java)
        startService(intent)

    }

    private fun startPlay() {
        // 서비스로 노래 플레이정보 전달 -> service.onreceive 수신 -> service.startplay
        val serviceBRIntent = Intent("com.example.MP3SERVICE")
        serviceBRIntent.putExtra("mode","play")
        serviceBRIntent.putExtra("song",song)
        sendBroadcast(serviceBRIntent)

        runThread= true
        if(thread==null || !thread!!.isAlive) { // thread가 null이거나 thread가 alive가 아닌 경우
            binding.progressBar.progress=0
            thread = ProgressThread() // thread 생성
            thread!!.start()
        }
     }

    inner class ProgressThread:Thread() {
        override fun run() {
            while(runThread) {
                binding.progressBar.incrementProgressBy(1000)
                SystemClock.sleep(1000) // 예외발생해도 무시
                if(binding.progressBar.progress==binding.progressBar.max) {
                    runThread = false
                    binding.progressBar.progress = 0
                }
            }
        }
    }

    var receiver = object:BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val mode = intent!!.getStringExtra("mode")
            if(mode!=null) {
                when(mode) {
                    "play" -> {
                        binding.progressBar.max = intent.getIntExtra("duration",-1)
                        binding.progressBar.progress = 0
                    }

                    "stop" -> {
                        runThread = false
                        binding.progressBar.progress=0
                    }
                    //종료후 다시 실행시 oncreate부터 수행되므로 progressbar에 대한 상태정보 다시 받아옴 -> 노래는 실행중
                    "playing" -> {
                        runThread = true
                        binding.progressBar.max = intent.getIntExtra("duration",-1)
                        binding.progressBar.progress = intent.getIntExtra("currentPos",-1)
                        song = intent.getStringExtra("song")!!
                        if(thread==null || !thread!!.isAlive) {
                            thread = ProgressThread()
                            thread!!.start()
                        }
                    }
                }
            }
        }
    }

    private fun stopPlay() {
        val serviceBRIntent = Intent("com.example.MP3SERVICE")
        serviceBRIntent.putExtra("mode","stop")
        sendBroadcast(serviceBRIntent)

        runThread = false
        binding.progressBar.progress=0
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}