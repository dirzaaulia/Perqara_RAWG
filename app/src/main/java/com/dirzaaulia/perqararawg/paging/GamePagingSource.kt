package com.dirzaaulia.perqararawg.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dirzaaulia.avowstmdb.util.pagingSucceeded
import com.dirzaaulia.perqararawg.data.model.Game
import com.dirzaaulia.perqararawg.repository.Repository
import com.dirzaaulia.perqararawg.util.Constant.RAWG_PAGE_STARTING_INDEX

class GamePagingSource (
    private val repository: Repository,
    private val search: String
): PagingSource<Int, Game>() {
    override fun getRefreshKey(state: PagingState<Int, Game>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {
        val page = params.key ?: RAWG_PAGE_STARTING_INDEX

        return repository.getGames(page = page, search = search).pagingSucceeded { data ->
            LoadResult.Page(
                data = data.results,
                prevKey = if (data.previous?.isNotBlank() == true) page - 1 else null,
                nextKey = if (data.next?.isNotBlank() == true) page + 1 else null
            )
        }
    }
}