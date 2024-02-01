package com.example.gatherpoint.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.gatherpoint.R
import com.example.gatherpoint.databinding.FragmentEventDetailsBinding
import com.example.gatherpoint.network.Model
import com.example.gatherpoint.network.Resource
import com.example.gatherpoint.utils.Prefs
import com.example.gatherpoint.viewmodel.EventsViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.properties.Delegates

class EventDetailsFragment : Fragment() {

    private val viewModel: EventsViewModel by navGraphViewModels(R.id.nav_graph_dashboard)
    private val args: EventDetailsFragmentArgs by navArgs()

    private var _binding: FragmentEventDetailsBinding? = null
    private val binding get() = _binding!!
    private var eventId by Delegates.notNull<Long>()
    private var isOwner by Delegates.notNull<Boolean>()
    private lateinit var token: String
    private var userId by Delegates.notNull<Long>()
    private var selectedDateLong: Long? = null
    private var selectedTimeMinutes: Int? = null
    private var selectedTimeHours: Int? = null
    private var defaultDate: Date? = null

    private val outputDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventId = args.eventId
        isOwner = args.isOwner

        Prefs(requireActivity()).let { prefs ->
            token = prefs.token!!
            userId = prefs.userId
            if (eventId > 0) {
                viewModel.getEventById(token, eventId)
            } else {
                viewModel.clearEvent()
            }
        }
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
            binding.progress.isVisible = status is Resource.Loading
            if (eventId < 0) return@observe
            when (status) {
                is Resource.Loading -> {}

                is Resource.Success -> {
                    status.data?.let {
                        fillEventDetails(it)
                    }
                }

                else -> {}
            }
        }

        viewModel.eventDetailsStatus.observe(viewLifecycleOwner) { status ->
            binding.progress.isVisible = status is Resource.Loading
            when (status) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    status.data?.let {
                        Toast.makeText(requireContext(), "Event saved!", Toast.LENGTH_SHORT).show()
                        viewModel.clearEventStatus()
                        findNavController().popBackStack()
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Cannot save event", Toast.LENGTH_SHORT).show()
                    viewModel.clearEventStatus()
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

        binding.dateBtn.setOnClickListener { initDatePicker() }

        binding.timeBtn.setOnClickListener {
            initTimePicker()
        }

        binding.saveButton.setOnClickListener {
            if (eventId < 0) {
                viewModel.addEvent(
                    token = token,
                    userId = userId,
                    title = binding.titleInput.text.toString(),
                    location = binding.locationInput.text.toString(),
                    description = binding.descriptionInput.text.toString(),
                    date = getDateFromPickers()
                )
            } else {
                viewModel.event.value?.data?.let { event ->
                    viewModel.editEvent(
                        token = token,
                        event.id,
                        userId,
                        binding.titleInput.text.toString(),
                        binding.locationInput.text.toString(),
                        binding.descriptionInput.text.toString(),
                        getDateFromPickers()
                    )
                }
            }
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getDateFromPickers(): String? {
        if ((selectedTimeMinutes == null || selectedTimeHours == null || selectedDateLong == null) && eventId < 0) {
            Toast.makeText(requireContext(), "Please input date and time", Toast.LENGTH_SHORT).show()
            return null
        }
        if (selectedTimeMinutes != null && selectedTimeHours != null && selectedDateLong != null) {
            val cal: Calendar = Calendar.getInstance()
            cal.time = Date(selectedDateLong!!)
            cal.set(Calendar.HOUR_OF_DAY, selectedTimeHours!!)
            cal.set(Calendar.MINUTE, selectedTimeMinutes!!)
            cal.set(Calendar.MILLISECOND, 1)
            val timeInMillis = cal.timeInMillis

            val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
            outputFormat.timeZone = TimeZone.getTimeZone("UTC")
            return outputFormat.format(Date(timeInMillis))
        }
        return null
    }

    private fun initTimePicker() {
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select event time")
                .build()
        picker.addOnPositiveButtonClickListener {
            val hour = picker.hour
            val minute = picker.minute
            selectedTimeMinutes = minute
            selectedTimeHours = hour
            binding.timeBtn.text = "$hour:$minute"
        }
        picker.show(childFragmentManager, null)
    }

    private fun initDatePicker() {
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(constraintsBuilder.build())
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        datePicker.addOnPositiveButtonClickListener {
            val dateString = outputDateFormat.format(it)
            selectedDateLong = it
            binding.dateBtn.text = dateString
        }
        datePicker.show(childFragmentManager, null)
    }

    private fun fillEventDetails(event: Model.Event) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date: Date = inputFormat.parse(event.eventDateTime) ?: Date()
        defaultDate = date
        val outputFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val outputFormatDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val outputFormatTime = SimpleDateFormat("HH:mm", Locale.getDefault())
        val outputDateTimeString: String = outputFormat.format(date)
        val outputDateString: String = outputFormatDate.format(date)
        val outputTimeString: String = outputFormatTime.format(date)
        with(binding) {
            listOf(title, titleInput).forEach { it.text = event.eventName }
            locationInput.setText(event.location)
            dateBtn.text = outputDateString
            timeBtn.text = outputTimeString
            location.text = outputDateTimeString + ", " + event.location
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
            location.visibility = View.VISIBLE
            listOf(dateBtn, timeBtn).forEach {
                it.visibility = View.GONE
            }
            locationInputContainer.visibility = View.GONE
            description.visibility = View.VISIBLE
            descriptionInputContainer.visibility = View.GONE
        }
    }

    private fun initEventCreation() {
        with(binding) {
            attendBtn.visibility = View.GONE
            saveButton.visibility = View.VISIBLE
            title.visibility = View.GONE
            titleInputContainer.visibility = View.VISIBLE
            location.visibility = View.GONE
            locationInputContainer.visibility = View.VISIBLE
            listOf(dateBtn, timeBtn).forEach {
                it.visibility = View.VISIBLE
            }
            description.visibility = View.GONE
            descriptionInputContainer.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}