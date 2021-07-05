package com.example.myengvoc.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myengvoc.R
import com.example.myengvoc.adapter.MyFragStateAdapter
import com.example.myengvoc.data.MyData
import com.example.myengvoc.databinding.ActivityMainBinding
import com.example.myengvoc.view.viewmodel.MyViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.PrintStream

class MainActivity : AppCompatActivity() {


    //없는 단어 검색 예외 처리

    //    val handler = CoroutineExceptionHandler { coroutineScope,exception ->
//        Toast.makeText(this,"없는 단어입니다.", Toast.LENGTH_SHORT).show()
//    }
    val scope = CoroutineScope(Dispatchers.IO)
    lateinit var word2: String
    lateinit var binding: ActivityMainBinding
    val viewModel: MyViewModel by viewModels()
    val textarr = arrayListOf<String>("Voc", "VocADD", "Quiz", "Wrong")
    val imageArr =
        arrayListOf<Int>(R.drawable.book, R.drawable.add, R.drawable.quiz, R.drawable.wrong)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.voc -> {
            }
            R.id.card -> {
                startActivity(Intent(this, SecondActivity::class.java))
                finish()
            }
            R.id.wrong -> {
                startActivity(Intent(this, ThirdActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        val data = binding.searchText.text
        binding.viewPager.adapter = MyFragStateAdapter(this)
        TabLayoutMediator(binding.tablayout, binding.viewPager) { tab, position ->
            tab.text = textarr[position]
            tab.setIcon(imageArr[position])
        }.attach()

        binding.searchBtn.setOnClickListener {


            val url = "https://dic.daum.net/search.do?q=" + data + "&dic=eng&search_first=Y"

            scope.launch {
                try {

                    val doc = Jsoup.connect(url).get()

                    withContext(Dispatchers.Main) {
                        val word =
                            doc.select(".cleanword_type > .list_search > li > .txt_search").first()
                        word2 = word.text()

                        val builder = AlertDialog.Builder(this@MainActivity)
                        builder.setMessage("단어장에 추가하시겠습니까?")
                        builder.setPositiveButton("네") { _, _ ->

                            viewModel.add(MyData(data.toString(), word2.toString()))
                            val output = PrintStream(openFileOutput("out1.txt", MODE_APPEND))
                            output.println(data)
                            output.println(word2.toString())
                            output.close()
                        }.create()
                         builder.setNegativeButton("아니요") { _, _ -> }.create()
                        builder.show()
                    }
                } catch (e: java.lang.Exception) {

                    Log.e("delet", "없는 단어 입니다")
                }
            }
        }
    }
}



//    if (word2.isNotEmpty()) {
//
//        val builder = AlertDialog.Builder(this)
//        builder.setMessage("단어장에 추가하시겠습니까?")
//        builder.setPositiveButton("네") { _, _ ->
//
//            viewModel.add(MyData(data.toString(), word2.toString()))
//            val output = PrintStream(openFileOutput("out1.txt", MODE_APPEND))
//            output.println(data)
//            output.println(word2)
//            output.close()
//        }.create()
//        // builder.setNegativeButton("아니요") { _, _ -> }.create()
//        builder.show()
//    }


//            catch (e:Exception) {
//                Toast.makeText(applicationContext,"없는 단어입니다.", Toast.LENGTH_SHORT).show()
//            }





//    private fun writeFile(word1: String, meaning1: String) {
//        val output = PrintStream(openFileOutput("out1.txt", MODE_APPEND))
//        output.println(word1)
//        output.println(meaning1)
//        output.close()
//    }


