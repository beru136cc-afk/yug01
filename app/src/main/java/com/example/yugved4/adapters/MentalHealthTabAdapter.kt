package com.example.yugved4.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.yugved4.fragments.EmergencyTabFragment
import com.example.yugved4.fragments.ResourcesTabFragment

/**
 * ViewPager2 Adapter for Mental Health Tabs
 */
class MentalHealthTabAdapter(fragmentActivity: FragmentActivity) : 
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ResourcesTabFragment()
            1 -> EmergencyTabFragment()
            else -> ResourcesTabFragment()
        }
    }
}
