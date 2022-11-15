package com.dirzaaulia.perqararawg.ui.detail

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.dirzaaulia.avowstmdb.util.error
import com.dirzaaulia.avowstmdb.util.isError
import com.dirzaaulia.avowstmdb.util.isLoading
import com.dirzaaulia.avowstmdb.util.isSucceeded
import com.dirzaaulia.avowstmdb.util.success
import com.dirzaaulia.perqararawg.R
import com.dirzaaulia.perqararawg.data.model.Game
import com.dirzaaulia.perqararawg.databinding.FragmentDetailBinding
import com.dirzaaulia.perqararawg.ui.MainViewModel
import com.dirzaaulia.perqararawg.util.changeDateFormat
import com.dirzaaulia.perqararawg.util.loadNetworkImage
import com.dirzaaulia.perqararawg.util.replaceIfNull
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.TimeZone

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    private var toolbar: MaterialToolbar? = null

    private val viewModel: MainViewModel by activityViewModels()
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadArguments()
        setupToolbar()
        subscribeGame()
        subscribeIsGameFavorited()
    }

    private fun setupToolbar() {
        toolbar = activity?.findViewById(R.id.toolbar)
        toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_favorite -> {
                    if (viewModel.isGameFavorited.value)
                        viewModel.deleteGameFromFavorite()
                            .invokeOnCompletion {
                                Snackbar.make(binding.root, "Game Unfavorited", Snackbar.LENGTH_SHORT).show()
                                viewModel.isGameFavorited(args.id)
                            }
                    else
                        viewModel.insertGameToFavorite()
                            .invokeOnCompletion {
                                Snackbar.make(binding.root, "Game Favorited", Snackbar.LENGTH_SHORT).show()
                                viewModel.isGameFavorited(args.id)
                            }
                    true
                }
                else -> false
            }
        }
        toolbar?.setNavigationOnClickListener {
            binding.root.findNavController().navigateUp()
        }
    }

    private fun loadArguments() {
        viewModel.apply {
            isGameFavorited(args.id)
            getGameDetail(args.id)
        }
    }

    private fun subscribeGame() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.gameState.collect { state ->
                    when {
                        state.isLoading -> binding.root.displayedChild = 0
                        state.isSucceeded -> {
                            state.success {
                                setSuccessView(it)
                            }
                        }
                        state.isError -> {
                            state.error {
                                setErrorView(it.message.toString())
                            }
                        }
                    }
                }
            }
        }
    }


    private fun subscribeIsGameFavorited() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.isGameFavorited.collect {
                    if (it) {
                        toolbar?.menu?.getItem(0)?.icon =
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_baseline_favorite_24
                            )
                    } else {
                        toolbar?.menu?.getItem(0)?.icon =
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_baseline_favorite_border_24
                            )
                    }
                }
            }
        }
    }

    private fun setSuccessView(game: Game?) {
        binding.apply {
            contentDetail.apply {
                image.loadNetworkImage(game?.backgroundImage.replaceIfNull())
                var publisher = ""
                game?.developers?.forEach {
                    publisher += "${it.name} "
                }
                this.publisher.text = publisher
                name.text = game?.name
                val releaseDateFormatted = game?.released?.changeDateFormat(
                    "yyyy-MM-dd",
                    "dd MMMM yyyy",
                    TimeZone.getDefault().id
                )
                releaseDate.text = getString(R.string.release_date_format, releaseDateFormatted)
                rating.text = "${game?.rating}"
                ratingCount.text = getString(R.string.rating_count_format, game?.ratingsCount)
                description.text = Html.fromHtml(game?.description, Html.FROM_HTML_MODE_LEGACY)
            }
            root.displayedChild = 1
        }
    }

    private fun setErrorView(message: String){
        binding.apply {
            viewCommonError.apply {
                errorMessage.text = message
                buttonRetry.setOnClickListener { viewModel.getGameDetail(args.id) }
            }
            root.displayedChild = 2
        }
    }

}