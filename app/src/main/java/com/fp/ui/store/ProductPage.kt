package com.fp.ui.store

import kotlinx.serialization.Serializable


@Serializable
data class ProductPage(
    val products: List<Product>,
    val total: Int,
    val skip: Int,
    val limit: Int
)