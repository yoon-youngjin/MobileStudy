package com.example.engvoc.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.engvoc.adapter.ViewPagerAdapter
import com.example.engvoc.data.MyData
import com.example.engvoc.databinding.FragmentSecondBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class SecondFragment : Fragment() {
    lateinit var adapter: ViewPagerAdapter
    lateinit var tts: TextToSpeech
    lateinit var rdb: DatabaseReference
    lateinit var rdb2: DatabaseReference
    var isTtsReady = false
    var binding: FragmentSecondBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rdb = FirebaseDatabase.getInstance().getReference("datas/items")
        rdb2 = FirebaseDatabase.getInstance().getReference("datas/items2")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecondBinding.inflate(layoutInflater,container,false)
        init()
        initTTS()
        return binding!!.root
    }
    fun initTTS() {
        tts = TextToSpeech(context,TextToSpeech.OnInitListener {
            isTtsReady = true
            tts.language = Locale.US
        })
    }


    override fun onStop() {
        super.onStop()
        tts.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.shutdown()
    }

    private fun init() {
        val query = rdb.orderByKey()
        val option = FirebaseRecyclerOptions.Builder<MyData>()
            .setQuery(query, MyData::class.java)
            .build()

        adapter = ViewPagerAdapter(option)
        binding!!.viewpager4.adapter = adapter
        adapter.itemClickListener = object : ViewPagerAdapter.OnItemClickListener {
            override fun OnItemClick(holder: ViewPagerAdapter.ViewHolder, view: View) {

                if(holder.answerText.visibility==View.GONE)
                    holder.answerText.visibility = View.VISIBLE
                else
                    holder.answerText.visibility = View.GONE
            }
        }
        adapter.itemClickListener2 = object : ViewPagerAdapter.OnItemClickListener {
            override fun OnItemClick(holder: ViewPagerAdapter.ViewHolder, view: View) {
                if(isTtsReady) {
                    Toast.makeText(context,holder.answerText.text.toString(),Toast.LENGTH_SHORT).show()
                    tts.speak(holder.secondText.text.toString(),TextToSpeech.QUEUE_ADD,null,null)
                }
            }
        }
        adapter.itemClickListener3 = object :ViewPagerAdapter.OnItemClickListener {
            override fun OnItemClick(holder: ViewPagerAdapter.ViewHolder, view: View) {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("오답노트에 추가하시겠습니까?")
                builder.setPositiveButton("네") { _, _ ->
                    val item = MyData(holder.secondText.text.toString(),holder.answerText.text.toString())

                    rdb2.child(holder.secondText.text.toString()).setValue(item)
                }
                builder.setNegativeButton("아니요") {_,_->}.show()
            }
        }
        adapter.startListening()
    }
}