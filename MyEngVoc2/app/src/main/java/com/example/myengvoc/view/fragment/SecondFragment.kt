package com.example.myengvoc.view.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.myengvoc.adapter.ViewPagerAdapter
import com.example.myengvoc.data.MyData
import com.example.myengvoc.databinding.FragmentSecondBinding
import com.example.myengvoc.view.viewmodel.MyViewModel
import java.io.PrintStream
import java.util.*


class SecondFragment : Fragment() {
    lateinit var adapter: ViewPagerAdapter
    lateinit var tts: TextToSpeech
    var isTtsReady = false
    val viewModel : MyViewModel by activityViewModels()
    var binding:FragmentSecondBinding?=null
    private fun writeFile(word1: String, meaning1: String) {
        val output = PrintStream(activity?.openFileOutput("out2.txt", Context.MODE_APPEND))
        output.println(word1)
        output.println(meaning1)
        output.close()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        adapter = ViewPagerAdapter(viewModel.liveItems.value)
        binding!!.viewpager4.adapter = adapter
        adapter.itemClickListener = object : ViewPagerAdapter.OnItemClickListener {
            override fun OnItemClick(holder: ViewPagerAdapter.ViewHolder, view: View,data:MyData,position:Int) {

                if(holder.answerText.visibility==View.GONE)
                    holder.answerText.visibility = View.VISIBLE
                else
                    holder.answerText.visibility = View.GONE


            }
        }
        adapter.itemClickListener2 = object : ViewPagerAdapter.OnItemClickListener {
            override fun OnItemClick(holder: ViewPagerAdapter.ViewHolder, view: View,data:MyData,position:Int) {
                if(isTtsReady) {
                    Toast.makeText(context,holder.answerText.text.toString(),Toast.LENGTH_SHORT).show()
                    tts.speak(holder.secondText.text.toString(),TextToSpeech.QUEUE_ADD,null,null)
                }
            }
        }
        adapter.itemClickListener3 = object :ViewPagerAdapter.OnItemClickListener {
            override fun OnItemClick(holder: ViewPagerAdapter.ViewHolder, view: View,data:MyData,position:Int) {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("오답노트에 추가하시겠습니까?")
                builder.setPositiveButton("네") { _, _ ->
                    writeFile(data.word, data.mean)
                }
                builder.setNegativeButton("아니요") {_,_->}.show()
            }
        }


//        binding!!.viewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                Toast.makeText(context,position.toString(),Toast.LENGTH_SHORT).show()
//                var current = binding!!.viewpager2.currentItem
//                if(current==adapter.itemCount-1) {
//                    binding!!.viewpager2.setCurrentItem(0,false)
//                }
//                else {
//                    binding!!.viewpager2.setCurrentItem(current+1,false)
//                }
//                    super.onPageSelected(position)
//            }
//        })
    }




}