package com.dirzaaulia.perqararawg.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.dirzaaulia.perqararawg.R
import com.dirzaaulia.perqararawg.data.model.Game
import com.dirzaaulia.perqararawg.databinding.FragmentHomeBinding
import com.dirzaaulia.perqararawg.ui.MainViewModel
import com.dirzaaulia.perqararawg.ui.common.adapter.GameAdapter
import com.dirzaaulia.perqararawg.ui.common.adapter.CommonLoadStateAdapter
import com.dirzaaulia.perqararawg.util.replaceIfNull
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: MainViewModel by activityViewModels()

    private val adapter = GameAdapter(this::onGameClicked)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        subscribeGames()
        setupSearch()
    }

    private fun subscribeGames() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.games.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    private fun setupSearch() {
        binding.editTextSearch.doAfterTextChanged {
            viewModel.setSearch(it.toString())
        }
    }

    private fun onGameClicked(game: Game) {
        val gameId = game.id.replaceIfNull()
        if (gameId != 0) {
            val directions = HomeFragmentDirections.actionHomeFragmentToDetailFragment(gameId)
            binding.root.findNavController().navigate(directions)
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