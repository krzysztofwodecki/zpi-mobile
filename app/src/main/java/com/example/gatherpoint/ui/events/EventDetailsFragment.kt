package com.example.gatherpoint.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.gatherpoint.R
import com.example.gatherpoint.databinding.FragmentEventDetailsBinding
import com.example.gatherpoint.network.Model
import com.example.gatherpoint.network.Resource
import com.example.gatherpoint.viewmodel.EventsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlin.properties.Delegates

class EventDetailsFragment : Fragment() {

    private val viewModel: EventsViewModel by navGraphViewModels(R.id.nav_graph_dashboard)
    val args: EventDetailsFragmentArgs by navArgs()

    private var _binding: FragmentEventDetailsBinding? = null
    private val binding get() = _binding!!
    private var eventId by Delegates.notNull<Long>()
    private var isOwner by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventId = args.eventId
        isOwner = args.isOwner

        viewModel.getEventById(eventId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleEventStatus()

        viewModel.event.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    status.data?.let {
                        fillEventDetails(it)
                    }
                }
                else -> {}
            }
        }

        binding.attendBtn.setOnClickListener {
            it.visibility = View.GONE
            Snackbar.make(
                binding.root,
                resources.getString(R.string.snackbar_points_gained_message, "200"),
                Snackbar.LENGTH_SHORT
            ).setAction("OK") {}.show()
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun fillEventDetails(event: Model.Event) {
        with(binding) {
            listOf(title, titleInput).forEach { it.text = event.eventName }
            listOf(info, infoInput).forEach { it.text = event.location }
            listOf(description, descriptionInput).forEach { it.text = event.description }
        }
    }

    private fun handleEventStatus() {
        when {
            eventId < 0 -> initEventCreation()
            eventId > 0 && isOwner -> initEventCreation()
            else -> initEventPreview()
        }
    }

    private fun initEventPreview() {
        with(binding) {
            saveButton.visibility = View.GONE
            title.visibility = View.VISIBLE
            titleInputContainer.visibility = View.GONE
            info.visibility = View.VISIBLE
            infoInputContainer.visibility = View.GONE
            description.visibility = View.VISIBLE
            descriptionInputContainer.visibility = View.GONE
        }
    }

    private fun initEventCreation() {
        with(binding) {
            saveButton.visibility = View.VISIBLE
            title.visibility = View.GONE
            titleInputContainer.visibility = View.VISIBLE
            info.visibility = View.GONE
            infoInputContainer.visibility = View.VISIBLE
            description.visibility = View.GONE
            descriptionInputContainer.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}