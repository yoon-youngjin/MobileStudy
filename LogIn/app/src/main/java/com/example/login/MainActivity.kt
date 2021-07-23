package com.example.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.login.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val DATABASE_VERSION = 1
    val DATABASE_NAME = "LocalDB.db"
    private lateinit var binding: ActivityMainBinding
    private lateinit var localDB: LocalDB
    //    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)    // 뷰바인딩 사용
        val view = binding.root
        setContentView(view)

        localDB= LocalDB(this, DATABASE_NAME,null, DATABASE_VERSION) // SQLite 모듈 생성

        binding.btnLogin.setOnClickListener { view->
            val id = binding.editId.text.toString()
            val passwd = binding.editPw.text.toString()
            val exist = localDB.logIn(id,passwd) // 로그인 실행
            if(exist){ // 로그인 성공
                val intent =Intent(this,SecondActivity::class.java)
                startActivity(intent)
            }else{ // 실패
                Toast.makeText(this@MainActivity, "아이디나 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnRegister.setOnClickListener { view->
            val intent =Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onDestroy() {// 엑티비티가 종료시 close
        localDB.close()
        super.onDestroy()
    }
}
