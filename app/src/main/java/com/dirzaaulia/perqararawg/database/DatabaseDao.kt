package com.dirzaaulia.perqararawg.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dirzaaulia.perqararawg.data.model.Game

@Dao
interface DatabaseDao {

    @Insert(
        entity = Game::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertGame(game: Game)

    @Delete(entity = Game::class)
    suspend fun deleteGame(game: Game)

    @Query("SELECT EXISTS(SELECT * FROM favorite_game_table WHERE id = :gameId)")
    suspend fun isGameFavorited(gameId: Int): Boolean

    @Query("SELECT * FROM favorite_game_table")
    fun getFavoriteGames(): PagingSource<Int, Game>
}