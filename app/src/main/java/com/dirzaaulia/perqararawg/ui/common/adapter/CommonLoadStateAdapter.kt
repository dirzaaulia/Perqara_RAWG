package com.dirzaaulia.perqararawg.ui.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dirzaaulia.perqararawg.databinding.ViewLoadStateBinding

class CommonLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<CommonLoadStateAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(
            ViewLoadStateBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    inner class ViewHolder(
        private val binding: ViewLoadStateBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.viewCommonError.apply {
                buttonRetry.setOnClickListener { retry.invoke() }
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                if (loadState is LoadState.Error) {
                    viewCommonError.errorMessage.text = loadState.error.localizedMessage
                }

                when (loadState) {
                    is LoadState.NotLoading -> { }
                    LoadState.Loading -> root.displayedChild = 0
                    is LoadState.Error -> root.displayedChild = 1
                }
            }
        }
    }
}