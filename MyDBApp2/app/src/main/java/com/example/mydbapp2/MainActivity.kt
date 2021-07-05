package com.example.mydbapp2

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.mydbapp2.databinding.ActivityMainBinding
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var myDBHelper: MyDBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initDB()
        getAllRecord()

    }

    private fun initDB() {
        val dbfile = getDatabasePath("mydb.db")
        if(!dbfile.parentFile.exists()) {// databases폴더 확인
            dbfile.parentFile.mkdir()// databases폴더 생성
        }
        if(!dbfile.exists()) {
            val file = resources.openRawResource(R.raw.mydb)
            val fileSize = file.available()//파일 크기정보 get
            val buffer = ByteArray(fileSize)// 파일 크가만큼 bite
            file.read(buffer) // file읽어서 buffer에 저장
            file.close()
            dbfile.createNewFile()
            val output = FileOutputStream(dbfile)
            output.write(buffer)
            output.close()

        }

    }

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
        myDBHelper = MyDBHelper(this)
        binding.apply {
            testsql.addTextChangedListener { //textWatcher ,text입력할때 마다 수행
                val pname = it.toString()
                val result = myDBHelper.findProduct2(pname)
                // editText에 입력시 해당 name인 product나열 기능
            }
            insertbtn.setOnClickListener {
                val name = pNameEdit.text.toString()
                val quantity = pQuantityEdit.text.toString().toInt()
                val product = Product(0,name,quantity)
                val result = myDBHelper.insertProduct(product)
                if(result) {
                    getAllRecord()
                    Toast.makeText(this@MainActivity,"Data INSERT SUCCESS",Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this@MainActivity,"Data INSERT FAILED",Toast.LENGTH_SHORT).show()

                }
                clearEditText()

            }

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

            updatebtn.setOnClickListener {
                val pid = pIdEdit.text.toString().toInt()
                val name = pNameEdit.text.toString()
                val quantity = pQuantityEdit.text.toString().toInt()
                val product = Product(pid,name,quantity)
                val result = myDBHelper.updateProduct(product)
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