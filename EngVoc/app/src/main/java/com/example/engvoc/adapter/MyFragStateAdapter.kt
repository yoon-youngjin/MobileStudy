package com.example.engvoc.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.engvoc.view.fragment.AddFragment
import com.example.engvoc.view.fragment.ItemFragment
import com.example.engvoc.view.fragment.QuizFragment
import com.example.engvoc.view.fragment.WrongAnswerFragment



class MyFragStateAdapter(fragmentActivity:FragmentActivity):FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> ItemFragment()
            1 -> AddFragment()
            2 -> QuizFragment()
            3 -> WrongAnswerFragment()
            else -> ItemFragment()
        }
    }
}