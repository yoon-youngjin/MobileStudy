package com.example.myengvoc.view.fragment

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.myengvoc.adapter.ViewPagerAdapter2
import com.example.myengvoc.data.MyData
import com.example.myengvoc.databinding.FragmentThirdBinding
import com.example.myengvoc.view.viewmodel.MyViewModel2
import java.util.*


class ThirdFragment : Fragment() {
    val viewModel2 : MyViewModel2 by activityViewModels()
    lateinit var adapter: ViewPagerAdapter2
    lateinit var tts: TextToSpeech
    var isTtsReady = false
    var binding: FragmentThirdBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        adapter = ViewPagerAdapter2(viewModel2.liveItems.value)
        binding!!.viewpager4.adapter = adapter
        adapter.itemClickListener = object : ViewPagerAdapter2.OnItemClickListener {
            override fun OnItemClick(holder: ViewPagerAdapter2.ViewHolder, view: View, data: MyData, position:Int) {

                if(holder.answerText.visibility==View.GONE)
                    holder.answerText.visibility = View.VISIBLE
                else
                    holder.answerText.visibility = View.GONE


            }
        }
        adapter.itemClickListener2 = object : ViewPagerAdapter2.OnItemClickListener {
            override fun OnItemClick(holder: ViewPagerAdapter2.ViewHolder, view: View, data: MyData, position:Int) {
                if(isTtsReady) {
                    tts.speak(holder.thirdText.text.toString(), TextToSpeech.QUEUE_ADD,null,null)
                }
            }
        }
    }


}