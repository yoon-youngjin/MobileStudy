package com.example.myengvoc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myengvoc.R
import com.example.myengvoc.data.MyData


class MyItemRecyclerViewAdapter2(
    private val data: ArrayList<MyData>?
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter2.ViewHolder>() {
    interface OnItemClickListener {
        fun OnItemClick(holder: MyItemRecyclerViewAdapter2.ViewHolder, view: View,data:MyData,position: Int)
    }
    var itemClickListener: OnItemClickListener? = null


    fun removeItem(pos: Int) {
        data!!.removeAt(pos)
        notifyItemRemoved(pos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_wrong_answer, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data!!.get(position)
        holder.idView.text = item?.mean
        holder.contentView.text = item?.word
    }



    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.content)
        val contentView: TextView = view.findViewById(R.id.item_number)

        init {
            view.setOnClickListener {
                itemClickListener!!.OnItemClick(this,it,data!!.get(bindingAdapterPosition),bindingAdapterPosition)
            }
        }
    }
//예외 처리
    override fun getItemCount(): Int {
        try {
            return data!!.size
        }
        catch (e: Exception) {
            return 0
        }
    }
}