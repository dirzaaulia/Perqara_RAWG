package com.dirzaaulia.perqararawg.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.dirzaaulia.perqararawg.R
import com.dirzaaulia.perqararawg.data.model.Game
import com.dirzaaulia.perqararawg.databinding.FragmentFavoriteBinding
import com.dirzaaulia.perqararawg.ui.MainViewModel
import com.dirzaaulia.perqararawg.ui.common.adapter.GameAdapter
import com.dirzaaulia.perqararawg.ui.common.adapter.CommonLoadStateAdapter
import com.dirzaaulia.perqararawg.ui.home.HomeFragmentDirections
import com.dirzaaulia.perqararawg.util.replaceIfNull
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding

    private val viewModel: MainViewModel by activityViewModels()

    private val adapter = GameAdapter(this::onGameClicked)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        subscribeFavoriteGames()
    }

    private fun onGameClicked(game: Game) {
        val gameId = game.id.replaceIfNull()
        if (gameId != 0) {
            val directions = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(gameId)
            binding.root.findNavController().navigate(directions)
        }
    }

    private fun subscribeFavoriteGames() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favoriteGames.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    private fun setupAdapter() {
        binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            CommonLoadStateAdapter { adapter.retry() },
            CommonLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            when (loadState.source.refresh) {
                is LoadState.Loading -> binding.viewAnimator.displayedChild = 0
                is LoadState.NotLoading -> {
                    if (adapter.itemCount == 0) {
                        setViewBasedOnState(loadState, getString(R.string.empty_games))
                    } else {
                        binding.viewAnimator.displayedChild = 1
                    }
                }
                is LoadState.Error -> {
                    val errorMessage = (loadState.source.refresh as LoadState.Error).error.message
                    setViewBasedOnState(loadState, errorMessage.toString())
                }
            }
        }
    }

    private fun setViewBasedOnState(
        loadState: CombinedLoadStates,
        message: String
    ) {
        binding.apply {
            viewCommonError.apply {
                errorMessage.text = message
                buttonRetry.apply {
                    isVisible = loadState.source.refresh is LoadState.Error
                    setOnClickListener { adapter.retry() }
                }
            }
            viewAnimator.displayedChild = 2
        }
    }
}