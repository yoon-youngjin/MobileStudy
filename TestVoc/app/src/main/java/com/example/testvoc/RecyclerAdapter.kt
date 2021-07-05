import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.testvoc.R
import com.example.testvoc.User

class RecyclerAdapter(var data: LiveData<ArrayList<User>>) :
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder constructor(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list, parent, false)
    ) {
        var tv1 = itemView.findViewById<TextView>(R.id.firstTV)
        var tv2 = itemView.findViewById<TextView>(R.id.secondTV)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(parent)
    }

    override fun getItemCount(): Int {
        Log.e("datasize", "" + data.value!!.size)
        return data.value!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        data.value!!.get(position).let { item ->
            with(holder) {
                tv1.text = item.name
                tv2.text = item.nickname
                Log.e("TextSet", tv1.text.toString() + tv2.text.toString())
            }
        }
    }
}