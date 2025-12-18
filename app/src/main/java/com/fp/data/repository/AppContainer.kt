package com.fp.data.repository

import com.fp.network.ProductApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit



interface AppContainer  {
    val productRepository: ProductsRepository
}

class DefaultAppContainer() : AppContainer {

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
    // 👇 ESTA ES LA LÍNEA CORREGIDA 👇
// Usando by lazy (también correcto y a menudo preferido)
//    override val productsRepository: ProductsRepository by lazy {
//        NetworkProductsRepository(retrofitService)
//    }
    override val productRepository: ProductsRepository
        get() = NetworkProductsRepository(retrofitService)

}
