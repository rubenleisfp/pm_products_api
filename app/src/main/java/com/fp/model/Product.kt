package com.fp.model

import kotlinx.serialization.Serializable


/**
 * Represents a product entity.
 *
 * This data class holds all the essential information about a product,
 * such as its identifier, name, description, and pricing details.
 * It is marked as `@Serializable` to support serialization, for example, to JSON.
 *
 * @property id The unique identifier for the product.
 * @property title The name or title of the product.
 * @property description A detailed description of the product.
 * @property category The category to which the product belongs.
 * @property price The selling price of the product.
 * @property rating The average customer rating of the product.
 * @property stock The number of items available in stock.
 * @property thumbnail The URL of the product's thumbnail image.
 */
@Serializable
data class Product(
    val id : Int,
    val title : String,
    val description : String,
    val category: String,
    val price: Double,
    val rating: Double,
    val stock: Int,
    val thumbnail: String

)