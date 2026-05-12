package com.leelasri.commerceappshopflow.data.repository

import com.leelasri.commerceappshopflow.data.local.dao.CartDao
import com.leelasri.commerceappshopflow.data.local.entity.CartEntity
import com.leelasri.commerceappshopflow.domain.model.CartItem
import com.leelasri.commerceappshopflow.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : CartRepository {

    override fun getCartItems(): Flow<List<CartItem>> =
        cartDao.getCartItems().map { entities ->
            entities.map { entity ->
                CartItem(
                    productId = entity.productId,
                    title = entity.title,
                    price = entity.price,
                    image = entity.image,
                    quantity = entity.quantity
                )
            }
        }

    override suspend fun addToCart(item: CartItem) {
        val existing = cartDao.getCartItemById(item.productId)
        if (existing != null) {
            cartDao.updateQuantity(item.productId, existing.quantity + 1)
        } else {
            cartDao.insertCartItem(
                CartEntity(
                    productId = item.productId,
                    title = item.title,
                    price = item.price,
                    image = item.image,
                    quantity = item.quantity
                )
            )
        }
    }

    override suspend fun removeFromCart(productId: Int) {
        cartDao.getCartItemById(productId)?.let { cartDao.deleteCartItem(it) }
    }

    override suspend fun updateQuantity(productId: Int, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(productId)
        } else {
            cartDao.updateQuantity(productId, quantity)
        }
    }

    override suspend fun clearCart() = cartDao.clearCart()
}