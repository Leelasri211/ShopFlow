package com.leelasri.commerceappshopflow.domain.repository

import com.leelasri.commerceappshopflow.domain.model.Product
import com.leelasri.commerceappshopflow.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<Resource<List<Product>>>
    fun getProductById(id: Int): Flow<Resource<Product>>
}