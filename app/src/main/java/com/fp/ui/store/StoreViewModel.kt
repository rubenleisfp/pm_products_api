package com.fp.ui.store


import androidx.lifecycle.ViewModel
import com.fp.data.repository.Datasource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


/**
 * Created by Your name on 14/09/2025.
 */
class StoreViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(StoreState(emptyList()))
    val uiState: StateFlow<StoreState> = _uiState.asStateFlow()

    private val LOG_TAG = "ProductViewModel"

    fun loadProducts() {
        val productsWrapperList = Datasource().loadProductsWrapper()
        _uiState.value.productWrapperList = productsWrapperList
    }

    fun onDetailSelected(productId: Int) {
        _uiState.update { currentState ->
            val updatedProductWrapperList = currentState.productWrapperList.map { productWrapper ->
                if (productWrapper.id == productId) {
                    productWrapper.copy(expanded = !productWrapper.expanded)
                } else {
                    productWrapper
                }
            }
            currentState.copy(productWrapperList = updatedProductWrapperList)
        }
    }
}