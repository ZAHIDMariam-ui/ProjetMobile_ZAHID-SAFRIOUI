package com.example.projetappmobile.ui.screens.home

import androidx.compose.ui.graphics.Color
import com.example.projetappmobile.R

val RajaGreen = Color(0xFF006633)
val SearchBarTextColor = Color(0xFF1A9C6D)

// LA CLASSE PRODUCT
data class Product(
    val id: Int,
    val name: String,
    val price: Int,
    val isPromotion: Boolean,
    val imageRes: Int,
    val description: String
)

// LA SEULE LISTE DE PRODUITS
val sampleProducts = listOf(
    Product(1, "Raja T-shirt 2025", 200, true, R.drawable.tshirt_2025,
        "T-shirt officiel Raja 2025 – Édition limitée, coton premium très confortable."),
    Product(2, "Raja Scarf", 150, false, R.drawable.scarf,
        "Écharpe officielle aux couleurs du Raja Club Athletic."),
    Product(3, "Raja Cap", 100, false, R.drawable.cap,
        "Casquette ajustable avec logo brodé."),
    Product(4, "Raja Jersey 2025", 300, false, R.drawable.jersey,
        "Maillot extérieur officiel saison 2025."),
    Product(5, "Raja Jersey Home 2025", 350, true, R.drawable.jersey_home,
        "Maillot domicile officiel 2025 – Promotion spéciale !"),
    Product(6, "Sac Raja Vintage", 180, false, R.drawable.sac,
        "Sac rétro style vintage aux couleurs du Raja.")
)