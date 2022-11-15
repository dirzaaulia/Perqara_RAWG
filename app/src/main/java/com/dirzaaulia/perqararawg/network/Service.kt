package com.dirzaaulia.perqararawg.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.dirzaaulia.perqararawg.data.model.Game
import com.dirzaaulia.perqararawg.data.response.GamesResponse
import com.dirzaaulia.perqararawg.util.Constant.RAWG_API_KEY
import com.dirzaaulia.perqararawg.util.Constant.RAWG_BASE_URL
import com.dirzaaulia.perqararawg.util.Constant.RAWG_PAGE_SIZE
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface Service {

    @GET("games")
    suspend fun getGames(
        @Query("key") key: String = RAWG_API_KEY,
        @Query("page_size") pageSize: Int = RAWG_PAGE_SIZE,
        @Query("page") page: Int,
        @Query("search") search: String
    ): Response<GamesResponse>

    @GET("games/{gameId}")
    suspend fun getGameDetail(
        @Path("gameId") gameId: Int,
        @Query("key") key: String = RAWG_API_KEY
    ): Response<Game>

    companion object {
        fun create(context: Context): Service {
            val httpInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val chuckerInterceptor = ChuckerInterceptor.Builder(context)
                .collector(ChuckerCollector(context))
                .maxContentLength(250000L)
                .redactHeaders(emptySet())
                .alwaysReadResponseBody(false)
                .build()

            val client = OkHttpClient.Builder()
                .addInterceptor(httpInterceptor)
                .addInterceptor(chuckerInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl(RAWG_BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(Service::class.java)
        }
    }
}