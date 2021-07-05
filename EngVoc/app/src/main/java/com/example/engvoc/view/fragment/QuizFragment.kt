package com.example.engvoc.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.engvoc.adapter.QuizAdapter
import com.example.engvoc.data.MyData
import com.example.engvoc.databinding.FragmentQuizBinding
import com.example.engvoc.view.viewmodel.MyViewModel
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase


class QuizFragment : Fragment() {
    val viewModel: MyViewModel by activityViewModels()
    lateinit var adapter: QuizAdapter
    var binding: FragmentQuizBinding?=null
    val rdb = FirebaseDatabase.getInstance().getReference("datas/items")
    val rdb2 = FirebaseDatabase.getInstance().getReference("datas/items2")

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
        val query = rdb.orderByKey()
        val option = FirebaseRecyclerOptions.Builder<MyData>()
            .setQuery(query, MyData::class.java)
            .build()

        adapter = QuizAdapter(option)
        binding!!.viewpager3.adapter = adapter
        adapter.itemClickListener = object : QuizAdapter.OnItemClickListener {
            override fun OnItemClick(holder: QuizAdapter.ViewHolder, view: View) {
                if (holder.answerEdit.text.toString().equals(holder.answerText.text.toString())) {
                    scrollView()
                    Toast.makeText(context, "정답입니다!", Toast.LENGTH_SHORT).show()
                    holder.answerEdit.text.clear()
                } else {
                    Toast.makeText(context, "틀렸습니다!", Toast.LENGTH_SHORT).show()
                    holder.answerEdit.text.clear()
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("오답노트에 추가하시겠습니까?")
                    builder.setPositiveButton("네") { _, _ ->
                        viewModel.liveData.value =viewModel.liveData.value as Int +1
                        val item = MyData(holder.TextView.text.toString(), holder.answerText.text.toString())
                        rdb2.child(holder.TextView.text.toString()).setValue(item)
                        scrollView()
                    }
                    builder.setNegativeButton("아니요") {_,_ ->
                        scrollView()
                    }.show()

                }
            }
        }
        binding!!.viewpager3.orientation = ViewPager2.ORIENTATION_VERTICAL
        adapter.startListening()
    }


    fun scrollView() { // 자동 화면 전환
        var current = binding!!.viewpager3.currentItem
        if(current==adapter.itemCount-1) {
            binding!!.viewpager3.setCurrentItem(0, false)
        }
        else {
            binding!!.viewpager3.setCurrentItem(current+1,false)
        }
    }

}