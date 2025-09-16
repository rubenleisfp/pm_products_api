package com.fp.ui.product

/**
 * Created by Your name on 16/09/2025.
 */
data class ProductState(
    val productPage: ProductPage = ProductPage(emptyList(), 0, 0, 0),
    val action: ActionEnum = ActionEnum.READ
)