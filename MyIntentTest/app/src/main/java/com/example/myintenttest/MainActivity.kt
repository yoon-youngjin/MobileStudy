package com.example.myintenttest


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.myintenttest.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val REQUEST_CALL = 100
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }

    private fun init() {
        with(binding) {
            callBtn.setOnClickListener {
                callAction()
            }
            msgBtn.setOnClickListener {
                val message = Uri.parse("tel:010-14124-1414")
                val messageIntent = Intent(Intent.ACTION_SENDTO,message)
                messageIntent.putExtra("sms_body","ㅎㅇ")
                startActivity(messageIntent)

            }
            webBtn.setOnClickListener {
                val web = Uri.parse("http://www.naver.com")
                val webIntent = Intent(Intent.ACTION_VIEW,web)
                startActivity(webIntent)
            }
            mapBtn.setOnClickListener {
                val map = Uri.parse("geo : 123124124?z=16")
                val mapIntent = Intent(Intent.ACTION_VIEW,map)
                startActivity(mapIntent)
            }
        }
    }

    private fun callAction() {
        val call = Uri.parse("tel:010-2342-234324")
        val callIntent = Intent(Intent.ACTION_CALL,call)
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED) {
            callAlertDlg()
        }
        else startActivity(callIntent)

    }

    private fun callAlertDlg() {
        val builder =  AlertDialog.Builder(this)
        builder.setMessage("ㅎㅇㅎㅇ")
                .setTitle("권한설정")
                .setPositiveButton("확인") {
                    _,_ ->
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE),REQUEST_CALL)
                }
                .show()

    }


}