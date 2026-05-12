package com.leelasri.commerceappshopflow.di

import com.leelasri.commerceappshopflow.data.repository.CartRepositoryImpl
import com.leelasri.commerceappshopflow.data.repository.ProductRepositoryImpl
import com.leelasri.commerceappshopflow.domain.repository.CartRepository
import com.leelasri.commerceappshopflow.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository
}