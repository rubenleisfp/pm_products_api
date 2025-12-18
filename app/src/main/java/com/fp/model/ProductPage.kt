package com.fp.model

import kotlinx.serialization.Serializable


@Serializable
data class ProductPage(
    val products: List<Product>,
    val total: Int,
    val skip: Int,
    val limit: Int
)