package com.fp.model

/**
 * Contiene un objeto de producto, un indicador de si está expandido y un ID único.
 *
 * @property product El objeto de producto.
 * @property expanded Indica si el objeto de producto está expandido o no.
 * @property id El ID único del objeto de producto.
 */
data class ProductWrapper (
    val product: Product,
    val expanded: Boolean,
    val id: Int
)