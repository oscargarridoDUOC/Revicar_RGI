package com.example.revicar_rgi

import SplashScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.revicar_rgi.data.repository.AuthRepository
import com.example.revicar_rgi.navigation.AppRoutes
import com.example.revicar_rgi.ui.screens.MainAppScreen
import com.example.revicar_rgi.ui.screens.common.LoginScreen
import com.example.revicar_rgi.ui.screens.common.RegisterScreen
import com.example.revicar_rgi.ui.screens.mechanic.MechanicHomeScreen
import com.example.revicar_rgi.ui.theme.Revicar_RGITheme
import com.example.revicar_rgi.ui.viewmodel.AuthViewModel
import com.example.revicar_rgi.ui.viewmodel.AuthViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = AuthRepository(applicationContext)
        val authViewModel: AuthViewModel by viewModels {
            AuthViewModelFactory(repository)
        }

        setContent {
            Revicar_RGITheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = AppRoutes.SPLASH_SCREEN
                ) {

                    composable(AppRoutes.SPLASH_SCREEN) {
                        SplashScreen(
                            authViewModel = authViewModel,
                            onNavigate = { route ->
                                navController.navigate(route) {
                                    popUpTo(AppRoutes.SPLASH_SCREEN) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(AppRoutes.LOGIN_SCREEN) {
                        LoginScreen(
                            authViewModel = authViewModel,
                            onSuccessNavigation = { isMechanic ->
                                val route = if (isMechanic) AppRoutes.MECHANIC_HOME_SCREEN else AppRoutes.MAIN_APP_SCREEN
                                navController.navigate(route) {
                                    popUpTo(AppRoutes.LOGIN_SCREEN) { inclusive = true }
                                }
                            },
                            onNavigateToRegister = { navController.navigate(AppRoutes.REGISTER_SCREEN) }
                        )
                    }

                    composable(AppRoutes.REGISTER_SCREEN) {
                        RegisterScreen(
                            authViewModel = authViewModel,
                            onSuccessNavigation = { isMechanic ->
                                val route = if (isMechanic) AppRoutes.MECHANIC_HOME_SCREEN else AppRoutes.MAIN_APP_SCREEN
                                navController.navigate(route) {
                                    popUpTo(AppRoutes.REGISTER_SCREEN) { inclusive = true }
                                    navController.graph.findNode(AppRoutes.LOGIN_SCREEN)?.id?.let {
                                        navController.popBackStack(it, true)
                                    }
                                }
                            },
                            onNavigateToLogin = { navController.popBackStack() }
                        )
                    }

                    composable(AppRoutes.MAIN_APP_SCREEN) {
                        MainAppScreen(authViewModel = authViewModel, navControllerApp = navController)
                    }

                    composable(AppRoutes.MECHANIC_HOME_SCREEN) {
                        MechanicHomeScreen()
                    }
                }
            }
        }
    }
}