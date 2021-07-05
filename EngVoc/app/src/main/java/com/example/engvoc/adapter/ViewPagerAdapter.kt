package com.example.engvoc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.engvoc.R
import com.example.engvoc.data.MyData
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class ViewPagerAdapter(options: FirebaseRecyclerOptions<MyData>)
    : FirebaseRecyclerAdapter<MyData, ViewPagerAdapter.ViewHolder>(options) {
    interface OnItemClickListener {
        fun OnItemClick(holder: ViewHolder, view: View)
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
                itemClickListener!!.OnItemClick(this,it)
            }
            speakBtn.setOnClickListener {
                itemClickListener2!!.OnItemClick(this,it)
            }
            addBtn.setOnClickListener {
                itemClickListener3!!.OnItemClick(this,it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemview, parent, false)
        return ViewHolder(view)
    }



    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.ViewHolder, position: Int, model: MyData) {

        holder.secondText.text = model.word
        holder.answerText.text = model.mean
    }
}