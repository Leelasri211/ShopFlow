package com.leelasri.commerceappshopflow.domain.repository

import com.leelasri.commerceappshopflow.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItems(): Flow<List<CartItem>>
    suspend fun addToCart(item: CartItem)
    suspend fun removeFromCart(productId: Int)
    suspend fun updateQuantity(productId: Int, quantity: Int)
    suspend fun clearCart()
}