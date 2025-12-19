package com.fp.model

/**
 * Represents a paginated response for a list of products.
 *
 * This data class is a wrapper for a collection of products, providing metadata
 * about the pagination state, such as the total number of products available,
 * the number of products skipped, and the page size limit.
 *
 * @property productsWrapper A list of [ProductWrapper] objects on the current page.
 * @property total The total number of products available across all pages.
 * @property skip The number of products skipped in the current result set (offset).
 * @property limit The maximum number of products returned per page.
 */
data class ProductPageWrapper (
    val productsWrapper: List<ProductWrapper>,
    val total: Int,
    val skip: Int,
    val limit: Int
)