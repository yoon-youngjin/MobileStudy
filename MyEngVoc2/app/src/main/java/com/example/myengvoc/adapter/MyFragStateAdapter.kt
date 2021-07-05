package com.example.myengvoc.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myengvoc.view.fragment.QuizFragment
import com.example.myengvoc.view.fragment.WrongAnswerFragment
import com.example.myengvoc.view.fragment.AddFragment
import com.example.myengvoc.view.fragment.ItemFragment

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