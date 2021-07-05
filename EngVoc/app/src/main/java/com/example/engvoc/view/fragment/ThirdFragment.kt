package com.example.engvoc.view.fragment

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.engvoc.adapter.ViewPagerAdapter2
import com.example.engvoc.data.MyData
import com.example.engvoc.databinding.FragmentThirdBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class ThirdFragment : Fragment() {
    lateinit var adapter: ViewPagerAdapter2
    lateinit var tts: TextToSpeech
    lateinit var rdb2: DatabaseReference
    var isTtsReady = false
    var binding: FragmentThirdBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        rdb2 = FirebaseDatabase.getInstance().getReference("datas/items2")
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentThirdBinding.inflate(layoutInflater,container,false)
        init()
        initTTS()
        return binding!!.root
    }

    private fun init() {
        val query = rdb2.orderByKey()
        val option = FirebaseRecyclerOptions.Builder<MyData>()
            .setQuery(query, MyData::class.java)
            .build()

        adapter = ViewPagerAdapter2(option)
        binding!!.viewpager4.adapter = adapter
        adapter.itemClickListener = object : ViewPagerAdapter2.OnItemClickListener {
            override fun OnItemClick(holder: ViewPagerAdapter2.ViewHolder,view:View) {

                if(holder.answerText.visibility==View.GONE)
                    holder.answerText.visibility = View.VISIBLE
                else
                    holder.answerText.visibility = View.GONE


            }
        }
        adapter.itemClickListener2 = object : ViewPagerAdapter2.OnItemClickListener {
            override fun OnItemClick(holder: ViewPagerAdapter2.ViewHolder, view: View) {
                if(isTtsReady) {
                    tts.speak(holder.thirdText.text.toString(), TextToSpeech.QUEUE_ADD,null,null)
                }
            }
        }
        adapter.startListening()
    }


}