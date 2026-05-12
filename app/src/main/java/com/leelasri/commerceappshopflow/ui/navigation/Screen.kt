package com.leelasri.commerceappshopflow.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object ProductList : Screen("product_list")
    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: Int) = "product_detail/$productId"
    }
    object Cart : Screen("cart")
    object Checkout : Screen("checkout")
    object OrderHistory : Screen("order_history")
}