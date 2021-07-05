package com.example.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(val items:ArrayList<MyData>):RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    public interface OnItemClickListener {
        fun OnItemClick(viewHolder: ViewHolder,view:View,data:MyData,position: Int)
    }
    var itemClickListener:OnItemClickListener?=null
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val textView2 : TextView = itemView.findViewById(R.id.textView2)
        init {
            textView2.visibility =View.GONE
            itemView.setOnClickListener {
                itemClickListener?.OnItemClick(this,itemView,items[adapterPosition],adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = items[position].word
        holder.textView2.text = items[position].meaning

    }

    override fun getItemCount(): Int {
    return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}