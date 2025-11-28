package com.example.projetappmobile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.projetappmobile.ui.components.BottomBar
import com.example.projetappmobile.ui.navigation.AppNavHost
import com.example.projetappmobile.ui.navigation.Route

@Composable
fun RajaClubApp() {
    val navController = rememberNavController()

    // Observer la route actuelle
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    // Déterminer si on doit afficher la BottomBar
    val shouldShowBottomBar = Route.shouldShowBottomBar(currentRoute)

    Box(modifier = Modifier.fillMaxSize()) {
        // Contenu principal avec navigation
        Scaffold(
            bottomBar = { }, // On laisse vide, la BottomBar sera par-dessus
            content = { padding ->
                AppNavHost(
                    navController = navController,
                    modifier = Modifier.padding(
                        // Ajouter padding en bas SEULEMENT si la BottomBar est visible
                        bottom = if (shouldShowBottomBar) padding.calculateBottomPadding() else 0.dp
                    )
                )
            }
        )

        // BottomBar conditionnelle (affichée par-dessus)
        if (shouldShowBottomBar) {
            BottomBar(
                navController = navController,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}