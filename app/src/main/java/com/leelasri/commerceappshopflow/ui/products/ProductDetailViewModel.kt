package com.leelasri.commerceappshopflow.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leelasri.commerceappshopflow.domain.model.CartItem
import com.leelasri.commerceappshopflow.domain.model.Product
import com.leelasri.commerceappshopflow.domain.usecase.AddToCartUseCase
import com.leelasri.commerceappshopflow.domain.usecase.GetProductByIdUseCase
import com.leelasri.commerceappshopflow.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<Product>>(Resource.Loading)
    val uiState = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    fun loadProduct(id: Int) {
        viewModelScope.launch {
            getProductByIdUseCase(id).collect { _uiState.value = it }
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            addToCartUseCase(
                CartItem(product.id, product.title, product.price, product.image, 1)
            )
            _snackbarMessage.emit("Added to cart!")
        }
    }
}