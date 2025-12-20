package com.fp.data.repository

/**
 * Created by Your name on 19/12/2025.
 */

import com.fp.data.FavoriteProduct
import com.fp.data.FavoriteProductDao
import kotlinx.coroutines.flow.Flow


/**
 * Repository that provides insert and retrieval operations for favorite products.
 */
interface FavoriteProductRepository {

    /**
     * Retrieve all favorite products from the given data source as a Flow.
     * The Flow will emit a new list of products whenever the underlying data changes.
     *
     * @return A Flow emitting a list of all [FavoriteProduct] items.
     */
    fun getAllFavoritesStream(): Flow<List<FavoriteProduct>>

    /**
     * Inserts a new favorite product into the data source.
     * This is a suspend function, designed to be called from a coroutine scope.
     *
     * @param favoriteProduct The [FavoriteProduct] to be inserted.
     */
    suspend fun insertFavorite(favoriteProduct: FavoriteProduct)

    /**
     * Deletes a favorite product from the data source.
     * This is a suspend function, designed to be called from a coroutine scope.
     *
     * @param favoriteProduct The [FavoriteProduct] to be deleted.
     */
    suspend fun deleteFavorite(favoriteProduct: FavoriteProduct)


}

class DatabaseFavoriteProductRepository(private val favoriteProductDao: FavoriteProductDao) : FavoriteProductRepository {
    override fun getAllFavoritesStream(): Flow<List<FavoriteProduct>> {
        return favoriteProductDao.getAll()
    }

    override suspend fun insertFavorite(favoriteProduct: FavoriteProduct) {
        favoriteProductDao.insert(favoriteProduct)
    }

    override suspend fun deleteFavorite(favoriteProduct: FavoriteProduct) {
        favoriteProductDao.delete(favoriteProduct)
    }
}