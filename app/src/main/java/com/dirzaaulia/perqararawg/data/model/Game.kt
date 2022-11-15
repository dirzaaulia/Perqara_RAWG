package com.dirzaaulia.perqararawg.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "favorite_game_table")
data class Game(
    @PrimaryKey
    @Json(name = "id")
    var id: Int? = null,
    @Json(name = "slug")
    @Ignore
    val slug: String? = null,
    @Json(name = "name")
    var name: String? = null,
    @Json(name = "released")
    var released: String? = null,
    @Json(name = "background_image")
    var backgroundImage: String? = null,
    @Json(name = "rating")
    var rating: Double? = null,
    @Json(name = "ratings_count")
    @Ignore
    val ratingsCount: Int? = null,
    @Json(name = "description")
    @Ignore
    val description: String? = null,
    @Json(name = "developers")
    @Ignore
    val developers: List<Developer> = emptyList()

)