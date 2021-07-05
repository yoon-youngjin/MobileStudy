package com.example.myengvoc.view.fragment

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
import com.example.myengvoc.R
import com.example.myengvoc.adapter.MyItemRecyclerViewAdapter2
import com.example.myengvoc.data.MyData
import com.example.myengvoc.view.viewmodel.MyViewModel2
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.PrintWriter
import java.util.*


class WrongAnswerFragment : Fragment() {
    lateinit var adapter: MyItemRecyclerViewAdapter2
    val viewModel2 : MyViewModel2 by activityViewModels()
    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    private fun initData() {
        try {
            val scan2 = Scanner(activity?.openFileInput("out2.txt"))
            readFileScan(scan2)
        } catch (e: Exception) {
        }

    }
    private fun readFileScan(scan: Scanner) {
        while (scan.hasNextLine()) {
            val word = scan.nextLine()
            val meaning = scan.nextLine()
            viewModel2?.add(MyData(word, meaning))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wrong_answer_list, container, false)
        adapter = MyItemRecyclerViewAdapter2(viewModel2.liveItems.value)
        adapter.itemClickListener = object : MyItemRecyclerViewAdapter2.OnItemClickListener {
            override fun OnItemClick(holder: MyItemRecyclerViewAdapter2.ViewHolder, view: View, data: MyData, position: Int) {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("오답노트에서 지우시겠습니까?")
                builder.setPositiveButton("네") { _, _ ->
                    adapter.removeItem(position)
                    val br = BufferedReader(InputStreamReader(activity?.openFileInput("out2.txt")))
                    var vr:String = ""
                    var line:String =""
                    try {
                        while (true) {
                            line = br.readLine()
                            if (line.equals(viewModel2.liveItems.value!!.get(position).toString())) {
                                break
                            }
                            vr += (line + "\r\n")

                        }
                        var deldata1 = br.readLine()
                        while(true) {
                            line = br.readLine()
                            vr += (line + "\r\n")
                        }
                    }
                    catch(e:Exception) {}
                    br.close()
                    try {
                        val file = File("/data/data/com.example.myengvoc/files/out2.txt")
                        val printWriter = PrintWriter(file)
                        printWriter.print(vr)
                        printWriter.close()
                    }
                    catch (e:Exception) {}
                }
                builder.setNegativeButton("아니요") { _, _ ->
                }.show()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel2.liveItems.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(view is RecyclerView) {
                view.adapter!!.notifyDataSetChanged()
            }
        })

    }
}