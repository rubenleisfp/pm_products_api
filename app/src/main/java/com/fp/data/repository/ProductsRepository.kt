package com.fp.data.repository

import com.fp.model.ProductPage
import com.fp.model.ProductPageWrapper
import com.fp.model.ProductWrapper
import com.fp.network.ProductApiService
import retrofit2.Response

/**
 * Created by Your name on 16/12/2025.
 */


/**
 * Repository that fetch products list from API Products
 */
interface ProductsRepository {
    suspend fun getProducts(): ProductPageWrapper
}

/**
 * Network Implementation of Repository that fetch products from API
 */
class NetworkProductsRepository( private val productApiService: ProductApiService) : ProductsRepository {
    override suspend fun getProducts(): ProductPageWrapper {
        val productsResponse : Response<ProductPage> = productApiService.getProducts()
        if (productsResponse.isSuccessful) {
            val productPageWrapper = getWrapper(productsResponse.body()!!)
            return productPageWrapper
        } else {
            throw ApiException(
                statusCode = productsResponse.code(),
                errorMessage = productsResponse.message()
            )
        }
    }

    private fun getWrapper(productPage: ProductPage): ProductPageWrapper {

        val productsWrapperList = productPage.products.mapIndexed { index, product ->
            ProductWrapper(product = product, id = index, expanded = false)
        }
        var productPageWrapper = ProductPageWrapper(
            productsWrapperList,
            productPage.total,
            productPage.skip,
            productPage.limit
        )
        return productPageWrapper
    }
}