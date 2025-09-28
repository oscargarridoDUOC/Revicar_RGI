package com.example.revicar_rgi.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        // Item de Inicio
        NavigationBarItem(
            selected = currentRoute == AppRoutes.BUYER_HOME_SCREEN,
            onClick = {
                navController.navigate(AppRoutes.BUYER_HOME_SCREEN) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") }
        )

        // Item de Inspecciones
        NavigationBarItem(
            selected = currentRoute == AppRoutes.INSPECTIONS_SCREEN,
            onClick = {
                navController.navigate(AppRoutes.INSPECTIONS_SCREEN) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.Build, contentDescription = "Inspecciones") },
            label = { Text("Mis inspecciones") }
        )
    }
}