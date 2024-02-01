package com.example.gatherpoint.ui.rewards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gatherpoint.R
import com.example.gatherpoint.adapters.RewardsAdapter
import com.example.gatherpoint.databinding.FragmentRedeemedRewardsBinding
import com.example.gatherpoint.network.Model
import com.example.gatherpoint.network.Resource
import com.example.gatherpoint.ui.events.EventsFragmentDirections
import com.example.gatherpoint.viewmodel.RewardsViewModel

class RedeemedRewardsFragment: Fragment() {

    private val viewModel: RewardsViewModel by navGraphViewModels(R.id.nav_graph_dashboard)

    private var _binding: FragmentRedeemedRewardsBinding? = null
    private val binding get() = _binding!!
    private lateinit var rewardsAdapter: RewardsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getRedeemedRewardsList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRedeemedRewardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rewardsAdapter = RewardsAdapter(
            onRewardClicked = { reward ->
                navigateToDetails(reward)
            }
        )
        binding.rewardsList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rewardsAdapter
        }

        viewModel.redeemedRewardsList.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Resource.Loading -> {
                    binding.shimmerLayout.startShimmer()
                }

                is Resource.Success -> {
                    status.data?.let {
                        rewardsAdapter.setRewardsList(it)
                        showRecyclerView()
                    }
                }

                else -> {}
            }
        }

        binding.searchInput.searchQuery.observe(requireActivity()) {
            viewModel.setRedeemedRewardsSearchQuery(it)
        }
    }

    private fun navigateToDetails(reward: Model.Reward) {
        val action = RewardsFragmentDirections.actionRewardsFragmentToRewardDetailsFragment(
            rewardId = reward.id,
            isRedeemed = true
        )
        findNavController().navigate(action)
    }

    private fun showRecyclerView() {
        binding.shimmerLayout.apply {
            stopShimmer()
            visibility = View.GONE
        }
        binding.rewardsList.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = RedeemedRewardsFragment()
    }

}