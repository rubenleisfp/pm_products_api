package com.fp.ui.store

/**
 * Created by Your name on 17/09/2025.
 */
data class ProductPageWrapper (
    val productsWrapper: List<ProductWrapper>,
    val total: Int,
    val skip: Int,
    val limit: Int
)