package com.garibyan.armen.tbc_classwork_5.nodel

import com.garibyan.armen.tbc_classwork_5.nodel.network.ViewItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

object ApiClient {
    private const val BASE_URL = "https://run.mocky.io/v3/"
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

interface ApiService{
    @GET("a8169f82-d5c3-415d-bae4-88d3f6ee6d44")
    suspend fun getData(): Response<List<List<ViewItem>>>
}