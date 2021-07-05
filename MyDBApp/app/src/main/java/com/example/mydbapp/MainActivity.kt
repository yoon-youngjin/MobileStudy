package com.example.mydbapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.mydbapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var myDBHelper: MyDBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        //create시 db에 데이터 있는경우 바로 화면 출력
        getAllRecord()

    }
    // 테이블 내용 가져오는 함수
    fun getAllRecord() {
        myDBHelper.getAllRecord()
    }

    fun clearEditText() {
        binding.apply {
            pIdEdit.text.clear()
            pNameEdit.text.clear()
            pQuantityEdit.text.clear()
        }
    }
    private fun init() {
        
        //helper객체 생성
        myDBHelper = MyDBHelper(this)
        binding.apply {


            // editText에 입력시 해당 name인 product나열 기능
            testsql.addTextChangedListener { //textWatcher ,text입력할때 마다 수행
                val pname = it.toString()
                val result = myDBHelper.findProduct2(pname)

            }
            insertbtn.setOnClickListener {
                // id -> autoincrement 자동 생성 , 이름 ,수량 정보 get
                val name = pNameEdit.text.toString()
                val quantity = pQuantityEdit.text.toString().toInt()
                val product = Product(0,name,quantity)
                val result = myDBHelper.insertProduct(product)
                if(result) {
                    // 입력 완료 후 입력데이터 반영해서 화면 출력
                    getAllRecord()
                    Toast.makeText(this@MainActivity,"Data INSERT SUCCESS",Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this@MainActivity,"Data INSERT FAILED",Toast.LENGTH_SHORT).show()
                }
                clearEditText()

            }
            //name 정보로 찾기
            findbtn.setOnClickListener {
                val name = pNameEdit.text.toString()
                val result = myDBHelper.findProduct(name)
                if(result) {
                    Toast.makeText(this@MainActivity,"RECORD FOUND",Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this@MainActivity,"NO MATCH FOUND",Toast.LENGTH_SHORT).show()

                }

            }

            deletebtn.setOnClickListener {
                val pid = pIdEdit.text.toString()
                val result = myDBHelper.deletProduct(pid)
                if(result) {

                    Toast.makeText(this@MainActivity,"Data DELETE SUCCESS",Toast.LENGTH_SHORT).show()
                }
                else {

                    Toast.makeText(this@MainActivity,"Data DELETE FAILED",Toast.LENGTH_SHORT).show()

                }
                getAllRecord()//화면에 다시 보여주는경우
                clearEditText()

            }

            //id값을 가지고 update
            updatebtn.setOnClickListener {
                val pid = pIdEdit.text.toString().toInt()
                val name = pNameEdit.text.toString()
                val quantity = pQuantityEdit.text.toString().toInt()
                val product = Product(pid,name,quantity)
                val result = myDBHelper.updateProduct(product) // 위 내용으로 갱신
                if(result) {
                    getAllRecord()

                    Toast.makeText(this@MainActivity,"Data UPDATE SUCCESS",Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this@MainActivity,"Data UPDATE FAILED",Toast.LENGTH_SHORT).show()

                }
                clearEditText()


            }

        }
    }
}