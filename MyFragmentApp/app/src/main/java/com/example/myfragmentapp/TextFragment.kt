package com.example.myfragmentapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.myfragmentapp.databinding.FragmentTextBinding

class TextFragment : Fragment() {
    var binding: FragmentTextBinding?=null
    val myViewModel:MyViewModel by activityViewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTextBinding.inflate(layoutInflater,container,false)
        return binding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val intent = requireActivity().intent
        val imgnum = intent.getIntExtra("imgnum",-1)
        if(imgnum!=-1) { // 클릭으로 넘어온 경우
            binding!!.textView.text = imgnum.toString()
        }
        else { // 회전한 경우
            myViewModel.selectednum.observe(viewLifecycleOwner, Observer {
                binding!!.textView.text = it.toString()
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding=null
    }

}