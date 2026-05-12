package com.leelasri.commerceappshopflow.domain.model

data class CartItem(
    val productId: Int,
    val title: String,
    val price: Double,
    val image: String,
    val quantity: Int
) {
    val totalPrice: Double get() = price * quantity
}