package com.dirzaaulia.perqararawg.data.model

import com.squareup.moshi.Json

data class Developer(
    @Json(name = "id")
    val id: Int? = null,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "slug")
    val slug: String? = null,
    @Json(name= "games_count")
    val gamesCount: Int? = null,
    @Json(name = "image_background")
    val imageBackground: String? = null
)