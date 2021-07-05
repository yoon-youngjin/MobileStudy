package com.example.myfbtest

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        init()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.intro -> {
                val fragment = supportFragmentManager.beginTransaction()
                //fragment.addToBackStack(null)
                fragment.replace(R.id.frame, ItemFragment2())
                fragment.commit()

            }

        }
        return super.onOptionsItemSelected(item)
    }




    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onResume() {
        super.onResume()

    }

    private fun init() {

        val fragment = supportFragmentManager.beginTransaction()
        //fragment.addToBackStack(null)
        fragment.replace(R.id.frame, ItemFragment2())
        fragment.commit()


    }
}