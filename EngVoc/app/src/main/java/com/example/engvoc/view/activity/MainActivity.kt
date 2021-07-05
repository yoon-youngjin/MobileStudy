package com.example.engvoc.view.activity

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.GravityCompat
import com.example.engvoc.R
import com.example.engvoc.adapter.MyFragStateAdapter
import com.example.engvoc.data.MyData
import com.example.engvoc.databinding.ActivityMainBinding
import com.example.engvoc.view.viewmodel.MyViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.util.*



class MainActivity : AppCompatActivity() {

    val viewModel: MyViewModel by viewModels()
    val scope = CoroutineScope(Dispatchers.IO)
    lateinit var word2: String
    lateinit var binding: ActivityMainBinding
    lateinit var rdb: DatabaseReference
    lateinit var rdb2: DatabaseReference
    lateinit var value: Any
    lateinit var value2: Any
    val random = Random()
    lateinit var num: IntArray
    val textarr = arrayListOf<String>("Voc", "VocADD", "Quiz", "Wrong")
    val imageArr =
        arrayListOf<Int>(R.drawable.book, R.drawable.add, R.drawable.quiz, R.drawable.wrong)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rdb = FirebaseDatabase.getInstance().getReference("datas/items")
        rdb2 = FirebaseDatabase.getInstance().getReference("datas/items2")
        viewModel.liveData.value = 0
        initData()
        init()
    }

    private fun initData() {
        val scan = Scanner(resources.openRawResource(R.raw.words))
        while (scan.hasNextLine()) {
            val word = scan.nextLine()
            val mean = scan.nextLine()
            val item = MyData(word, mean)
            rdb.child(viewModel.liveData.value.toString()).setValue(item)
            viewModel.liveData.value = viewModel.liveData.value as Int + 1
        }
        var num1 = viewModel.liveData.value!!.toInt()
        var num2 = random.nextInt(num1)

        var num3 = random.nextInt(num1)
        while (true) {
            if (num2 == num3) {
                num3 = random.nextInt(num1)
            } else break
        }
        num = intArrayOf(num2, num3)
        scan.close()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                binding.drawer.openDrawer(GravityCompat.START)
            }

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

    override fun onDestroy() {
        if (binding.linear.visibility == View.VISIBLE && binding.linear2.visibility != View.VISIBLE) {
            makeNotification(binding.textView13.text.toString())
            val item = MyData(binding.textView13.text.toString(), value.toString())
            rdb2.child(binding.textView13.text.toString()).setValue(item)
        }

        if (binding.linear2.visibility == View.VISIBLE && binding.linear.visibility == View.VISIBLE) {
            makeNotification(binding.textView11.text.toString())
            val item = MyData(binding.textView11.text.toString(), value2.toString())
            rdb2.child(binding.textView11.text.toString()).setValue(item)
        }

        if (binding.linear.visibility == View.VISIBLE && binding.linear2.visibility == View.VISIBLE) {
            val item = MyData(binding.textView13.text.toString(), value.toString())
            rdb2.child(binding.textView13.text.toString()).setValue(item)

            val item2 = MyData(binding.textView11.text.toString(), value2.toString())
            rdb2.child(binding.textView13.text.toString()).setValue(item2)

            makeNotification2(
                binding.textView13.text.toString(),
                binding.textView11.text.toString()
            )
        }
        super.onDestroy()

    }

    private fun makeNotification(value: String) {
        val id = "MyChannel"
        val name = "MyEngVoc"
        val notificationChannel =
            NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.enableVibration(true)


        val builder = NotificationCompat.Builder(this, id)
            .setSmallIcon(R.drawable.book)
            .setContentTitle("Today's Voc Fail")
            .setContentText("오답노트에 " + "$value" + "가 추가되었습니다.")
            .setAutoCancel(true)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(notificationChannel)
        val notification = builder.build()
        manager.notify(11, notification)

    }

    private fun makeNotification2(value1: String, value2: String) {
        val id = "MyChannel"
        val name = "MyEngVoc"
        val notificationChannel =
            NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.enableVibration(true)

        val builder = NotificationCompat.Builder(this, id)
            .setSmallIcon(R.drawable.book)
            .setContentTitle("Today's Voc Fail")
            .setContentText("오답노트에 " + "$value1" + ",$value2" + "가 추가되었습니다.")
            .setAutoCancel(true)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(notificationChannel)
        val notification = builder.build()
        manager.notify(11, notification)
    }


    private fun init() {
        rdb.child(num[0].toString()).child("word")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot?.value
                    binding.textView13.text = "$value"
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("cancel", "실패")
                }
            })
        rdb.child(num[0].toString()).child("mean")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    value = snapshot.value!!
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("cancel", "실패")
                }
            })


        binding.button.setOnClickListener {
            if (binding.editText1.text.toString().equals(value.toString())) {
                binding.linear.visibility = View.GONE
            }
        }
        rdb.child(num[1].toString()).child("word")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot?.value
                    binding.textView11.text = "$value"

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("cancel", "실패")
                }
            })

        rdb.child(num[1].toString()).child("mean")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    value2 = snapshot.value!!
                    Log.e("text", value.toString())
                    if (binding.editText2.text.toString().equals(value.toString())) {
                        Log.e("text1", value.toString())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("cancel", "실패")
                }
            })
        binding.button2.setOnClickListener {
            if (binding.editText2.text.toString().equals(value2.toString())) {
                binding.linear2.visibility = View.GONE
            }
        }

            supportActionBar?.setDisplayHomeAsUpEnabled(true) // 드로어를 꺼낼 홈 버튼 활성화
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24) // 홈버튼 이미지 변경
            supportActionBar?.setDisplayShowTitleEnabled(false) // 툴바에 타이틀 안보이게


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
                                doc.select(".cleanword_type > .list_search > li > .txt_search")
                                    .first()
                            word2 = word.text()
                            Toast.makeText(applicationContext,word2,Toast.LENGTH_SHORT).show()

                            val builder = AlertDialog.Builder(this@MainActivity)
                            builder.setMessage("단어장에 추가하시겠습니까?")
                            builder.setPositiveButton("네") { _, _ ->

                                rdb.child(viewModel.liveData.value.toString())
                                    .setValue(
                                        MyData(
                                            binding.searchText.text.toString(),
                                            word2.toString()
                                        )
                                    )
                                viewModel.liveData.value = viewModel.liveData.value as Int + 1
                            }
                            builder.setNegativeButton("아니요") { _, _ ->
                            }.show()


                        }
                    } catch (e: java.lang.Exception) {
                        Log.e("delet", "없는 단어 입니다")

                    }

                }
            }


        }

    }





