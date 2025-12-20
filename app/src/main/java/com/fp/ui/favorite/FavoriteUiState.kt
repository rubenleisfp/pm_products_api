package com.fp.ui.favorite

import com.fp.data.FavoriteProduct

/**
 * Represents the UI state for the favorite products screen.
 *
 * @param favoriteProductList The list of favorite products to be displayed. Defaults to an empty list.
 */
data class FavoriteUiState(
    val favoriteProductList: List<FavoriteProduct> = emptyList(),
)