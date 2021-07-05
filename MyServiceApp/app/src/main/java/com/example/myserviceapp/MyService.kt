package com.example.myserviceapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MyService : Service() {
    // 재실행시 onStartCommand()부터 실행 , destroy -> onCreate , destroyx -> onStartCommand
    var thread:Thread?=null
    var num = 0
    // 사용 안할것
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
    //
    override fun onCreate() {
        super.onCreate()
        Log.i("MyService","OnCreate()")
    }
    //create다음 호출
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
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
        Log.i("MyService","onStartCommand()")
        //return 값으로 서비스 중지 상황 처리 -> START_STICK 종료시 다시 시작,intent=null
        // START_NOT_STICKY : 서비스가 강제 종료되어도 재시작x
        // START_REDELIVER_INTENT : 특정 동작에 대해 stopself()호출하기 전에 종료되면, 시스템이 재시작 시켜주며, 해당 intent값을 그대로 유지
        return START_STICKY
    }
    //종료시 호출
    override fun onDestroy() {
        Log.i("MyService","onDestroy()")
        if(thread!=null) {
            thread!!.interrupt()
            // sleep상태에서 interrupt시 에러
        }
        thread=null
        super.onDestroy()
    }
}