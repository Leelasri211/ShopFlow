package com.leelasri.commerceappshopflow.domain.usecase

import com.leelasri.commerceappshopflow.domain.repository.CartRepository
import javax.inject.Inject

class RemoveFromCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(productId: Int) = repository.removeFromCart(productId)
}