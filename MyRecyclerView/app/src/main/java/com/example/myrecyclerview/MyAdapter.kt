package com.example.myrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecyclerview.databinding.RowBinding

class MyAdapter(val items:ArrayList<MyData>): RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    var itemClickListener:OnItemClickListener?=null
    public interface OnItemClickListener {
        fun OnItemClick(holder: ViewHolder, view: View,data: MyData,position: Int)
    }

    inner class ViewHolder(var binding: RowBinding) : RecyclerView.ViewHolder(binding.root) {
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

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.binding.apply {
                textView.text = items[position].applabel
                textView2.text = items[position].appclass
                imageView.setImageDrawable(items[position].appicon)
            }


        }


    override fun getItemCount(): Int {
        return items.size
    }

    fun moveItem(oldPos:Int,newPos:Int) {
        val item = items[oldPos]
        items.removeAt(oldPos)
        items.add(newPos,item)
        notifyItemMoved(oldPos,newPos)

    }
    fun removeItem(pos:Int) {
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }
}

