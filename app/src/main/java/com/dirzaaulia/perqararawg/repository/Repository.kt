package com.dirzaaulia.perqararawg.repository

import androidx.annotation.WorkerThread
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.dirzaaulia.avowstmdb.util.ResponseResult
import com.dirzaaulia.avowstmdb.util.executeWithResponse
import com.dirzaaulia.perqararawg.data.model.Game
import com.dirzaaulia.perqararawg.database.DatabaseDao
import com.dirzaaulia.perqararawg.network.Service
import com.dirzaaulia.perqararawg.util.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class Repository @Inject constructor(
 private val service: Service,
 private val databaseDao: DatabaseDao
) {

    /**
     * API
     */
    @WorkerThread
    suspend fun getGames(
        page: Int,
        search: String
    ) = withContext(Dispatchers.IO) {
        executeWithResponse {
            val response = service.getGames(page = page, search = search)
            response.body() ?: run { throw HttpException(response) }
        }
    }

    @WorkerThread
    fun getGameDetail(gameId: Int) = flow {
        emit(ResponseResult.Loading)
        try {
            val response = service.getGameDetail(gameId)
            response.body()?.let {
                emit(ResponseResult.Success(it))
            } ?: run {
                throw HttpException(response)
            }
        } catch (throwable: Throwable) {
            emit(ResponseResult.Error(throwable))
        }
    }

    /**
     * Room
     */
    fun getFavoriteGames() = Pager(
        config = PagingConfig(pageSize = Constant.RAWG_PAGE_SIZE),
        pagingSourceFactory = { databaseDao.getFavoriteGames() }
    ).flow

    suspend fun isGameFavorited(gameId: Int) = databaseDao.isGameFavorited(gameId)

    suspend fun insertGameToFavorite(game: Game) = databaseDao.insertGame(game)

    suspend fun deleteGameFromFavorite(game: Game) = databaseDao.deleteGame(game)
}