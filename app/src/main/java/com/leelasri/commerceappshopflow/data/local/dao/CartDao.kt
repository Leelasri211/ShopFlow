package com.leelasri.commerceappshopflow.data.local.dao

import androidx.room.*
import com.leelasri.commerceappshopflow.data.local.entity.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartEntity)

    @Delete
    suspend fun deleteCartItem(item: CartEntity)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    @Query("SELECT * FROM cart_items WHERE productId = :id")
    suspend fun getCartItemById(id: Int): CartEntity?

    @Query("UPDATE cart_items SET quantity = :quantity WHERE productId = :id")
    suspend fun updateQuantity(id: Int, quantity: Int)
}