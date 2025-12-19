package com.fp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoriteProducto: FavoriteProduct)

    @Update
    suspend fun update(favoriteProducto: FavoriteProduct)

    @Delete
    suspend fun delete(favoriteProducto: FavoriteProduct)

    @Query("SELECT * from items ORDER BY price DESC")
    fun getAll(): Flow<List<FavoriteProduct>>
}