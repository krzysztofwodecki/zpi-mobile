package com.example.gatherpoint.ui.rewards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gatherpoint.adapters.RewardsAdapter
import com.example.gatherpoint.databinding.FragmentRewardsToRedeemBinding
import com.example.gatherpoint.network.Resource
import com.example.gatherpoint.viewmodel.RewardsViewModel

class RewardsToRedeemFragment : Fragment() {

    private val viewModel: RewardsViewModel by viewModels({ requireParentFragment() })

    private var _binding: FragmentRewardsToRedeemBinding? = null
    private val binding get() = _binding!!
    private lateinit var rewardsAdapter: RewardsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getRewardsList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRewardsToRedeemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rewardsAdapter = RewardsAdapter(
            onRewardClicked = { rewardId ->
                //navigateToDetails()
            }
        )
        binding.rewardsList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rewardsAdapter
        }

        viewModel.rewardsList.observe(viewLifecycleOwner) { status ->
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
            viewModel.setRewardsSearchQuery(it)
        }
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
        fun newInstance() = RewardsToRedeemFragment()
    }
}