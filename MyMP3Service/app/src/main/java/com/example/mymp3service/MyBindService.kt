package com.example.mymp3service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MyBindService : Service() {

    lateinit var song:String
    var player : MediaPlayer?=null
    var binder = MyBinder()
    var manager : NotificationManager?=null
    var notificationid = 100

    fun makenotification() {
        val channel = NotificationChannel("channel1", "mp3Channel", NotificationManager.IMPORTANCE_DEFAULT)
        manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager?.createNotificationChannel(channel)
        val builder = NotificationCompat.Builder(this,"channel1")
                .setSmallIcon(R.drawable.ic_baseline_audiotrack_24)
                .setContentTitle("MP3")
                .setContentText("음악 플레이중")
        val notification = builder.build()
        //makenotification호출되면 foreground시작
        startForeground(notificationid,notification)
    }

    // iBinder(인터페이스)객체 리턴 , binder로 부터 서비스 획득하여 서비스함수 사용
    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    inner class MyBinder: Binder() {
        fun getService() : MyBindService = this@MyBindService
    }


    fun getMaxDuration() :Int {
        if (player != null)
            return player!!.duration
        else return 0
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

        //노래가 끝난 경우 이벤트 처리 -> broadcast발생
        player!!.setOnCompletionListener {
            stopPlay()
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