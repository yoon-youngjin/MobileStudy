package com.example.engvoc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.engvoc.R
import com.example.engvoc.data.MyData
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class QuizAdapter (options: FirebaseRecyclerOptions<MyData>) :
     FirebaseRecyclerAdapter<MyData, QuizAdapter.ViewHolder>(options) {
    interface OnItemClickListener {
        fun OnItemClick(holder: ViewHolder, view: View)
    }
    var itemClickListener: OnItemClickListener? = null
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        val TextView: TextView = view.findViewById(R.id.textView4)
        val answerText : TextView = view.findViewById(R.id.answerText)
        val answerBtn : ImageButton = view.findViewById(R.id.answerBtn)
        val answerEdit: EditText = view.findViewById(R.id.answerEditText)


        init {

            answerBtn.setOnClickListener {
                itemClickListener!!.OnItemClick(this,it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.quizitemview, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: MyData) {
        holder.TextView.text = model.word
        holder.answerText.text = model.mean
    }

}