package com.fp.network


import com.fp.ui.product.Product

data class ProductsResponse(
    val products: List<Product>,
    val total: Int,
    val skip: Int,
    val limit: Int
)
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Query
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory



//https://dummyjson.com/products?limit=30&skip=0&select=title,description,category,price,rating,stock,thumbnail
private const val BASE_URL = "https://dummyjson.com/products/"

private val json = Json {
    ignoreUnknownKeys = true
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()


interface ProductApiService {
//    @POST("login")
//    suspend fun login(@Body loginData: LoginData): Response<Void>

    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = 30,
        @Query("skip") skip: Int = 0,
        @Query("select") select: String = "title,description,category,price,rating,stock,thumbnail"
    ): Response<ProductsResponse>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object ProductApi {
    val retrofitService: ProductApiService by lazy {
        retrofit.create(ProductApiService::class.java)
    }
}