package com.fp.data

import android.content.Context
import com.fp.data.repository.DatabaseFavoriteProductRepository
import com.fp.data.repository.FavoriteProductRepository
import com.fp.data.repository.NetworkProductsRepository
import com.fp.data.repository.ProductsRepository
import com.fp.network.ProductApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit



interface AppContainer  {
    val productRepository: ProductsRepository
    val favoriteProductRepository : FavoriteProductRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val BASE_URL =
        "https://dummyjson.com/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: ProductApiService by lazy {
        retrofit.create(ProductApiService::class.java)
    }

    /**
     * A repository for accessing product data.
     * The custom getter ensures a new instance of the repository is provided
     * every time it's accessed, injecting the retrofit service.
     */
    override val productRepository: ProductsRepository
        get() = NetworkProductsRepository(retrofitService)
    override val favoriteProductRepository: FavoriteProductRepository
        get() = DatabaseFavoriteProductRepository(ProductDatabase.getDatabase(context).itemDao())

}
