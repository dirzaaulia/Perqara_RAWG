package com.dirzaaulia.perqararawg.ui

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.dirzaaulia.avowstmdb.util.ResponseResult
import com.dirzaaulia.avowstmdb.util.success
import com.dirzaaulia.perqararawg.paging.GamePagingSource
import com.dirzaaulia.perqararawg.data.model.Game
import com.dirzaaulia.perqararawg.repository.Repository
import com.dirzaaulia.perqararawg.util.Constant.RAWG_PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (
    private val repository: Repository
): ViewModel() {

    private val _search: MutableStateFlow<String> = MutableStateFlow("")

    private val _isGameFavorited: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isGameFavorited = _isGameFavorited.asStateFlow()

    val games = _search.flatMapLatest { search ->
        Pager(
            config = PagingConfig(pageSize = RAWG_PAGE_SIZE),
            pagingSourceFactory = {
                GamePagingSource(
                    repository = repository,
                    search = search
                )
            }
        ).flow
    }.cachedIn(viewModelScope)

    val favoriteGames = repository.getFavoriteGames().cachedIn(viewModelScope)

    private val _gameState: MutableStateFlow<ResponseResult<Game?>> =
        MutableStateFlow(ResponseResult.Success(null))
    val gameState = _gameState.asStateFlow()

    private val _game: MutableStateFlow<Game?> = MutableStateFlow(null)

    @MainThread
    fun setSearch(search: String) {
        _search.value = search
    }

    fun isGameFavorited(gameId: Int) {
        viewModelScope.launch {
            _isGameFavorited.value = repository.isGameFavorited(gameId)
        }
    }

    fun insertGameToFavorite() = viewModelScope.launch {
        _game.value?.let { repository.insertGameToFavorite(it) }
        }

    fun deleteGameFromFavorite()  = viewModelScope.launch {
        _game.value?.let { repository.deleteGameFromFavorite(it) }
    }

    fun getGameDetail(gameId: Int) {
        repository.getGameDetail(gameId)
            .onEach {
                _gameState.value = it
                it.success { game -> _game.value = game }
            }
            .launchIn(viewModelScope)
    }
}