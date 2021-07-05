package com.example.engvoc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.engvoc.data.MyData
import com.example.engvoc.databinding.FragmentItemBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions


class MyItemRecyclerViewAdapter (options: FirebaseRecyclerOptions<MyData>)
    : FirebaseRecyclerAdapter<MyData, MyItemRecyclerViewAdapter.ViewHolder>(options) {

    interface OnItemClickListener {
        fun OnItemClick(holder: ViewHolder, view: View)
    }

    var itemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val binding: FragmentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.linearLayout.visibility = View.GONE
            binding.root.setOnClickListener {
                itemClickListener!!.OnItemClick(this, it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = FragmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: MyData) {
        holder.binding.apply {

            word2.text = model.word
            meaning2.text = model.mean

        }
    }
}