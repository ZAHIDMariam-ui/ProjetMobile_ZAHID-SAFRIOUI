package com.example.projetappmobile.ui.screens.home

import androidx.compose.ui.graphics.Color
import com.example.projetappmobile.R

val RajaGreen = Color(0xFF006633)
val SearchBarTextColor = Color(0xFF1A9C6D)

// LA CLASSE PRODUCT
data class Product(
    val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    val originalPrice: Double? = null, // Pour les promotions
    val imageRes: Int,
    val category: String,
    val isPromotion: Boolean = false,
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val inStock: Boolean = true
)

// LA SEULE LISTE DE PRODUITS
val sampleProducts = listOf(
    Product(
        id = 1L,
        name = "Raja T-shirt 2025",
        description = "T-shirt officiel Raja 2025 – Édition limitée, coton premium très confortable.",
        price = 200.0,
        originalPrice = 250.0,
        imageRes = R.drawable.tshirt_2025,
        category = "T-shirts",
        isPromotion = true
    ),
    Product(
        id = 2L,
        name = "Raja Scarf",
        description = "Écharpe officielle aux couleurs du Raja Club Athletic.",
        price = 150.0,
        imageRes = R.drawable.scarf,
        category = "Accessories"
    ),
    Product(
        id = 3L,
        name = "Raja Cap",
        description = "Casquette ajustable avec logo brodé.",
        price = 100.0,
        imageRes = R.drawable.cap,
        category = "Accessories"
    ),
    Product(
        id = 4L,
        name = "Raja Jersey 2025",
        description = "Maillot extérieur officiel saison 2025.",
        price = 300.0,
        imageRes = R.drawable.jersey,
        category = "Jerseys"
    ),
    Product(
        id = 5L,
        name = "Raja Jersey Home 2025",
        description = "Maillot domicile officiel 2025 – Promotion spéciale !",
        price = 350.0,
        originalPrice = 400.0,
        imageRes = R.drawable.jersey_home,
        category = "Jerseys",
        isPromotion = true
    ),
    Product(
        id = 6L,
        name = "Sac Raja Vintage",
        description = "Sac rétro style vintage aux couleurs du Raja.",
        price = 180.0,
        imageRes = R.drawable.sac,
        category = "Bags"
    )
)
