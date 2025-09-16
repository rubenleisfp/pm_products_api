package com.fp.data.repository

import com.fp.R
import com.fp.ui.store.Product

/**
 * [Datasource] generates a list of [Affirmation]
 */


//https://dummyjson.com/products?limit=30&skip=0&select=title,description,category,price,rating,stock,thumbnail
class Datasource() {

    fun loadProducts(): List<Product> {
        return listOf<Product>(
            Product(1, "Boli", "Boli Bic sensancional", "Papeleria", 3.12, 3.0, 20,""),
            Product(2, "Lapiz", "Lapiz increible", "Papeleria", 2.0, 4.2, 10,""))
    }

}