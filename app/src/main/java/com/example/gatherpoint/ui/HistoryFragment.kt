package com.example.gatherpoint.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gatherpoint.R
import com.example.gatherpoint.adapters.EventsAdapter
import com.example.gatherpoint.databinding.FragmentHistoryBinding
import com.example.gatherpoint.network.Resource
import com.example.gatherpoint.viewmodel.HistoryViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HistoryFragment: Fragment() {

    private val viewModel: HistoryViewModel by viewModels()

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventsAdapter: EventsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getHistoryEventsList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventsAdapter = EventsAdapter(
            onEventClicked = { event ->
                //navigateToDetails()
            },
            onEventLongClicked = { event ->
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(resources.getString(R.string.event_dialog_title))
                    .setItems(arrayOf("Add to favourites")) { _, which ->
                        when (which) {
                            0 -> viewModel.addEventToFavourites(event.id)
                        }
                    }.show()
            }
        )
        binding.eventsList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventsAdapter
        }

        viewModel.historyEventsList.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Resource.Loading -> {
                    binding.shimmerLayout.startShimmer()
                }

                is Resource.Success -> {
                    status.data?.let {
                        eventsAdapter.setEventsList(it)
                        showRecyclerView()
                    }
                }

                else -> {}
            }
        }

        binding.searchInput.searchQuery.observe(requireActivity()) {
            viewModel.setHistoryEventsSearchQuery(it)
        }
    }

    private fun showRecyclerView() {
        binding.shimmerLayout.apply {
            stopShimmer()
            visibility = View.GONE
        }
        binding.eventsList.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}