package com.fp.ui.product

/**
 * Created by Your name on 16/09/2025.
 */
data class ProductState(
    val products : List<Product> = emptyList(),
    val action: ActionEnum = ActionEnum.READ
)