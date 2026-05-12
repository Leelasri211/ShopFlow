package com.leelasri.commerceappshopflow.domain.usecase

import com.leelasri.commerceappshopflow.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke() = repository.getProducts()
}