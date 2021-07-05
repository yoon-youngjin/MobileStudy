package com.example.midproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.midproject.databinding.RowBinding

class MyAdapter(val items:ArrayList<MyData>): RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    var itemClickListener: OnItemClickListener? = null

    public interface OnItemClickListener {
        fun OnItemClick(holder: ViewHolder, view: View, data: MyData, position: Int)
    }

    inner class ViewHolder(var binding: RowBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                itemClickListener?.OnItemClick(this, it, items[adapterPosition], adapterPosition)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            textView4.text = items[position].a
            textView5.text = items[position].cost
            textView6.text = items[position].model

        }


    }


    override fun getItemCount(): Int {
        return items.size
    }
}