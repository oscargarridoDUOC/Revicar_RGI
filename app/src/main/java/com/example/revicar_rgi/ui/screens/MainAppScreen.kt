package com.example.revicar_rgi.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.revicar_rgi.data.repository.AuthRepository
import com.example.revicar_rgi.data.repository.InspectionRepository
import com.example.revicar_rgi.navigation.AppRoutes
import com.example.revicar_rgi.navigation.BottomBar
import com.example.revicar_rgi.ui.screens.buyer.BuyerHomeScreen
import com.example.revicar_rgi.ui.screens.buyer.BuyerInspectionDetailScreen
import com.example.revicar_rgi.ui.screens.buyer.BuyerReportScreen
import com.example.revicar_rgi.ui.screens.buyer.InspectionFormScreen
import com.example.revicar_rgi.ui.screens.buyer.InspectionsScreen
import com.example.revicar_rgi.ui.screens.common.NotificationsScreen
import com.example.revicar_rgi.ui.viewmodel.AuthViewModel
import com.example.revicar_rgi.ui.viewmodel.BuyerReportViewModel
import com.example.revicar_rgi.ui.viewmodel.InspectionDetailViewModel
import com.example.revicar_rgi.ui.viewmodel.InspectionsViewModel
import com.example.revicar_rgi.ui.viewmodel.NotificationsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(authViewModel: AuthViewModel, navControllerApp: NavHostController) {
    val navController = rememberNavController()

    val context = LocalContext.current
    val authRepository = AuthRepository(context)
    val inspectionRepository = InspectionRepository()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ReviCar") },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.NOTIFICATIONS_SCREEN) }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
                    }
                    IconButton(onClick = {
                        authViewModel.logout()
                        navControllerApp.navigate(AppRoutes.LOGIN_SCREEN) {
                            popUpTo(AppRoutes.MAIN_APP_SCREEN) { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar Sesión")
                    }
                }
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
                InspectionsScreen(viewModel = viewModel, navController = navController)
            }
            composable(AppRoutes.NOTIFICATIONS_SCREEN) {
                val viewModel: NotificationsViewModel = viewModel()
                NotificationsScreen(viewModel = viewModel)
            }

            composable(AppRoutes.INSPECTION_FORM_SCREEN) {
                InspectionFormScreen(navHost = navController)
            }

            composable(
                route = AppRoutes.BUYER_INSPECTION_DETAIL,
                arguments = listOf(navArgument("inspectionId") { type = NavType.StringType })
            ) { backStackEntry ->
                val inspectionId = backStackEntry.arguments?.getString("inspectionId")

                val factory = InspectionDetailViewModel.Factory(inspectionRepository, authRepository)
                val detailViewModel: InspectionDetailViewModel = viewModel(factory = factory)

                if (inspectionId != null) {
                    BuyerInspectionDetailScreen(
                        inspectionId = inspectionId,
                        viewModel = detailViewModel,
                        navController = navController
                    )
                }
            }

            composable(
                route = AppRoutes.BUYER_REPORT_SCREEN,
                arguments = listOf(navArgument("inspectionId") { type = NavType.StringType })
            ) { backStackEntry ->
                val inspectionId = backStackEntry.arguments?.getString("inspectionId")

                val factory = BuyerReportViewModel.Factory(inspectionRepository)
                val reportViewModel: BuyerReportViewModel = viewModel(factory = factory)

                if (inspectionId != null) {
                    BuyerReportScreen(
                        inspectionId = inspectionId,
                        viewModel = reportViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}