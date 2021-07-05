package com.example.myengvoc.view.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.myengvoc.adapter.QuizAdapter
import com.example.myengvoc.data.MyData
import com.example.myengvoc.databinding.FragmentQuizBinding
import com.example.myengvoc.view.viewmodel.MyViewModel
import com.example.myengvoc.view.viewmodel.MyViewModel2
import java.io.PrintStream


class QuizFragment : Fragment() {
    lateinit var adapter: QuizAdapter
    val viewModel: MyViewModel by activityViewModels()
    val viewModel2: MyViewModel2 by activityViewModels()
    var binding: FragmentQuizBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuizBinding.inflate(layoutInflater, container, false)
        init()
        return binding!!.root
    }

    private fun init() {
        adapter = QuizAdapter(viewModel.liveItems.value)
        binding!!.viewpager3.adapter = adapter
        adapter.itemClickListener = object : QuizAdapter.OnItemClickListener {
            override fun OnItemClick(holder: QuizAdapter.ViewHolder, view: View) {
                if (holder.answerEdit.text.toString().equals(holder.answerText.text.toString())) {
                    scrollView()
                    Toast.makeText(context, "정답입니다!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "틀렸습니다!", Toast.LENGTH_SHORT).show()
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("오답노트에 추가하시겠습니까?")
                    builder.setPositiveButton("네") { _, _ ->
                        viewModel2.add(MyData(holder.answerText.text.toString(), holder.TextView.text.toString()))
                        writeFile(holder.answerText.text.toString(),holder.TextView.text.toString())
                        scrollView()
                    }
                    builder.setNegativeButton("아니요") {_,_ ->
                        scrollView()
                    }.show()

                }


            }

        }


//        binding!!.viewpager3.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                Toast.makeText(context,position.toString(),Toast.LENGTH_SHORT).show()
//                super.onPageSelected(position)
//            }
//        })
        binding!!.viewpager3.orientation = ViewPager2.ORIENTATION_VERTICAL


    }
    private fun writeFile(word1: String, meaning1: String) {
        val output = PrintStream(activity?.openFileOutput("out2.txt", Context.MODE_APPEND))
        output.println(word1)
        output.println(meaning1)
        output.close()
    }

    fun scrollView() {
        var current = binding!!.viewpager3.currentItem
        Log.e("datasize",adapter.itemCount.toString())
        Log.e("currentpage",current.toString())

        if(current==adapter.itemCount-1) {
            binding!!.viewpager3.setCurrentItem(0, false)
        }
        else {
            binding!!.viewpager3.setCurrentItem(current+1,false)
        }
    }

}