package com.example.gatherpoint.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gatherpoint.ui.rewards.RedeemedRewardsFragment
import com.example.gatherpoint.ui.rewards.RewardsToRedeemFragment

class RewardsPagerAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> RewardsToRedeemFragment.newInstance()
            1 -> RedeemedRewardsFragment.newInstance()
            else -> RewardsToRedeemFragment.newInstance()
        }
    }
}