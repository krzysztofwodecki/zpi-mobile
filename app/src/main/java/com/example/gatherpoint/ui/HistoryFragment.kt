package com.example.gatherpoint.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gatherpoint.R
import com.example.gatherpoint.adapters.EventsAdapter
import com.example.gatherpoint.databinding.FragmentHistoryBinding
import com.example.gatherpoint.network.Model
import com.example.gatherpoint.network.Resource
import com.example.gatherpoint.utils.Prefs
import com.example.gatherpoint.viewmodel.HistoryViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.properties.Delegates

class HistoryFragment: Fragment() {

    private val viewModel: HistoryViewModel by viewModels()

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventsAdapter: EventsAdapter

    private lateinit var token: String
    private var userId by Delegates.notNull<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Prefs(requireActivity()).let { prefs ->
            token = prefs.token!!
            userId = prefs.userId
            viewModel.getHistoryEventsList(token, userId)
        }
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
                navigateToDetails(event)
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

    private fun navigateToDetails(event: Model.Event) {
        val action = HistoryFragmentDirections.actionHistoryFragmentToEventDetailsFragment(
            eventId = event.id,
            isOwner = false
        )
        findNavController().navigate(action)
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