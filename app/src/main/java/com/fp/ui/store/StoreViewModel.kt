package com.fp.ui.store

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fp.model.ProductPage
import com.fp.model.ProductPageWrapper
import com.fp.model.ProductWrapper
import com.fp.network.ProductApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by Your name on 14/09/2025.
 */
class StoreViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(StoreState())
    val uiState: StateFlow<StoreState> = _uiState.asStateFlow()

    private val LOG_TAG = "ProductViewModel"

    fun loadProducts() {
        Log.i(LOG_TAG, "Loading products")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy (action = ActionEnum.IS_LOADING)
            try {
                val response = ProductApi.retrofitService.getProducts()
                if (response.isSuccessful) {
                    val productPage = response.body()
                    val productPageWrapper = getWrapper(productPage!!)
                     _uiState.value = _uiState.value.copy(productPageWrapper = productPageWrapper, action = ActionEnum.READ) 
                    Log.i(LOG_TAG, "Load was Ok")
                } else {
                    _uiState.value = _uiState.value.copy(action = ActionEnum.ERROR)
                    Log.e(LOG_TAG, "Load was NOT Ok")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(action = ActionEnum.ERROR)
                Log.e(LOG_TAG, "Exception: $e")
            }

        }
    }

    fun getWrapper(productPage: ProductPage): ProductPageWrapper {

        val productsWrapperList = productPage.products.mapIndexed { index, product ->
            ProductWrapper(product = product, id = index, expanded = false)
        }
        var productPageWrapper = ProductPageWrapper(
            productsWrapperList,
            productPage.total,
            productPage.skip,
            productPage.limit
        )
        return productPageWrapper
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