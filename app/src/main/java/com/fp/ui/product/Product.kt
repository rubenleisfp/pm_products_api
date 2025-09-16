package com.fp.ui.product

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import java.time.LocalDate

/**
 * [Product] is the data class to represent the Affirmation text and imageResourceId
 */
data class Product(
    val id : Int,
    val title : String,
    val description : String,
    val category: String,
    val price: Double,
    val rating: Double,
    val stock: Int

)