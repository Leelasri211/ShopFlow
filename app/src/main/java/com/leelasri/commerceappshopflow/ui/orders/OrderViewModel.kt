package com.leelasri.commerceappshopflow.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Order(
    val id: String,
    val paymentId: String,
    val total: Double,
    val timestamp: Long,
    val items: List<Map<String, Any>>
)

@HiltViewModel
class OrderViewModel @Inject constructor() : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init { loadOrders() }

    fun loadOrders() {
        val userId = auth.currentUser?.uid ?: return
        _isLoading.value = true
        db.collection("orders")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                _orders.value = snapshot.documents.mapNotNull { doc ->
                    Order(
                        id = doc.id,
                        paymentId = doc.getString("paymentId") ?: "",
                        total = doc.getDouble("total") ?: 0.0,
                        timestamp = doc.getLong("timestamp") ?: 0L,
                        items = (doc.get("items") as? List<Map<String, Any>>) ?: emptyList()
                    )
                }
                _isLoading.value = false
            }
            .addOnFailureListener { _isLoading.value = false }
    }
}