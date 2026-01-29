package com.fp.ui.favorite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fp.ProductApplication
import com.fp.data.FavoriteProduct
import com.fp.data.repository.FavoriteProductRepository
import com.fp.data.repository.ProductsRepository
import com.fp.model.Product
import com.fp.model.ProductWrapper
import com.fp.ui.store.ActionEnum
import com.fp.ui.store.StoreViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Created by Your name on 19/12/2025.
 */
class FavoriteViewModel(
    private val favoriteProductRepository: FavoriteProductRepository
) : ViewModel() {

    private val LOG_TAG = "ProductViewModel"

    val uiState: StateFlow<FavoriteUiState> =
        favoriteProductRepository.getAllFavoritesStream().filterNotNull()
            .map {
                FavoriteUiState(it)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = FavoriteUiState()
            )

    /**
     * Adds a product to the user's favorites.
     *
     * This function converts a [Product] object into a [FavoriteProduct] and then
     * inserts it into the favorite product repository. The operation is performed
     * asynchronously within a `viewModelScope` coroutine. Any exceptions during
     * the database insertion are caught and logged.
     *
     * @param product The [Product] to be added to favorites.
     */
    fun addFavoriteProduct(product: Product) {
        Log.i(LOG_TAG, "Add Favorite Product")
        viewModelScope.launch {
            try {
                val favoriteProduct : FavoriteProduct = convertToFavoriteProduct(product)
                favoriteProductRepository.insertFavorite(favoriteProduct)

                Log.i(LOG_TAG, "Add favorite $favoriteProduct was Ok")
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Exception: $e")
            }
        }
    }

    /**
     * Deletes a product from the user's favorites.
     *
     * This function takes a [FavoriteProduct] object and requests its deletion from
     * the favorite product repository. The operation is performed asynchronously
     * within a `viewModelScope` coroutine. Any exceptions during the database
     * deletion are caught and logged.
     *
     * @param favoriteProduct The [FavoriteProduct] to be removed from favorites.
     */
    fun deleteFavoriteProduct(favoriteProduct:FavoriteProduct) {
        Log.i(LOG_TAG, "Deleted Favorite Product")
        viewModelScope.launch {
            try {
                favoriteProductRepository.deleteFavorite(favoriteProduct)
                Log.i(LOG_TAG, "Deleted favorite $favoriteProduct was Ok")
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Exception: $e")
            }
        }
    }

    fun deleteFavoriteProductById(id:Int) {
        Log.i(LOG_TAG, "Deleted Favorite Product By Id")
        viewModelScope.launch {
            try {
                favoriteProductRepository.deleteFavoriteById(id)
                Log.i(LOG_TAG, "Deleted favorite by id $id was Ok")
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Exception: $e")
            }
        }
    }

    private fun convertToFavoriteProduct(product: Product): FavoriteProduct {
        return FavoriteProduct(
            id = product.id,
            name = product.title,
            price = product.price
        )
    }


    /**
     * Factory for [StoreViewModel] that takes repository as a dependency
     */
    companion object {

        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ProductApplication)

                val favoriteProductRepository = application.container.favoriteProductRepository
                FavoriteViewModel(favoriteProductRepository = favoriteProductRepository)
            }
        }
    }

}