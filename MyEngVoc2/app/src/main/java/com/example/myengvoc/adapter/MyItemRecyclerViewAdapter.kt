package com.example.myengvoc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myengvoc.R
import com.example.myengvoc.data.MyData


class MyItemRecyclerViewAdapter(val data: ArrayList<MyData>?) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick(holder: ViewHolder, view: View)
    }
    var itemClickListener: OnItemClickListener? = null



    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_number)
        val contentLayout: LinearLayout = view.findViewById(R.id.linearLayout)
        val contentView: TextView = view.findViewById(R.id.content)
        val btn: ImageButton = view.findViewById(R.id.addBtn2)
        init {
            contentLayout.visibility=View.GONE
            view.setOnClickListener {
                itemClickListener!!.OnItemClick(this,it)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data!!.get(position)
        holder.idView.text = item?.word
        holder.contentView.text = item?.mean

    }

    override fun getItemCount(): Int = data!!.size
    override fun getItemViewType(position: Int): Int {
        return position
    }


}