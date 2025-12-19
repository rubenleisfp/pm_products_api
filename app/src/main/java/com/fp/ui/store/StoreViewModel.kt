package com.fp.ui.store

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by Your name on 14/09/2025.
 */
class StoreViewModel(private val productRepository: ProductsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(StoreState())
    val uiState: StateFlow<StoreState> = _uiState.asStateFlow()

    private val LOG_TAG = "ProductViewModel"

    fun loadProducts() {
        Log.i(LOG_TAG, "Loading products")
        viewModelScope.launch {
            //_uiState.value = _uiState.value.copy (action = ActionEnum.IS_LOADING)
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
     * Factory for [MarsViewModel] that takes [MarsPhotosRepository] as a dependency
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

}