package com.example.gatherpoint.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gatherpoint.R
import com.example.gatherpoint.databinding.RecyclerviewRewardsItemBinding
import com.example.gatherpoint.network.Model

class RewardsAdapter (
    private val onRewardClicked: (eventId: Long) -> Unit
) : RecyclerView.Adapter<RewardsAdapter.RewardViewHolder>() {

    private var rewardsList = emptyList<Model.Reward>()

    override fun onBindViewHolder(holder: RewardViewHolder, position: Int) {
        holder.bind(rewardsList[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RewardViewHolder {
        val binding: RecyclerviewRewardsItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recyclerview_rewards_item, parent, false
        )
        return RewardViewHolder(binding)
    }

    override fun getItemCount() = rewardsList.size

    inner class RewardViewHolder(
        private val binding: RecyclerviewRewardsItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var rewardItem: Model.Reward

        init {
            itemView.setOnClickListener { onRewardClicked.invoke(rewardItem.id) }
        }

        fun bind(rewardItem: Model.Reward) {
            this.rewardItem = rewardItem

            with(binding) {
                title.text = rewardItem.name
                description.text = itemView.context.getString(R.string.lorem_ipsum)
                points.text = itemView.context.resources.getString(
                    R.string.rewards_points_label,
                    rewardItem.value.toString()
                )
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setRewardsList(rewardItems: List<Model.Reward>) {
        rewardsList = rewardItems
        notifyDataSetChanged()
    }
}