package com.example.mp3player

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    var mediaPlayer:MediaPlayer ?=null
    var vol = 0.0f
    var flag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        val imageView = findViewById<VoulmeControlView>(R.id.imageView)
        imageView.setVolumeListener(object :VoulmeControlView.VolumeListener {
            override fun onChanged(angle: Float) {
                if(angle>0) {
                    vol = angle/360

                }
                else {
                    vol = (360+angle)/360
                    }
                mediaPlayer?.setVolume(vol,vol)
            }

        })
        val playBtn = findViewById<Button>(R.id.playBtn)
        val pauseBtn = findViewById<Button>(R.id.pauseBrn)
        val stopBtn = findViewById<Button>(R.id.stopBtn)
        playBtn.setOnClickListener {
            if(mediaPlayer==null) {
                mediaPlayer = MediaPlayer.create(this,R.raw.song)
                mediaPlayer?.setVolume(vol,vol)

            }
            mediaPlayer?.start()
            flag=true
        }
        pauseBtn.setOnClickListener {
            mediaPlayer?.pause()
            flag=false
        }
        stopBtn.setOnClickListener {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer=null
            flag = false
        }

    }

    override fun onResume() {
        if(flag)
            mediaPlayer?.start()
        super.onResume()
    }

    override fun onPause() {
        mediaPlayer?.pause()
        super.onPause()
    }
}