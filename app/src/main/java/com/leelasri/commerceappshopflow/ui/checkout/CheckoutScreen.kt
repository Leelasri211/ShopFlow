package com.leelasri.commerceappshopflow.ui.checkout

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leelasri.commerceappshopflow.util.RazorpayHelper
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onBack: () -> Unit,
    onOrderPlaced: () -> Unit,
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val cartItems by viewModel.cartItems.collectAsState()
    val orderState by viewModel.orderState.collectAsState()
    val totalPrice by remember { derivedStateOf { cartItems.sumOf { it.totalPrice } } }

    LaunchedEffect(orderState) {
        if (orderState is OrderState.Success) onOrderPlaced()
    }

    // Make Activity implement PaymentResultWithDataListener
    val activity = context as? Activity

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total:", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "₹${String.format("%.2f", totalPrice * 83)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            activity?.let {
                                RazorpayHelper.startPayment(
                                    activity = it,
                                    amountInRupees = totalPrice * 83,
                                    orderId = System.currentTimeMillis().toString()
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        enabled = cartItems.isNotEmpty()
                    ) {
                        Text("Pay with Razorpay")
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding), contentPadding = PaddingValues(16.dp)) {
            item {
                Text("Order Summary", style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
            }
            items(cartItems) { item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${item.title.take(25)}... x${item.quantity}",
                        style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                    Text("₹${String.format("%.2f", item.totalPrice * 83)}",
                        style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}