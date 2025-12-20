package com.fp.ui.store

import com.fp.model.ProductPageWrapper

/**
 * Represents the state of the store UI.
 *
 * @property productPageWrapper The wrapper containing the current page of products and pagination details.
 * @property action The last user action performed, used to manage UI behavior (e.g., scrolling).
 */
data class StoreState(
    val productPageWrapper: ProductPageWrapper = ProductPageWrapper(emptyList(), 0, 0, 0),
    val action: ActionEnum = ActionEnum.READ
)