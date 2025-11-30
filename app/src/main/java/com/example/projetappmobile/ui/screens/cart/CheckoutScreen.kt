package com.example.projetappmobile.ui.screens.cart

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.projetappmobile.ui.navigation.Route

@Composable
fun CheckoutScreen(navController: NavController) {
    Button(onClick = {
        navController.navigate(Route.OrderSuccess)
    }) {
        Text("Pay Now")
    }
}