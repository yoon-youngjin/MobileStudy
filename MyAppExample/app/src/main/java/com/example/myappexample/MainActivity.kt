package com.example.myappexample

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    val REQUESTCODE =100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun onClick(view: View) {
        btnAcition()
    }

    private fun btnAcition() {
        val intent = Intent("android.intent.action.myrecyclerview")
        if(ActivityCompat.checkSelfPermission(this,"com.example.myexvocintentfilter")!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf("com.example.myexvocintentfilter"),REQUESTCODE)
        }
        else {
            startActivity(intent)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==REQUESTCODE) {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                btnAcition()
            }
            else {
                finish()
            }
        }
    }
}