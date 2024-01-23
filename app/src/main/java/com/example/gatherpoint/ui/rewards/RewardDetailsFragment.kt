package com.example.gatherpoint.ui.rewards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.gatherpoint.R
import com.example.gatherpoint.databinding.FragmentEventDetailsBinding
import com.example.gatherpoint.databinding.FragmentRewardDetailsBinding
import com.example.gatherpoint.network.Model
import com.example.gatherpoint.network.Resource
import com.example.gatherpoint.viewmodel.EventsViewModel
import com.example.gatherpoint.viewmodel.RewardsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlin.properties.Delegates

class RewardDetailsFragment : Fragment() {

    private val viewModel: RewardsViewModel by navGraphViewModels(R.id.nav_graph_dashboard)
    val args: RewardDetailsFragmentArgs by navArgs()

    private var _binding: FragmentRewardDetailsBinding? = null
    private val binding get() = _binding!!
    private var isRedeemed by Delegates.notNull<Boolean>()
    private var rewardId by Delegates.notNull<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isRedeemed = args.isRedeemed
        rewardId = args.rewardId

        viewModel.getRewardById(rewardId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRewardDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleRewardStatus()

        viewModel.reward.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Resource.Loading -> {

                }

                is Resource.Success -> {
                    status.data?.let {
                        fillRewardDetails(it)
                    }
                }

                else -> {}
            }
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.redeemBtn.setOnClickListener {
            it.visibility = View.GONE
            Snackbar.make(
                binding.root,
                R.string.snackbar_redeemed_reward_message,
                Snackbar.LENGTH_SHORT
            ).setAction("OK") {}.show()
        }
    }

    private fun fillRewardDetails(reward: Model.Reward) {
        with(binding) {
            title.text = reward.name
            description.text = reward.description
            redeemBtn.text = resources.getString(
                R.string.reward_details_redeem_button_text,
                reward.value.toString()
            )
        }
    }

    private fun handleRewardStatus() {
        with(binding) {
            redeemBtn.isVisible = !isRedeemed
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}