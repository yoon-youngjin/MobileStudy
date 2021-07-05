package com.example.myengvoc.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myengvoc.R
import com.example.myengvoc.data.MyData

class ViewPagerAdapter(val data: ArrayList<MyData>?) :
RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun OnItemClick(holder: ViewHolder, view: View,data:MyData,position: Int)
    }
    var itemClickListener: OnItemClickListener? = null
    var itemClickListener2: OnItemClickListener? = null
    var itemClickListener3: OnItemClickListener? = null
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val secondText:TextView = view.findViewById(R.id.thirdText)
        val answerText:TextView = view.findViewById(R.id.answer2)
        val answerBtn : ImageButton = view.findViewById(R.id.answerBtn3)
        val speakBtn : ImageButton = view.findViewById(R.id.speakBtn2)
        val addBtn :ImageButton = view.findViewById(R.id.addBtn3)
        init {
            answerText.visibility=View.GONE
            answerBtn.setOnClickListener {
                itemClickListener!!.OnItemClick(this,it,data!!.get(bindingAdapterPosition),bindingAdapterPosition)
            }
            speakBtn.setOnClickListener {
                itemClickListener2!!.OnItemClick(this,it,data!!.get(bindingAdapterPosition),bindingAdapterPosition)
            }
            addBtn.setOnClickListener {
                itemClickListener3!!.OnItemClick(this,it,data!!.get(bindingAdapterPosition),bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data!!.get(position)
        holder.secondText.text = item?.word
        holder.answerText.text = item?.mean

    }

    override fun getItemCount(): Int {
        try {
            return data!!.size
        }
        catch (e: Exception) {
            Log.e("note","단어장 Empty")
            return 0
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}