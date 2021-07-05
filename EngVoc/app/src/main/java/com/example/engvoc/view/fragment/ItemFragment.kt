package com.example.engvoc.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.engvoc.R
import com.example.engvoc.adapter.MyItemRecyclerViewAdapter
import com.example.engvoc.data.MyData
import com.example.engvoc.databinding.FragmentItemBinding
import com.example.engvoc.view.viewmodel.MyViewModel
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ItemFragment : Fragment() {
    val viewModel: MyViewModel by activityViewModels()
    lateinit var binding: FragmentItemBinding
    lateinit var adapter: MyItemRecyclerViewAdapter
    lateinit var rdb: DatabaseReference
    lateinit var rdb2:DatabaseReference

    private var columnCount = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rdb = FirebaseDatabase.getInstance().getReference("datas/items")
        rdb2 = FirebaseDatabase.getInstance().getReference("datas/items2")

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)
        val query = rdb.orderByKey()
        val option = FirebaseRecyclerOptions.Builder<MyData>()
            .setQuery(query, MyData::class.java)
            .build()

        adapter = MyItemRecyclerViewAdapter(option)


        adapter.itemClickListener = object : MyItemRecyclerViewAdapter.OnItemClickListener {
                override fun OnItemClick(holder: MyItemRecyclerViewAdapter.ViewHolder, view:View) {

                    if (holder.binding.linearLayout.visibility == View.GONE)
                        holder.binding.linearLayout.visibility = View.VISIBLE
                    else
                        holder.binding.linearLayout.visibility = View.GONE
                    holder.binding.addBtn2.setOnClickListener {
                        val builder = AlertDialog.Builder(context)
                        builder.setMessage("오답노트에 추가하시겠습니까?")
                        builder.setPositiveButton("네") { _, _ ->
                            viewModel.liveData.value =viewModel.liveData.value as Int +1
                            val item = MyData(holder.binding.word2.text.toString(),holder.binding.meaning2.text.toString())

                            rdb2.child(holder.binding.word2.text.toString()).setValue(item)

                        }
                        builder.setNegativeButton("아니요") {_,_ ->}.show()
                    }
                }

        }

            // Set the adapter
            if (view is RecyclerView)
            {
                view.layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                view.adapter = adapter

            }
            adapter.startListening()

            return view
        }


}



