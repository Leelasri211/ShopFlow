package com.leelasri.commerceappshopflow

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.leelasri.commerceappshopflow.ui.checkout.CheckoutViewModel
import com.leelasri.commerceappshopflow.ui.navigation.NavGraph
import com.leelasri.commerceappshopflow.ui.theme.CommerceAppShopFlowTheme
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity(),PaymentResultListener  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CommerceAppShopFlowTheme {
                NavGraph()
            }
        }
    }
    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        razorpayPaymentId?.let {
            CheckoutViewModel.paymentSuccess.tryEmit(it)
            Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPaymentError(errorCode: Int, errorDescription: String?) {
        Toast.makeText(this, "Payment Failed: $errorDescription", Toast.LENGTH_SHORT).show()
    }
}