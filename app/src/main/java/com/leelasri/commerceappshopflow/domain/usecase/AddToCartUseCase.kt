package com.leelasri.commerceappshopflow.domain.usecase

import com.leelasri.commerceappshopflow.domain.model.CartItem
import com.leelasri.commerceappshopflow.domain.repository.CartRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(item: CartItem) = repository.addToCart(item)
}