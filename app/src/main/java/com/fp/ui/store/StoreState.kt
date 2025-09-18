package com.fp.ui.store

/**
 * Created by Your name on 16/09/2025.
 */
data class StoreState(
    val productPageWrapper: ProductPageWrapper = ProductPageWrapper(emptyList(), 0, 0, 0),
    val action: ActionEnum = ActionEnum.READ
)