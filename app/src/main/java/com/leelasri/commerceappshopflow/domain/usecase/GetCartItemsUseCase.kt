package com.leelasri.commerceappshopflow.domain.usecase

import com.leelasri.commerceappshopflow.domain.repository.CartRepository
import javax.inject.Inject

class GetCartItemsUseCase @Inject constructor(
    private val repository: CartRepository
) {
    operator fun invoke() = repository.getCartItems()
}