package com.example.projetappmobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.projetappmobile.ui.cart.CartViewModel
import com.example.projetappmobile.ui.screens.auth.LoginScreen
import com.example.projetappmobile.ui.screens.auth.SignUpScreen
import com.example.projetappmobile.ui.screens.cart.CartScreen
import com.example.projetappmobile.ui.screens.cart.CheckoutScreen
import com.example.projetappmobile.ui.screens.home.HomeScreen
import com.example.projetappmobile.ui.screens.home.ProductDetailScreen
import com.example.projetappmobile.ui.screens.home.ProductListScreen
import com.example.projetappmobile.ui.screens.order.OrderFailedScreen
import com.example.projetappmobile.ui.screens.order.OrderSuccessScreen
import com.example.projetappmobile.ui.screens.profile.MyOrdersScreen
import com.example.projetappmobile.ui.screens.profile.ProfileScreen
import com.example.projetappmobile.ui.screens.welcome.WelcomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // 🔥 Le ViewModel partagé ici !
    val cartViewModel: CartViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    NavHost(
        navController = navController,
        startDestination = Route.Welcome.route,
        modifier = modifier
    ) {
        composable(route = Route.Welcome.route) {
            WelcomeScreen(navController = navController)
        }

        composable(route = Route.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(route = Route.Signup.route) {
            SignUpScreen(navController = navController)
        }

        composable(route = Route.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(route = Route.ProductList.route) {
            ProductListScreen(navController = navController)
        }

        composable(
            route = Route.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            ProductDetailScreen(
                productId = productId,
                navController = navController,
                cartViewModel = cartViewModel   // 🔥 ViewModel partagé
            )
        }

        composable(route = Route.Cart.route) {
            CartScreen(
                navController = navController,
                cartViewModel = cartViewModel   // 🔥 ViewModel partagé
            )
        }

        composable(route = Route.Profile.route) {
            ProfileScreen(navController = navController)
        }

        composable(route = Route.Checkout.route) {
            CheckoutScreen(navController = navController)
        }

        composable(Route.MyOrders.route) {
            MyOrdersScreen(navController)
        }

        composable(Route.OrderSuccess.route) {
            OrderSuccessScreen(navController)
        }

        composable(Route.OrderFailed.route) {
            OrderFailedScreen(navController)
        }
    }
}
