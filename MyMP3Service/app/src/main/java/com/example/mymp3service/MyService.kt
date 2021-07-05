package com.example.mymp3service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.app.NotificationCompat


// 서비스 구동 중에 액티비티와 서비스의 데이터 공유 -> service와 broadcastReceiver
//<uses-permission android:name="android.permission.FOREGROUND_SERVICE"/> -> foreground서비스 사용시 권한 요청만 해놓으면 된다.
// apk추출 : build -> generated signed bundle  / apk /next -> path지정 / 파일이름 지정 / keystore password 지정 -> release / finish-> 앱폴더 -> relase파일에 apk파일 생성
class MyService : Service() {
    
    lateinit var song:String
    var player :MediaPlayer?=null


    var manager : NotificationManager?=null
    var notificationid = 100


    fun makenotification() {
        val channel = NotificationChannel("channel1", "mp3Channel",NotificationManager.IMPORTANCE_DEFAULT)
        manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager?.createNotificationChannel(channel)
        val builder = NotificationCompat.Builder(this,"channel1")
                .setSmallIcon(R.drawable.ic_baseline_audiotrack_24)
                .setContentTitle("MP3")
                .setContentText("음악 플레이중")
                .setContentIntent(PendingIntent.getActivity(this,notificationid,Intent(this,MainActivity::class.java),PendingIntent.FLAG_UPDATE_CURRENT))
        val notification = builder.build()

        //makenotification호출되면 foreground시작
        startForeground(notificationid,notification)
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
    var receiver = object:BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            playControl(intent)
        }
    }
    //상태 주고받아야한다
    private fun playControl(intent: Intent?) {
        val mode = intent!!.getStringExtra("mode")
        if(mode!=null) {
            when(mode) {
                "play" -> {
                    song= intent.getStringExtra("song")!!
                    startPlay(song)
                    makenotification()
                }
                "stop" -> {
                    stopPlay()
                    //foreground 종료
                    stopForeground(true)
                }
            }
        }

    }

    override fun onCreate() {
        super.onCreate()
        // filter로 com.example.MP3SERVICE주었으므로 service의broadcasterreceiver에 전달하고 싶은 경우 필터를 com.example.MP3SERVICE로주면 -> onReceive호출
        registerReceiver(receiver, IntentFilter("com.example.MP3SERVICE"))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    fun startPlay(sname:String) {
        song= sname
        // 노래에관한 resource get / raw에서 찾겠다.
        val songid = resources.getIdentifier(song,"raw",packageName)


        //player가 playing중인경우  -> 다른곡재생
        if(player!=null && player!!.isPlaying) {
            player!!.stop()
            player!!.reset()
            player!!.release()
            player = null

        }

        //player가 playing중이지 않은 경우
        player = MediaPlayer.create(this,songid)
        player!!.start()

        // 곡정보를 mainActivity에 넘겨야 max값을 알 수있다. playtime정보 -> mediaplayer. duration
        val mainBRIntent = Intent("com.example.MP3ACTIVITY")
        mainBRIntent.putExtra("mode","play")
        mainBRIntent.putExtra("duration",player!!.duration)
        sendBroadcast(mainBRIntent)

        //노래가 끝난 경우 이벤트 처리 -> broadcast발생
        player!!.setOnCompletionListener {
            val mainBRIntent = Intent("com.example.MP3ACTIVITY")
            mainBRIntent.putExtra("mode","stop")
            sendBroadcast(mainBRIntent)
            stopPlay()
        }
    }
    //oncreate다음 onstartCommand호출 -> 앱종료후 progressbar상태 유지하기 위해 status()함수로 현재 음악 상태 메인으로 전달

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 상태 전달
        status()
        return START_STICKY // 강제 종료 시 다시 시작
    }
    fun status() {

        if(player!=null && player!!.isPlaying) {
            //액티비티 새로 생성되었으므로 song관련 정보 다시 보내야댐
            val mainBRIntent = Intent("com.example.MP3ACTIVITY")
            mainBRIntent.putExtra("mode","playing")
            mainBRIntent.putExtra("song",song)
            //현재 노래 위치
            mainBRIntent.putExtra("currentPos",player!!.currentPosition)
            mainBRIntent.putExtra("duration",player!!.duration)
            sendBroadcast(mainBRIntent)
        }
    }

    fun stopPlay() {
        if(player!=null && player!!.isPlaying) {
            player!!.stop()
            player!!.reset()
            player!!.release()
            player = null
        }
    }
}