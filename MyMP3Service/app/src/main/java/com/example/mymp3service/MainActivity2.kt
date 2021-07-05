package com.example.mymp3service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import com.example.mymp3service.databinding.ActivityMain2Binding
// <service
//            android:name=".MyBindService"
//            android:enabled="true"
//            android:exported="true"></service>
// bindservice -> broadcastRecevier사용 x binding이 되면 엑티비티에서 서비스 함수 호출가능 -> broadcastreceiver사용안해도됨
class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    lateinit var songlist: Array<String>
    lateinit var song: String
    lateinit var myService:MyBindService
    //binding 상태 확인
    var mBound = false


    
    val connection = object:ServiceConnection {
        // 연결 o
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MyBindService.MyBinder // 형변환
            myService = binder.getService() // 서비스 획득
            mBound = true
        }
        // 연결 x
        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
        }
    }

    // runThread true일경우만 스레드 실행
    var runThread = false

    //프로그레스바 진행상황 스레드
    var thread: ProgressThread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun init() {
        //초기화작업
        songlist = resources.getStringArray(R.array.songlist)
        song = songlist[0]
        binding.listview.setOnItemClickListener { parent, view, position, id ->
            song = songlist[position]
            myService.makenotification()
            startPlay()


        }

        binding.btnplay.setOnClickListener {
            myService.makenotification()
            startPlay()
        }
        binding.btnstop.setOnClickListener {
            myService.stopForeground(true)
            stopPlay()
        }

        val intent = Intent(this, MyBindService::class.java)
        //flag값 BIND_AUTO_CREATE -> 비정사적 종료시 자동으로 다시 바인딩
        bindService(intent,connection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mBound) {
            //노래 종료
            stopPlay()
            //연결 해제
            unbindService(connection)
        }
        mBound=false
        
    }

    private fun startPlay() {

        // 서비스로 노래 플레이정보 전달 -> service.onreceive 수신 -> service.startplay
        runThread = true
        if (thread == null || !thread!!.isAlive) { // thread가 null이거나 thread가 alive가 아닌 경우 -> 처음 누른 상태

            if(mBound) {
                myService.startPlay(song)
                binding.progressBar.progress = 0
                binding.progressBar.max = myService.getMaxDuration()
                thread = ProgressThread() // thread 생성
                thread!!.start()
            }
        }
        else {
            if(mBound) { // 재생 중에 다른 곡 누른 경우
                myService.stopPlay() // 기존 곡 stop , thread는 진행중
                myService.startPlay(song)
                binding.progressBar.progress = 0
                binding.progressBar.max = myService.getMaxDuration()
            }
        }
    }

    inner class ProgressThread : Thread() {
        override fun run() {
            while (runThread) {
                binding.progressBar.incrementProgressBy(1000)
                SystemClock.sleep(1000) // 예외발생해도 무시
                if (binding.progressBar.progress == binding.progressBar.max) {
                    runThread = false
                    binding.progressBar.progress = 0
                }
            }
        }
    }

    private fun stopPlay() {

        if(mBound)
            myService.stopPlay()

        runThread = false
        binding.progressBar.progress = 0
    }


}