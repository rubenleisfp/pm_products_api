package com.fp.ui.store

import kotlinx.serialization.Serializable


/**
 * [Product] is the data class to represent the Affirmation text and imageResourceId
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