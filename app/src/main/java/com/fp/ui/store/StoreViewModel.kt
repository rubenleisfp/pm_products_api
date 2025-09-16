package com.fp.ui.store

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fp.network.ProductApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
                    productPage?.let { _uiState.value = _uiState.value.copy(productPage = it, action = ActionEnum.READ) }
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