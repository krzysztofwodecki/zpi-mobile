package com.example.gatherpoint.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gatherpoint.R
import com.example.gatherpoint.adapters.EventsAdapter
import com.example.gatherpoint.databinding.FragmentMyEventsBinding
import com.example.gatherpoint.network.Resource
import com.example.gatherpoint.viewmodel.EventsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MyEventsFragment: Fragment() {

    private val viewModel: EventsViewModel by viewModels({ requireParentFragment() })

    private var _binding: FragmentMyEventsBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventsAdapter: EventsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getMyEventsList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventsAdapter = EventsAdapter(
            onEventClicked = { eventId ->
                //navigateToDetails()
            },
            onEventLongClicked = { eventId ->
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(resources.getString(R.string.event_dialog_title))
                    .setItems(arrayOf("Add to favourites", "Delete event")) { _, which ->
                        when (which) {
                            0 -> viewModel.addEventToFavourites(eventId)
                            1 -> viewModel.deleteEvent(eventId)
                        }
                    }.show()
            }
        )

        binding.eventsList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventsAdapter
        }

        viewModel.myEventsList.observe(viewLifecycleOwner) { status ->
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
            viewModel.setMyEventsSearchQuery(it)
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

    companion object {
        fun newInstance() = MyEventsFragment()
    }

}