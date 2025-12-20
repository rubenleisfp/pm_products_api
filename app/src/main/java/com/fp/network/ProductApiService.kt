package com.fp.network

import com.fp.model.ProductPage
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApiService {


    /**
     * Fetches a paginated list of products from the API.
     * //https://dummyjson.com/products?limit=30&skip=0&select=title,description,category,price,rating,stock,thumbnail
     *
     * This function corresponds to the `GET /products` endpoint. It allows for pagination
     * using `limit` and `skip` parameters and field selection using the `select` parameter.
     *
     * @param limit The maximum number of products to return in a single page. Defaults to 30.
     * @param skip The number of products to skip from the beginning of the list. Defaults to 0.
     * @param select A comma-separated string of product fields to include in the response.
     *               Defaults to "title,description,category,price,rating,stock,thumbnail".
     * @return A [Response] object containing a [ProductPage], which holds the list of products and pagination details.
     */
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = 30,
        @Query("skip") skip: Int = 0,
        @Query("select") select: String = "title,description,category,price,rating,stock,thumbnail"
    ): Response<ProductPage>
}

