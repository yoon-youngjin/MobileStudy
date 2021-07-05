package com.example.myengvoc.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myengvoc.R
import com.example.myengvoc.data.MyData
import com.example.myengvoc.databinding.ActivityThirdBinding
import com.example.myengvoc.view.fragment.ThirdFragment
import com.example.myengvoc.view.viewmodel.MyViewModel2
import java.util.*

class ThirdActivity : AppCompatActivity() {
    lateinit var binding:ActivityThirdBinding
    val thirdFragment = ThirdFragment()
    val viewModel2: MyViewModel2 by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        init()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.voc -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            R.id.card -> {
                startActivity(Intent(this, SecondActivity::class.java))
                finish()
            }
            R.id.wrong ->{}
        }
        return super.onOptionsItemSelected(item)
    }

    fun init() {

            val fragment = supportFragmentManager.beginTransaction()
            //fragment.addToBackStack(null)
            fragment.replace(R.id.framelayout2,thirdFragment)
            fragment.commit()
        }
    private fun initData() {
            try {
                val scan2 = Scanner(openFileInput("out2.txt"))
                readFileScan(scan2)
            } catch (e: Exception) { }
        }

    private fun readFileScan(scan: Scanner) {
            while (scan.hasNextLine()) {
                val word = scan.nextLine()
                val meaning = scan.nextLine()
                viewModel2?.add(MyData(word, meaning))
            }
        }
    }

