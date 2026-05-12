package com.leelasri.commerceappshopflow.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.leelasri.commerceappshopflow.ui.auth.LoginScreen
import com.leelasri.commerceappshopflow.ui.cart.CartScreen
import com.leelasri.commerceappshopflow.ui.checkout.CheckoutScreen
import com.leelasri.commerceappshopflow.ui.orders.OrderHistoryScreen
import com.leelasri.commerceappshopflow.ui.products.ProductDetailScreen
import com.leelasri.commerceappshopflow.ui.products.ProductListScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {

        composable(Screen.Login.route) {
            LoginScreen(onLoginSuccess = {
                navController.navigate(Screen.ProductList.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            })
        }

        composable(Screen.ProductList.route) {
            ProductListScreen(
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onCartClick = { navController.navigate(Screen.Cart.route) },
                onOrderHistoryClick = { navController.navigate(Screen.OrderHistory.route) },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }

            )
        }

        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: return@composable
            ProductDetailScreen(
                productId = productId,
                onBack = { navController.popBackStack() },
                onCartClick = { navController.navigate(Screen.Cart.route) }
            )
        }

        composable(Screen.Cart.route) {
            CartScreen(
                onBack = { navController.popBackStack() },
                onCheckout = { navController.navigate(Screen.Checkout.route) }
            )
        }

        composable(Screen.Checkout.route) {
            CheckoutScreen(
                onBack = { navController.popBackStack() },
                onOrderPlaced = {
                    navController.navigate(Screen.OrderHistory.route) {
                        popUpTo(Screen.ProductList.route)
                    }
                }
            )
        }

        composable(Screen.OrderHistory.route) {
            OrderHistoryScreen(onBack = { navController.popBackStack() })
        }
    }
}