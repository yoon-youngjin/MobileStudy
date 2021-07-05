package com.example.myengvoc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class IntroActivity : AppCompatActivity() {
    val REQUEST_CODE =100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
      //  init()
    }

//    private fun init() {
//        val btn1 = findViewById<Button>(R.id.button)
//        val btn2 = findViewById<Button>(R.id.button2)
//
////        btn1.setOnClickListener {
////            val intent = Intent(this,MainActivity::class.java)
////            intent.putExtra("name","ㅎㅇㅎㅇ")
////            startActivity(intent)
////        }
//        btn2.setOnClickListener {
//            val intent = Intent(this,AddVocActivity::class.java)
//            startActivityForResult(intent,REQUEST_CODE)
//        }
//
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            REQUEST_CODE-> {
                if(resultCode == Activity.RESULT_OK) {
                    val str = data?.getSerializableExtra("data") as MyData
                    Toast.makeText(this, str.word, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}