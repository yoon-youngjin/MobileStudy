package com.example.exvocintentfilter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exvocintentfilter.databinding.RowBinding

class MyAdapter(val items:ArrayList<MyData>):RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    val MainTAG = "MainActivity"
    // 어댑터는 뷰가 데이터를 가지고있는 상태로 제공 , 레이아웃정보 , 데이터셋 결합
    interface OnItemClickListener {
        fun OnItemClick(holder: ViewHolder, view: View, data: MyData, position: Int)
    }
    var itemClickListener: OnItemClickListener? = null
    fun moveItem(oldPos: Int, newPos: Int) {
        val item = items[oldPos]
        items.removeAt(oldPos)
        items.add(newPos, item)
        notifyItemMoved(oldPos, newPos)
    }

    fun removeItem(pos: Int) {
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }

    // 이너클래스 => 아웃터클래스 자유롭게 접근 가능
    inner class ViewHolder(val binding: RowBinding) : RecyclerView.ViewHolder(binding.root) { // 뷰 홀더는 뷰 객체를 참조한다

        //뷰 바인딩 방식으로 id접근



        init {

            binding.root.setOnClickListener {
                itemClickListener?.OnItemClick(this, it, items[adapterPosition], adapterPosition)
                Log.i(MainTAG,"Click" )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // viewBinding에 의해 접근 -> RowBinding 클래스 자동생성
        val view = RowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // 뷰 홀더에 값을 설정

        holder.binding.apply {
            appclass.text = items[position].appclass
            applabel.text = items[position].applabel
            imageView.setImageDrawable(items[position].appicon)
        }
    }
        // 뷰 홀더는 온크리트뷰 홀더에서 하나의 뷰를 생성하고, 뷰정보를 뷰 홀더에 리턴 뷰 홀더는 실체화된 객체를 가리킬수 있다.
// 뷰 홀더, 포지션정보 -> 뷰 홀더가 가지고있는 참조변수 값 변경가능 -> items는 어뎁터 생성시 생성된것

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return position

    }
    // MyAdapter클래스는 recyclerView의 Adapter상속받아서 생성 -> Adapter는 ViewHolder지정되어야 하고 ViewHolder는 MyAapter클래스의 이너클래스로 생성 ->
    // viewHolder클래스는 recyclerView의 ViewHolder상속받아서 생성 ,View정보 포함 view정보가 담고있는 위젯들을 멤버로 만들수 있음 -> onCreateViewHolder : 뷰를 생성해줄때 레이아웃 inflater 객체를 통해
    // 뷰홀더 객체를 통해 멤버들을 구성(캐쉬)가능 -> BindViewHolder : 뷰홀더가 전달이되면 캐쉬되어있던 텍스트,사이즈정보(바꿀 위젯의정보)를 원하는 데이터로 변경가능 -> getitemcount : 전체 데이터의 개수정보 반환
}
