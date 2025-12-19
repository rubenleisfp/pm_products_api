package com.fp.ui.favorite

import com.fp.data.FavoriteProduct

/**
 * Created by Your name on 16/09/2025.
 */
data class FavoriteUiState(
    val favoriteProductList: List<FavoriteProduct> = emptyList(),
)