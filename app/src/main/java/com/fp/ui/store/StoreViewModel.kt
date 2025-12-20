package com.fp.ui.store

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.fp.data.repository.ProductsRepository
import com.fp.model.ProductPageWrapper

import com.fp.ProductApplication
import com.fp.data.FavoriteProduct
import com.fp.data.repository.FavoriteProductRepository
import com.fp.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by Your name on 14/09/2025.
 */
class StoreViewModel(
    private val productRepository: ProductsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoreState())
    val uiState: StateFlow<StoreState> = _uiState.asStateFlow()

    private val LOG_TAG = "ProductViewModel"

    /**
     * Asynchronously loads product data from the repository.
     *
     * This function initiates a coroutine in the `viewModelScope` to fetch products.
     * On successful retrieval, it updates the UI state (`_uiState`) with the new product data
     * and sets the action to `READ`. If an exception occurs during the fetch operation,
     * it updates the UI state to reflect an `ERROR` action and logs the exception.
     */
    fun loadProducts() {
        Log.i(LOG_TAG, "Loading products")
        viewModelScope.launch {
            try {
                val productPageWrapper : ProductPageWrapper = productRepository.getProducts()
                _uiState.value = _uiState.value.copy(productPageWrapper = productPageWrapper, action = ActionEnum.READ)
                Log.i(LOG_TAG, "Load was Ok")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(action = ActionEnum.ERROR)
                Log.e(LOG_TAG, "Exception: $e")
            }
        }
    }

    /**
     * Updates the UI state to toggle the expanded state of a specific product.
     * When a product is selected, this function finds it in the current list
     * and inverts its `expanded` property, causing the UI to show or hide its details.
     *
     * @param productId The unique identifier of the product to be expanded or collapsed.
     */
    fun onDetailSelected(productId: Int) {
        _uiState.update { currentState ->
            val updatedProductWrapperList = currentState.productPageWrapper.productsWrapper.map { productWrapper ->
                if (productWrapper.id == productId) {
                    productWrapper.copy(expanded = !productWrapper.expanded)
                } else {
                    productWrapper
                }
            }
            currentState.copy(productPageWrapper = currentState.productPageWrapper.copy(productsWrapper = updatedProductWrapperList))
        }
    }

    /**
     * Factory for [StoreViewModel] that takes repository as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ProductApplication)
                val productRepository = application.container.productRepository
                StoreViewModel(productRepository = productRepository)
            }
        }
    }

}