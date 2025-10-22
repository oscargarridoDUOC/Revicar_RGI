package com.example.revicar_rgi.ui.screens.mechanic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.revicar_rgi.navigation.AppRoutes
import com.example.revicar_rgi.ui.viewmodel.AuthViewModel
import com.example.revicar_rgi.ui.viewmodel.MechanicViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MechanicHomeScreen(
    authViewModel: AuthViewModel,
    mechanicViewModel: MechanicViewModel = viewModel(),
    navControllerApp: NavHostController
) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Trabajos Disponibles", "Mis Trabajos")

    val uiState by mechanicViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ReviCar Mecánico") },
                actions = {
                    IconButton(onClick = {
                        authViewModel.logout()
                        navControllerApp.navigate(AppRoutes.LOGIN_SCREEN) {
                            popUpTo(AppRoutes.MECHANIC_HOME_SCREEN) { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar Sesión")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TabRow(selectedTabIndex = tabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                }
            }

            when (tabIndex) {
                0 -> AvailableJobsScreen(
                    jobs = uiState.availableJobs,
                    isLoading = uiState.isLoading,
                    error = uiState.error,
                    onJobClick = { inspectionId ->
                        scope.launch {
                            mechanicViewModel.acceptJob(inspectionId)
                        }
                    }
                )
                1 -> MyJobsScreen(
                    jobs = uiState.myJobs,
                    isLoading = uiState.isLoading,
                    error = uiState.error,
                    onJobClick = { inspectionId ->
                        scope.launch {
                            mechanicViewModel.completeJob(inspectionId)
                        }
                    }
                )
            }
        }
    }
}