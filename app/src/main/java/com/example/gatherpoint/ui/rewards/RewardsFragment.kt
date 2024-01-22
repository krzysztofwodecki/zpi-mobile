package com.example.gatherpoint.ui.rewards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.gatherpoint.R
import com.example.gatherpoint.adapters.RewardsPagerAdapter
import com.example.gatherpoint.databinding.FragmentRewardsBinding
import com.example.gatherpoint.viewmodel.RewardsViewModel
import com.google.android.material.tabs.TabLayoutMediator

class RewardsFragment : Fragment() {

    private val viewModel: RewardsViewModel by viewModels()

    private var _binding: FragmentRewardsBinding? = null
    private val binding get() = _binding!!
    private lateinit var rewardsPagerAdapter: RewardsPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRewardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rewardsPagerAdapter = RewardsPagerAdapter(this)
        binding.rewardsViewPager.adapter = rewardsPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.rewardsViewPager) { tab, position ->
            tab.text = resources.getString(
                when (position) {
                    0 -> R.string.rewards_tab_rewards_to_redeem_title
                    1 -> R.string.rewards_tab_redeemed_rewards_title
                    else -> R.string.rewards_tab_rewards_to_redeem_title
                }
            )
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}