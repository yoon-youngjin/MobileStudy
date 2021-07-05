package com.example.engvoc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.engvoc.R
import com.example.engvoc.data.MyData
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions


class MyItemRecyclerViewAdapter2(options: FirebaseRecyclerOptions<MyData>) :
    FirebaseRecyclerAdapter<MyData, MyItemRecyclerViewAdapter2.ViewHolder>(options)  {
    interface OnItemClickListener {
        fun OnItemClick(holder: MyItemRecyclerViewAdapter2.ViewHolder, view: View)
    }
    var itemClickListener: OnItemClickListener? = null




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_wrong_answer, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.meaning2)
        val contentView: TextView = view.findViewById(R.id.word2)

        init {
            contentView.setOnClickListener {
                itemClickListener!!.OnItemClick(this,it)
            }

        }
    }

    override fun onBindViewHolder(holder: MyItemRecyclerViewAdapter2.ViewHolder, position: Int, model: MyData) {
        holder.contentView.text = model.word
        holder.idView.text = model.mean
    }

}