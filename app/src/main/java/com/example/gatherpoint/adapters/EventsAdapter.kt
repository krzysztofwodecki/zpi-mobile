package com.example.gatherpoint.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gatherpoint.R
import com.example.gatherpoint.databinding.RecyclerviewEventsItemBinding
import com.example.gatherpoint.network.Model.Event
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventsAdapter (
    private val onEventClicked: (eventId: Long) -> Unit,
    private val onEventLongClicked: (eventId: Long) -> Unit
) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    private var eventsList = emptyList<Event>()

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(eventsList[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder {
        val binding: RecyclerviewEventsItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recyclerview_events_item, parent, false
        )
        return EventViewHolder(binding)
    }

    override fun getItemCount() = eventsList.size

    inner class EventViewHolder(
        private val binding: RecyclerviewEventsItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var eventItem: Event

        init {
            itemView.setOnClickListener { onEventClicked.invoke(eventItem.id) }
            itemView.setOnLongClickListener {
                onEventLongClicked.invoke(eventItem.id)
                return@setOnLongClickListener true
            }
        }

        fun bind(eventItem: Event) {
            this.eventItem = eventItem

            with(binding) {
                title.text = eventItem.eventName
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
                val date: Date = inputFormat.parse(eventItem.eventDateTime) ?: Date()
                val outputFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                val outputDateString: String = outputFormat.format(date)
                info.text = outputDateString + ", " + eventItem.location
                description.text = itemView.context.getString(R.string.lorem_ipsum)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setEventsList(eventItems: List<Event>) {
        eventsList = eventItems
        notifyDataSetChanged()
    }
}