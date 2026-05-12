package com.leelasri.commerceappshopflow.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey val productId: Int,
    val title: String,
    val price: Double,
    val image: String,
    val quantity: Int
)