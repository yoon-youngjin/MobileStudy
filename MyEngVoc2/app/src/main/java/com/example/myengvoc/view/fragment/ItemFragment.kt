package com.example.myengvoc.view.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myengvoc.R
import com.example.myengvoc.adapter.MyItemRecyclerViewAdapter
import com.example.myengvoc.data.MyData
import com.example.myengvoc.view.viewmodel.MyViewModel
import com.example.myengvoc.view.viewmodel.MyViewModel2
import java.io.PrintStream
import java.util.*


class ItemFragment : Fragment() {


    lateinit var adapter: MyItemRecyclerViewAdapter
    private var columnCount = 1
    val viewModel: MyViewModel by activityViewModels()
    val viewModel2: MyViewModel2 by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    private fun initData() {
        try {
            val scan2 = Scanner(activity?.openFileInput("out1.txt"))
            readFileScan(scan2)
        } catch (e: Exception) {
        }
        val scan = Scanner(resources.openRawResource(R.raw.words))
        readFileScan(scan)
    }


    private fun readFileScan(scan: Scanner) {
        while (scan.hasNextLine()) {
            val word = scan.nextLine()
            val meaning = scan.nextLine()
            viewModel?.add(MyData(word, meaning))
        }

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)
        adapter = MyItemRecyclerViewAdapter(viewModel.liveItems.value)
        adapter.itemClickListener = object : MyItemRecyclerViewAdapter.OnItemClickListener {
            override fun OnItemClick(holder: MyItemRecyclerViewAdapter.ViewHolder, view: View) {
                if (holder.contentLayout.visibility == View.GONE)
                    holder.contentLayout.visibility = View.VISIBLE
                else
                    holder.contentLayout.visibility = View.GONE
                holder.btn.setOnClickListener {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("오답노트에 추가하시겠습니까?")
                    builder.setPositiveButton("네") { _, _ ->
                        viewModel2.add(MyData(holder.idView.text.toString(), holder.contentView.text.toString()))
                        writeFile(holder.idView.text.toString(),holder.contentView.text.toString())

                    }.show()
                }
            }

        }


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

    private fun writeFile(word1: String, meaning1: String) {
        val output = PrintStream(activity?.openFileOutput("out2.txt", Context.MODE_APPEND))
        output.println(word1)
        output.println(meaning1)
        output.close()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.liveItems.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (view is RecyclerView) {

                view.adapter!!.notifyDataSetChanged()
            }
        })

    }
}
