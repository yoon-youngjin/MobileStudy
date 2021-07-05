package com.example.myengvoc.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.myengvoc.data.MyData
import com.example.myengvoc.databinding.FragmentAddBinding
import com.example.myengvoc.view.viewmodel.MyViewModel
import java.io.PrintStream


class AddFragment : Fragment() {
    val itemFragment = ItemFragment()
    var binding:FragmentAddBinding?=null
    val viewModel: MyViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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
                val word1 = word.text.toString()
                val meaning1 = meaning.text.toString()
                viewModel.add(MyData(word1,meaning1))
                writeFile(word1,meaning1)
                word.text.clear()
                meaning.text.clear()
            }

        }
    }

    private fun writeFile(word1: String, meaning1: String) {
        val output = PrintStream(activity?.openFileOutput("out1.txt", Context.MODE_APPEND))
        output.println(word1)
        output.println(meaning1)
        output.close()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding =null
    }
}