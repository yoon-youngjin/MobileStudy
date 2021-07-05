package com.example.myengvoc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myengvoc.R
import com.example.myengvoc.data.MyData

class ViewPagerAdapter2(val data: ArrayList<MyData>?) :
RecyclerView.Adapter<ViewPagerAdapter2.ViewHolder>() {
    interface OnItemClickListener {
        fun OnItemClick(holder: ViewHolder, view: View,data:MyData,position: Int)
    }
    var itemClickListener: OnItemClickListener? = null
    var itemClickListener2: OnItemClickListener? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val thirdText:TextView = view.findViewById(R.id.thirdText)
        val answerText:TextView = view.findViewById(R.id.answer2)
        val answerBtn : ImageButton = view.findViewById(R.id.answerBtn3)
        val speakBtn : ImageButton = view.findViewById(R.id.speakBtn2)

        init {
            answerText.visibility=View.GONE
            answerBtn.setOnClickListener {
                itemClickListener!!.OnItemClick(this,it,data!!.get(bindingAdapterPosition),bindingAdapterPosition)
            }
            speakBtn.setOnClickListener {
                itemClickListener2!!.OnItemClick(this,it,data!!.get(bindingAdapterPosition),bindingAdapterPosition)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemview2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data!!.get(position)
        holder.thirdText.text = item?.word
        holder.answerText.text = item?.mean

    }

    override fun getItemCount(): Int {
        try {
            return data!!.size
        }
        catch (e: Exception) {
            return 0
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}