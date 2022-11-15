package com.dirzaaulia.perqararawg.ui.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dirzaaulia.perqararawg.R
import com.dirzaaulia.perqararawg.data.model.Game
import com.dirzaaulia.perqararawg.databinding.ItemGameBinding
import com.dirzaaulia.perqararawg.util.changeDateFormat
import com.dirzaaulia.perqararawg.util.loadNetworkImage
import java.util.TimeZone

typealias OnGameClicked = (Game) -> Unit

class GameAdapter(
    private val onGameClicked: OnGameClicked
): PagingDataAdapter<Game, GameAdapter.ViewHolder>(COMPARATOR) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(item) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemGameBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    inner class ViewHolder(
        private val binding: ItemGameBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Game) {
            binding.apply {
                root.setOnClickListener { onGameClicked(item) }

                image.loadNetworkImage(item.backgroundImage.toString())
                title.text = item.name
                val releaseDateFormatted = item.released?.changeDateFormat(
                    "yyyy-MM-dd",
                    "dd MMMM yyyy",
                    TimeZone.getDefault().id
                )
                releaseDate.text = root.context.getString(R.string.release_date_format, releaseDateFormatted)
                rating.text = "${item.rating}"
            }
        }
    }

    companion object {
        val COMPARATOR = object: DiffUtil.ItemCallback<Game>() {
            override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
                return oldItem == newItem
            }

        }
    }
}