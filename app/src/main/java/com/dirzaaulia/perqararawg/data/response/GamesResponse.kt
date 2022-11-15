package com.dirzaaulia.perqararawg.data.response

import com.dirzaaulia.perqararawg.data.model.Game
import com.squareup.moshi.Json

data class GamesResponse(
    @Json(name = "count")
    val count: Int? = null,
    @Json(name = "next")
    val next: String? = null,
    @Json(name = "previous")
    val previous: String? = null,
    @Json(name = "results")
    val results: List<Game> = emptyList()
)