package com.dirzaaulia.perqararawg.data.model

import com.squareup.moshi.Json

data class ShortScreenshot(
    @Json(name = "id")
    val id: Int? = null,
    @Json(name = "image")
    val image: String? = null
)