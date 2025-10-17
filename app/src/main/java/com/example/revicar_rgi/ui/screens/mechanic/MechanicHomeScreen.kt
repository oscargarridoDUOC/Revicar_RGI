package com.example.revicar_rgi.ui.screens.mechanic

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.revicar_rgi.data.repository.AuthRepository
import com.example.revicar_rgi.data.repository.InspectionRepository
import com.example.revicar_rgi.navigation.AppRoutes
import com.example.revicar_rgi.ui.viewmodel.AuthViewModel
import com.example.revicar_rgi.ui.viewmodel.MechanicViewModel
import com.example.revicar_rgi.ui.viewmodel.MechanicViewModelFactory

object MechanicRoutes {
    const val AVAILABLE_JOBS = "available_jobs"
    const val MY_JOBS = "my_jobs"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MechanicHomeScreen(authViewModel: AuthViewModel, navControllerApp: NavHostController) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val mechanicViewModel: MechanicViewModel = viewModel(
        factory = MechanicViewModelFactory(
            InspectionRepository(),
            AuthRepository(context)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Portal del Mecánico") },
                actions = {
                    IconButton(onClick = {
                        authViewModel.logout()
                        navControllerApp.navigate(AppRoutes.LOGIN_SCREEN) {
                            popUpTo(AppRoutes.MECHANIC_HOME_SCREEN) { inclusive = true }
                        }
                    }) {
                        // 4. Se usa el ícono Logout desde 'Filled'
                        Icon(Icons.Filled.List, "Cerrar Sesión")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == MechanicRoutes.AVAILABLE_JOBS } == true,
                    onClick = {
                        navController.navigate(MechanicRoutes.AVAILABLE_JOBS) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    // 5. Se usa el ícono List desde 'Filled'
                    icon = { Icon(Icons.Filled.List, "Trabajos Disponibles") },
                    label = { Text("Disponibles") }
                )
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == MechanicRoutes.MY_JOBS } == true,
                    onClick = {
                        navController.navigate(MechanicRoutes.MY_JOBS) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    // 6. Se usa el ícono Assignment desde 'Filled'
                    icon = { Icon(Icons.Filled.List, "Mis Trabajos") },
                    label = { Text("Mis Trabajos") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MechanicRoutes.AVAILABLE_JOBS,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(MechanicRoutes.AVAILABLE_JOBS) {
                AvailableJobsScreen(viewModel = mechanicViewModel, navController = navController)
            }
            composable(MechanicRoutes.MY_JOBS) {
                MyJobsScreen(viewModel = mechanicViewModel, navController = navController)
            }
        }
    }
}