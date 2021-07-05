package com.example.engvoc.view.activity


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.engvoc.R
import com.example.engvoc.databinding.ActivitySecondBinding
import com.example.engvoc.view.fragment.SecondFragment

class SecondActivity : AppCompatActivity() {
    val secondFragment = SecondFragment()
    lateinit var binding: ActivitySecondBinding

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        init()
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
    fun init() {
        val fragment = supportFragmentManager.beginTransaction()
        //fragment.addToBackStack(null)
        fragment.replace(R.id.framelayout, secondFragment)
        fragment.commit()
    }
}