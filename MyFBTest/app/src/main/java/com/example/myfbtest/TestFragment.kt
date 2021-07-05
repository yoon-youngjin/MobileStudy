package com.example.myfbtest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.myfbtest.databinding.FragmentTestBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.concurrent.thread


class TestFragment : Fragment() {
    val viewModel: MyViewModel by activityViewModels()

    var binding: FragmentTestBinding? = null
    var TestInfo_ArrayList: ArrayList<TestInfo> = ArrayList()
    lateinit var adapter: TestRecyclerViewAdapter

    var flag: Boolean = true
    var total = 0
    var started = true





    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentTestBinding.inflate(layoutInflater, container, false)

        initRecycleView()
        firebaseDatainit()
        init()

        return binding!!.root
    }




    private fun initRecycleView() {
        adapter = TestRecyclerViewAdapter(TestInfo_ArrayList)
        binding!!.viewpager.adapter = adapter

    }

    private fun firebaseDatainit() {
            val firestore = FirebaseFirestore.getInstance()
            try {
                firestore.collection(viewModel.getValue())
                    .get()
                    .addOnSuccessListener { result ->
                        for (doc in result) {
                            TestInfo_ArrayList.add(doc.toObject(TestInfo::class.java))
                        }
                        Log.e("success", "success")
                        adapter.notifyDataSetChanged()

                    }
                    .addOnFailureListener {
                        Log.e("fail", "fail")
                    }
            } catch (e: Exception) {
            }

    }

    fun start() {
        flag = false
        started = true



        //sub thread

        thread(start = true) {
            var flag2 = true
            while (true) {

                Thread.sleep(1000)
                if(flag2 == false)  break
                if (!started) break
                total = total - 1
                activity!!.runOnUiThread {
                    if (binding!!.hour.text == "00" && binding!!.minute.text == "00" && binding!!.sec.text == "00") {
                        flag2 = false
                        var current = binding!!.viewpager.currentItem
                        binding!!.viewpager.setCurrentItem(current+1, false)
                        Log.e("scroll", "scroll")
                        Log.e("current",TestInfo_ArrayList.size.toString())
                        started=false
                        flag = true

                    }
                    val current = binding!!.viewpager.currentItem
                    if(current == TestInfo_ArrayList.size-1 && binding!!.hour.text == "00" && binding!!.minute.text == "00" && binding!!.sec.text == "00")
                    {
                        Toast.makeText(context,"pz",Toast.LENGTH_SHORT).show()
                    }
                    binding!!.hour.text = String.format("%02d", (total / 3600) % 60)
                    binding!!.minute.text = String.format("%02d", (total / 60) % 60)
                    binding!!.sec.text = String.format("%02d", total % 60)
                }

            }
        }

    }


    fun pause() {
        started = false
        flag = true
    }

    fun stop() {
        started = false
        total = 0
        binding!!.hour.text = "00"
        binding!!.minute.text = "00"
        binding!!.sec.text = "00"
        flag = true
    }

    fun init() {


        total = binding!!.hour.text.toString().toInt() * 3600 + binding!!.minute.text.toString().toInt() * 60 + binding!!.sec.text.toString().toInt()
        adapter.itemClickListener = object : TestRecyclerViewAdapter.OnItemClickListener {
            override fun OnItemClick(
                    holder: TestRecyclerViewAdapter.ViewHolder,
                    view: View,
                    position: Int,
                    hour: String,
                    minute: String,
                    sec: String
            ) {
                binding!!.hour.text = hour
                binding!!.minute.text = minute
                binding!!.sec.text = sec
                total = hour.toInt() * 3600 + minute.toInt() * 60 + sec.toInt()
                //holder.binding.nextButton.visibility = View.VISIBLE
            }

        }
//        adapter.itemClickListener2 = object :TestRecyclerViewAdapter.OnItemClickListener {
//            override fun OnItemClick(
//                    holder: TestRecyclerViewAdapter.ViewHolder,
//                    view: View,
//                    position: Int,
//                    hour: String,
//                    minute: String,
//                    sec: String
//            ) {
//                var current = binding!!.viewpager.currentItem
//                binding!!.viewpager.setCurrentItem(current+1, false)
//
//
//            }
//        }

        binding!!.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {


            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                    var current = binding!!.viewpager.currentItem

                    binding!!.hour.text = adapter.items[current].hour
                    binding!!.minute.text = adapter.items[current].minute
                    binding!!.sec.text = adapter.items[current].sec

                    total = binding!!.hour.text.toString().toInt() * 3600 + binding!!.minute.text.toString().toInt() * 60 + binding!!.sec.text.toString().toInt()
                if(position!=0) {
                    binding!!.startBtn.performClick()

                }

                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }


        })

        binding!!.startBtn.setOnClickListener {
            if (flag == true) {
                start()
            }
        }

        binding!!.pasueBtn.setOnClickListener {

            pause()

        }
        binding!!.stopBtn.setOnClickListener {
            stop()
        }
    }



    }




//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        if (binding!!.minute.text == "00" && binding!!.second.text == "00" && binding!!.milli.text == "00") {
//            Log.e("scroll","scroll")
//            layoutManager.scrollToPosition(6)
//
//        }
//        super.onActivityCreated(savedInstanceState)
//    }


