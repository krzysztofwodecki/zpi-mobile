package com.example.gatherpoint.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gatherpoint.ui.events.MyEventsFragment
import com.example.gatherpoint.ui.events.NearEventsFragment
import com.example.gatherpoint.ui.events.SavedEventsFragment

class EventsPagerAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SavedEventsFragment.newInstance()
            1 -> NearEventsFragment.newInstance()
            2 -> MyEventsFragment.newInstance()
            else -> NearEventsFragment.newInstance()
        }
    }
}