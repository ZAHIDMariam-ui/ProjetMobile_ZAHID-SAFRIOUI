package com.example.projetappmobile.ui.navigation

// Définition des routes de navigation
sealed class Route(val route: String) {
    // Écrans sans BottomBar
    object Welcome : Route("welcome")
    object Login : Route("login")
    object Signup : Route("signup")
    object ForgotPassword : Route("forgot_password")
    object ResetPassword : Route("reset_password/{token}") {
        fun createRoute(token: String) = "reset_password/$token"
    }

    // Écrans avec BottomBar
    object Home : Route("home")
    object ProductList : Route("product_list")
    object ProductDetail : Route("product_detail/{productId}") {
        fun createRoute(productId: Long): String {
            return "product_detail/$productId"
        }
    }
    object Cart : Route("cart")
    object Profile : Route("profile")

    object Checkout : Route("checkout")
    object OrderSuccess : Route("order_success")
    object OrderFailed : Route("order_failed")
    object MyOrders : Route("my_orders")

    companion object {
        // Liste des routes qui doivent afficher la BottomBar
        val routesWithBottomBar = listOf(
            Home.route,
            ProductList.route,
            Cart.route,
            Profile.route
        )

        // Vérifie si une route doit afficher la BottomBar
        fun shouldShowBottomBar(route: String?): Boolean {
            if (route == null) return false
            // Pour ProductDetail, on vérifie si ça commence par "product_detail/"
            return routesWithBottomBar.any { route == it } || route.startsWith("product_detail/")
        }
    }
}