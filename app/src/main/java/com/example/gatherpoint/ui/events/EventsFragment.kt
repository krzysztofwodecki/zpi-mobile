package com.example.gatherpoint.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.gatherpoint.R
import com.example.gatherpoint.adapters.EventsPagerAdapter
import com.example.gatherpoint.databinding.FragmentEventsBinding
import com.example.gatherpoint.viewmodel.EventsViewModel
import com.google.android.material.tabs.TabLayoutMediator

class EventsFragment : Fragment() {

    private val viewModel: EventsViewModel by viewModels()

    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventsPagerAdapter: EventsPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventsPagerAdapter = EventsPagerAdapter(this)
        binding.eventsViewPager.adapter = eventsPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.eventsViewPager) { tab, position ->
            tab.text = resources.getString(
                when (position) {
                    0 -> R.string.events_tab_saved_events_title
                    1 -> R.string.events_tab_near_events_title
                    2 -> R.string.events_tab_my_events_title
                    else -> R.string.events_tab_near_events_title
                }
            )
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}