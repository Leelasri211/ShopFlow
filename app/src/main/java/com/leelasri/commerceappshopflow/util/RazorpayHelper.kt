package com.leelasri.commerceappshopflow.util

import android.app.Activity
import com.razorpay.Checkout
import org.json.JSONObject

object RazorpayHelper {

    // Replace with your Razorpay test key from https://dashboard.razorpay.com
    private const val RAZORPAY_KEY = "YOUR_RAZORPAY_TEST_KEY"

    fun startPayment(activity: Activity, amountInRupees: Double, orderId: String) {
        Checkout.preload(activity.applicationContext)
        val checkout = Checkout()
        checkout.setKeyID(RAZORPAY_KEY)

        val options = JSONObject().apply {
            put("name", "ShopFlow")
            put("description", "Order #$orderId")
            put("currency", "INR")
            put("amount", (amountInRupees * 100).toInt()) // paise
            put("prefill", JSONObject().apply {
                put("email", "test@shopflow.in")
                put("contact", "9999999999")
            })
            put("theme", JSONObject().apply {
                put("color", "#6750A4")
            })
        }

        try {
            checkout.open(activity, options)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}