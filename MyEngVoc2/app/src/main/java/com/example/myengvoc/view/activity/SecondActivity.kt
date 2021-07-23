package com.example.myengvoc.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myengvoc.R
import com.example.myengvoc.data.MyData
import com.example.myengvoc.databinding.ActivitySecondBinding
import com.example.myengvoc.view.fragment.SecondFragment
import com.example.myengvoc.view.viewmodel.MyViewModel
import java.util.*

class SecondActivity : AppCompatActivity() {
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    lateinit var binding:ActivitySecondBinding
    val viewModel: MyViewModel by viewModels()
    val secondFragment = SecondFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        init()
        initData()
        setContentView(binding.root)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.voc -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            R.id.card -> {}

            R.id.wrong ->{
                    startActivity(Intent(this,ThirdActivity::class.java))
                    finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun initData() {
        try {
            val scan2 = Scanner(openFileInput("out1.txt"))
            readFileScan(scan2)
        } catch (e: Exception) { }
        val scan = Scanner(resources.openRawResource(R.raw.words))
        readFileScan(scan)
    }


    private fun readFileScan(scan: Scanner) {
        while (scan.hasNextLine()) {
            val word = scan.nextLine()
            val meaning = scan.nextLine()
            viewModel?.add(MyData(word, meaning))
        }
    }

    fun init() {
        val fragment = supportFragmentManager.beginTransaction()
        //fragment.addToBackStack(null)
        fragment.replace(R.id.framelayout,secondFragment)
        fragment.commit()
    }
}