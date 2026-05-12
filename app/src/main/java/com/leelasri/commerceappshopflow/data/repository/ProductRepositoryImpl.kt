package com.leelasri.commerceappshopflow.data.repository

import com.leelasri.commerceappshopflow.data.remote.ApiService
import com.leelasri.commerceappshopflow.domain.model.Product
import com.leelasri.commerceappshopflow.domain.repository.ProductRepository
import com.leelasri.commerceappshopflow.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ProductRepository {

    override fun getProducts(): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading)
        try {
            val products = apiService.getProducts().map { dto ->
                Product(
                    id = dto.id,
                    title = dto.title,
                    price = dto.price,
                    description = dto.description,
                    category = dto.category,
                    image = dto.image,
                    rating = dto.rating.rate,
                    ratingCount = dto.rating.count
                )
            }
            emit(Resource.Success(products))
        } catch (e: Exception) {
            val message = if (e.message?.contains("504") == true) {
                "Please check your network and retry."
            } else {
                e.message ?: "Unknown error"
            }
            emit(Resource.Error(message))
        }
    }

    override fun getProductById(id: Int): Flow<Resource<Product>> = flow {
        emit(Resource.Loading)
        try {
            val dto = apiService.getProductById(id)
            emit(Resource.Success(
                Product(
                    id = dto.id,
                    title = dto.title,
                    price = dto.price,
                    description = dto.description,
                    category = dto.category,
                    image = dto.image,
                    rating = dto.rating.rate,
                    ratingCount = dto.rating.count
                )
            ))
        } catch (e: Exception) {
            val message = if (e.message?.contains("504") == true) {
                "Please check your network and retry."
            } else {
                e.message ?: "Unknown error"
            }
            emit(Resource.Error(message))
        }
    }
}