package com.example.engvoc.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.engvoc.R
import com.example.engvoc.adapter.MyItemRecyclerViewAdapter2
import com.example.engvoc.data.MyData
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase


class WrongAnswerFragment : Fragment() {
    lateinit var adapter: MyItemRecyclerViewAdapter2

    private var columnCount = 1
    val rdb2 = FirebaseDatabase.getInstance().getReference("datas/items2")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        val view = inflater.inflate(R.layout.fragment_wrong_answer_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
                view.layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                view.adapter = adapter
            }
        return view
    }

    private fun init() {
        val query = rdb2.orderByKey()
        val option = FirebaseRecyclerOptions.Builder<MyData>()
            .setQuery(query, MyData::class.java)
            .build()

        adapter = MyItemRecyclerViewAdapter2(option)


        adapter.itemClickListener = object : MyItemRecyclerViewAdapter2.OnItemClickListener {

            override fun OnItemClick(holder: MyItemRecyclerViewAdapter2.ViewHolder, view: View) {

                val builder = AlertDialog.Builder(context)
                builder.setMessage("오답노트에서 지우시겠습니까?")
                builder.setPositiveButton("네") { _, _ ->
                    rdb2.child(holder.contentView.text.toString()).removeValue()

                }
                builder.setNegativeButton("아니요") { _, _ ->
                }.show()
            }
        }
        adapter.startListening()
    }


}