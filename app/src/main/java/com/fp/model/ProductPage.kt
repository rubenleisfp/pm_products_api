package com.fp.model

import kotlinx.serialization.Serializable

/**
 * Represents a paginated response containing a list of products.
 *
 * @property products The list of [Product] objects on the current page.
 * @property total The total number of products available in the entire dataset.
 * @property skip The number of products skipped (offset) from the beginning of the dataset.
 * @property limit The maximum number of products returned in this page.
 */
@Serializable
data class ProductPage(
    val products: List<Product>,
    val total: Int,
    val skip: Int,
    val limit: Int
)