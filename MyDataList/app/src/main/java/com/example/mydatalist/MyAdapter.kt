package com.example.mydatalist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(val items:ArrayList<MyData>):RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    // 이너클래스 => 아웃터클래스 자유롭게 접근 가능
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //itemView객체 => linearLayout의 textView
        val textView:TextView = itemView.findViewById(R.id.textView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // viewHolder생성 -> layout객체 인스턴스화
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = items[position].name
        holder.textView.textSize = items[position].pt.toFloat()

    }

    override fun getItemCount(): Int {
        return items.size
    }
    // MyAdapter클래스는 recyclerView의 Adapter상속받아서 생성 -> Adapter는 ViewHolder지정되어야 하고 ViewHolder는 MyAapter클래스의 이너클래스로 생성 ->
    // viewHolder클래스는 recyclerView의 ViewHolder상속받아서 생성 ,View정보 포함 view정보가 담고있는 위젯들을 멤버로 만들수 있음 -> onCreateViewHolder : 뷰를 생성해줄때 레이아웃 inflater 객체를 통해
    // 뷰홀더 객체를 통해 멤버들을 구성(캐쉬)가능 -> BindViewHolder : 뷰홀더가 전달이되면 캐쉬되어있던 텍스트,사이즈정보(바꿀 위젯의정보)를 원하는 데이터로 변경가능 -> getitemcount : 전체 데이터의 개수정보 반환

}