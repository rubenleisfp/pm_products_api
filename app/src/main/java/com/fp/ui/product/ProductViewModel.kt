package com.fp.ui.product

import androidx.lifecycle.ViewModel
import com.fp.data.repository.Datasource
import com.fp.ui.screens.artist.Artist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Your name on 14/09/2025.
 */
class ProductViewModel : ViewModel() {
    private val _products= MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> get() = _products

    fun loadProducts() {
        _products.value = Datasource().loadProducts()
    }
}