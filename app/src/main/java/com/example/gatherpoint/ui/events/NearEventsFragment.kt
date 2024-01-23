package com.example.gatherpoint.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gatherpoint.R
import com.example.gatherpoint.adapters.EventsAdapter
import com.example.gatherpoint.databinding.FragmentNearEventsBinding
import com.example.gatherpoint.network.Model
import com.example.gatherpoint.network.Resource
import com.example.gatherpoint.utils.Prefs
import com.example.gatherpoint.viewmodel.EventsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NearEventsFragment : Fragment() {

    private val viewModel: EventsViewModel by navGraphViewModels(R.id.nav_graph_dashboard)

    private var _binding: FragmentNearEventsBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventsAdapter: EventsAdapter
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Prefs(requireActivity()).token?.let {
            token = it
            viewModel.getNearEventsList(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNearEventsBinding.inflate(inflater, container, false)
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

        viewModel.nearEventsList.observe(viewLifecycleOwner) { status ->
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
            viewModel.setNearEventsSearchQuery(it)
        }
    }

    private fun navigateToDetails(event: Model.Event) {
        val action = EventsFragmentDirections.actionEventsFragmentToEventDetailsFragment(
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

    companion object {
        fun newInstance() = NearEventsFragment()
    }

}