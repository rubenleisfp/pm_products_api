package com.fp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Your name on 19/12/2025.
 */
@Entity(tableName = "items")
data class FavoriteProduct (
    @PrimaryKey
    val id: Int,
    val name: String,
    val price: Double)
