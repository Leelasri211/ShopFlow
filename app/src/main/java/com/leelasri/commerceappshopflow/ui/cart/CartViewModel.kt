package com.leelasri.commerceappshopflow.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leelasri.commerceappshopflow.domain.model.CartItem
import com.leelasri.commerceappshopflow.domain.usecase.GetCartItemsUseCase
import com.leelasri.commerceappshopflow.domain.usecase.RemoveFromCartUseCase
import com.leelasri.commerceappshopflow.domain.usecase.UpdateCartQuantityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase
) : ViewModel() {

    val cartItems = getCartItemsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalPrice get() = cartItems.value.sumOf { it.totalPrice }

    fun removeItem(productId: Int) {
        viewModelScope.launch { removeFromCartUseCase(productId) }
    }

    fun increaseQuantity(item: CartItem) {
        viewModelScope.launch { updateCartQuantityUseCase(item.productId, item.quantity + 1) }
    }

    fun decreaseQuantity(item: CartItem) {
        viewModelScope.launch { updateCartQuantityUseCase(item.productId, item.quantity - 1) }
    }
}