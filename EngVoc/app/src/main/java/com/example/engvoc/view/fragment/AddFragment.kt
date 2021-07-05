package com.example.engvoc.view.fragment


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.engvoc.data.MyData
import com.example.engvoc.databinding.FragmentAddBinding
import com.example.engvoc.view.viewmodel.MyViewModel
import com.google.firebase.database.FirebaseDatabase


class AddFragment : Fragment() {

    val viewModel: MyViewModel by activityViewModels()
    var binding: FragmentAddBinding?=null
    val rdb = FirebaseDatabase.getInstance().getReference("datas/items")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(layoutInflater,container,false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.apply {
            addBtn.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("단어장에 추가하시겠습니까?")
                builder.setPositiveButton("네") { _, _ ->
                    val word1 = word.text.toString()
                    val meaning1 = meaning.text.toString()
                    val item = MyData(word1,meaning1)
                    rdb.child(viewModel.liveData.value.toString()).setValue(item)
                    viewModel.liveData.value = viewModel.liveData.value as Int + 1
                    word.text.clear()
                    meaning.text.clear()
                }
                builder.setNegativeButton("아니요") {_,_ ->
                    word.text.clear()
                    meaning.text.clear()
                }.show()

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding =null
    }
}