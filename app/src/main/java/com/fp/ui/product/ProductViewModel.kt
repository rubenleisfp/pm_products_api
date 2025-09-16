package com.fp.ui.product

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fp.data.repository.Datasource
import com.fp.network.ProductApi
import com.fp.ui.screens.artist.Artist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Created by Your name on 14/09/2025.
 */
class ProductViewModel : ViewModel() {
    private val _products= MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> get() = _products


    //V2
    private val _uiState = MutableStateFlow(ProductState())
    val uiState: StateFlow<ProductState> = _uiState.asStateFlow()



    private val LOG_TAG = "ProductViewModel"

    fun loadProducts() {
        _products.value = Datasource().loadProducts()
    }

    fun loadProductsV2() {
        Log.i(LOG_TAG, "Loading books")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy (action = ActionEnum.IS_LOADING)
            try {
                val response = ProductApi.retrofitService.getProducts()
                if (response.isSuccessful) {
                    val productList = response.body() ?: emptyList()
                    _uiState.value = _uiState.value.copy(productList, action = ActionEnum.READ)
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

}