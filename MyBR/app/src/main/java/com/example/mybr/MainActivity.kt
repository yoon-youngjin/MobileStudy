package com.example.mybr

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.mybr.databinding.ActivityMainBinding

//foreground수신
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var broadcastReceiver: BroadcastReceiver
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initPermission()
        init()
        // intent받는 작업 -> receiver에 의해서 activity실행시에는 msg정보 가지고 있고, 아닌경우(런처에 의해 실행) msg정보 x
        if(intent.hasExtra("msg")) {
            Toast.makeText(this,"gg",Toast.LENGTH_SHORT).show()
            val msg = intent.getStringExtra("msg")
            Toast.makeText(this,msg.toString(),Toast.LENGTH_SHORT).show()
        }

    }
    // 실행중에 intent정보 받는 함수
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if(intent!=null) {
            if (intent.hasExtra("msg")) {
                val msg = intent.getStringExtra("msg")
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initPermission() {
        //버전 check
        if(Build.VERSION.SDK_INT>=29) {
            if(!Settings.canDrawOverlays(this)) {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName"))
                startActivity(intent)
            }
        }

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.RECEIVE_SMS)==PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"문자 수신 동의함",Toast.LENGTH_SHORT).show()
        }
        else {
            //다이얼로그 뜸
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECEIVE_SMS),100)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==100) {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"문자 수신 동의함",Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this,"문자 수신 동의 필요함",Toast.LENGTH_SHORT).show()
            }
        }
    }

    //running전 broadcastreceiver를 register
    override fun onResume() {
        super.onResume()
        val filter = IntentFilter()
        //액션정보
        filter.addAction(Intent.ACTION_POWER_CONNECTED)
        
        //filter에 해당하는 액션 수신 -> ACTION_POWER_CONNECTED시에 onReceive로 전달   
        registerReceiver(broadcastReceiver,filter)    
    }

    override fun onPause() {
        super.onPause()
        //수신끝 resume/ pause , create/destroy
        unregisterReceiver(broadcastReceiver)
    }

    fun init() {
        broadcastReceiver = object :BroadcastReceiver() {
            //시스템에세 발생하는 메세지,액션 수신시에 onReceive로 전달 -> 수신시 행동 작성
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent!=null) { // intent여러가지일 수 있음
                    if(intent.action.equals(Intent.ACTION_POWER_CONNECTED))
                    Toast.makeText(context, "베터리 충전 시작", Toast.LENGTH_SHORT).show()
                }
                
            }
        }
        
    }
}