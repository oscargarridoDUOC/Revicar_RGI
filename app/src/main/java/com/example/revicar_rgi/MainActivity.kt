package com.example.revicar_rgi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.revicar_rgi.navigation.AppRoutes
import com.example.revicar_rgi.navigation.BottomBar
import com.example.revicar_rgi.ui.screens.buyer.BuyerHomeScreen
import com.example.revicar_rgi.ui.screens.buyer.InspectionsScreen
import com.example.revicar_rgi.ui.screens.common.NotificationsScreen
import com.example.revicar_rgi.ui.theme.Revicar_RGITheme
import com.example.revicar_rgi.ui.viewmodel.InspectionsViewModel
import com.example.revicar_rgi.ui.viewmodel.NotificationsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Revicar_RGITheme {
                App()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(
                            text = "ReviCar",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Hola, Bienvenido",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.NOTIFICATIONS_SCREEN) }) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Notificaciones"
                        )
                    }
                    IconButton(onClick = { /* Lógica para navegar a Login/Perfil */ }) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Iniciar Sesión"
                        )
                    }
                    Spacer(modifier = Modifier.padding(end = 8.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = { BottomBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppRoutes.BUYER_HOME_SCREEN,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppRoutes.BUYER_HOME_SCREEN) {
                BuyerHomeScreen(navController = navController)
            }

            composable(AppRoutes.INSPECTIONS_SCREEN) {
                val viewModel: InspectionsViewModel = viewModel()
                InspectionsScreen(viewModel = viewModel)
            }

            composable(AppRoutes.NOTIFICATIONS_SCREEN) {
                val viewModel: NotificationsViewModel = viewModel()
                NotificationsScreen(viewModel = viewModel)
            }
        }
    }
}