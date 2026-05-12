package com.leelasri.commerceappshopflow.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leelasri.commerceappshopflow.domain.usecase.GetCartItemsUseCase
import com.leelasri.commerceappshopflow.domain.usecase.RemoveFromCartUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase
) : ViewModel() {

    companion object {
        val paymentSuccess = MutableSharedFlow<String>(extraBufferCapacity = 1)
    }

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    val cartItems = getCartItemsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalPrice get() = cartItems.value.sumOf { it.totalPrice }

    private val _orderState = MutableStateFlow<OrderState>(OrderState.Idle)
    val orderState = _orderState.asStateFlow()
    init {
        viewModelScope.launch {
            paymentSuccess.collect { paymentId ->
                saveOrderToFirestore(paymentId)
            }
        }
    }
    fun saveOrderToFirestore(paymentId: String) {
        val userId = auth.currentUser?.uid ?: return
        val order = hashMapOf(
            "userId" to userId,
            "paymentId" to paymentId,
            "items" to cartItems.value.map {
                mapOf("title" to it.title, "price" to it.price, "quantity" to it.quantity)
            },
            "total" to totalPrice,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("orders").add(order)
            .addOnSuccessListener {
                viewModelScope.launch {
                    cartItems.value.forEach { removeFromCartUseCase(it.productId) }
                    _orderState.value = OrderState.Success
                }
            }
            .addOnFailureListener { _orderState.value = OrderState.Error(it.message ?: "Failed") }
    }
}

sealed class OrderState {
    object Idle : OrderState()
    object Success : OrderState()
    data class Error(val message: String) : OrderState()
}