package com.example.engvoc.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.engvoc.R

class SplashActivity : AppCompatActivity() {
    val SPLASH_VIEW_TIME:Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val iv_bg = findViewById<ImageView>(R.id.imageView)

        iv_bg.alpha = 0f
        iv_bg.animate().setDuration(SPLASH_VIEW_TIME).alpha(1f).withEndAction {
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}