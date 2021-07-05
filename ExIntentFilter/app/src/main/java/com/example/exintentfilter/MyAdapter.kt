package com.example.exintentfilter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exintentfilter.databinding.RowBinding

class MyAdapter(val items:ArrayList<MyData>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun OnItemClick(holder:ViewHolder, view: View,data: MyData,position: Int)
    }
    var itemClickListener : OnItemClickListener?=null

    inner class ViewHolder(val binding: RowBinding) :RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                itemClickListener?.OnItemClick(this,it,items[adapterPosition],adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            applabel.text = items[position].applabel
            appclass.text = items[position].appclass
            imageView.setImageDrawable(items[position].appicon)
        }
    }


}