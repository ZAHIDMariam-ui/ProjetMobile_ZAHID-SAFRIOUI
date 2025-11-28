package com.example.projetappmobile.ui.screens.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projetappmobile.ui.cart.CartItem
import com.example.projetappmobile.ui.cart.CartViewModel
import com.example.projetappmobile.ui.navigation.Route
import com.example.projetappmobile.ui.screens.home.RajaGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val total by cartViewModel.totalPrice.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Cart", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Route.ProductList.route) {
                            popUpTo(Route.ProductList.route) { inclusive = true }
                        }
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour à la boutique",
                            tint = RajaGreen
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Route.Home.route) {
                            popUpTo(Route.Home.route) { inclusive = true }
                        }
                    }) {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = "Accueil",
                            tint = RajaGreen
                        )
                    }
                }
            )
        }
    ) { padding ->

        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Votre panier est vide", fontSize = 18.sp, color = Color.Gray)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Liste des produits
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemRow(
                            cartItem = item,
                            onIncrease = { cartViewModel.updateQuantity(item, item.quantity + 1) },
                            onDecrease = {
                                if (item.quantity > 1) {
                                    cartViewModel.updateQuantity(item, item.quantity - 1)
                                } else {
                                    cartViewModel.removeItem(item)
                                }
                            },
                            onSizeChange = { newSize ->
                                cartViewModel.updateSize(item, newSize)
                            }
                        )
                    }
                }

                // Section Total et Bouton
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Divider()

                    Spacer(Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total:", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "$total.00 MAD",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = RajaGreen
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    // BOUTON CORRIGÉ : OrderSuccess au lieu de Checkout
                    Button(
                        onClick = {
                            // Navigation vers OrderSuccess et vidage du panier
                            cartViewModel.clearCart()
                            navController.navigate(Route.OrderSuccess.route) {
                                popUpTo(Route.OrderSuccess.route) { inclusive = true }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RajaGreen)
                    ) {
                        Text("Confirmer la commande", color = Color.White, fontSize = 18.sp)
                    }

                    Spacer(Modifier.height(30.dp))
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    cartItem: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onSizeChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = cartItem.product.imageRes),
                contentDescription = cartItem.product.name,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F5F5))
            )

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    cartItem.product.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(8.dp))

                SizeSelectorMini(
                    selectedSize = cartItem.selectedSize,
                    onSizeSelected = onSizeChange
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    "${cartItem.product.price} MAD",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = RajaGreen
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = onIncrease,
                    modifier = Modifier
                        .size(22.dp)
                        .background(RajaGreen, CircleShape)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Augmenter",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }

                Text(
                    "${cartItem.quantity}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                IconButton(
                    onClick = onDecrease,
                    modifier = Modifier
                        .size(22.dp)
                        .background(Color(0xFFF0F0F0), CircleShape)
                ) {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = "Diminuer",
                        tint = Color.Gray,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SizeSelectorMini(
    selectedSize: String,
    onSizeSelected: (String) -> Unit
) {
    Column {
        Text(
            "Taille:",
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(4.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("S", "M", "L", "XL").forEach { size ->
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (selectedSize == size) RajaGreen else Color(0xFFF0F0F0)
                        )
                        .clickable { onSizeSelected(size) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        size,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedSize == size) Color.White else Color.Gray
                    )
                }
            }
        }
    }
}