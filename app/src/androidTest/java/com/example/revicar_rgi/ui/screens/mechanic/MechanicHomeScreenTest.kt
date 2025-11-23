package com.example.revicar_rgi.ui.screens.mechanic

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.revicar_rgi.data.repository.AuthRepository
import com.example.revicar_rgi.ui.viewmodel.AuthViewModel
import com.example.revicar_rgi.ui.viewmodel.MechanicViewModel
import org.junit.Rule
import org.junit.Test

class MechanicHomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun muestraTituloYElementosPrincipales() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val authRepository = AuthRepository(context)
        val authViewModel = AuthViewModel(authRepository)
        val mechanicViewModel = MechanicViewModel()

        composeTestRule.setContent {
            val navController = rememberNavController()
            MechanicHomeScreen(
                authViewModel = authViewModel,
                mechanicViewModel = mechanicViewModel,
                navControllerApp = navController
            )
        }

        // Verificar título
        composeTestRule.onNodeWithText("ReviCar Mecánico").assertIsDisplayed()

        // Verificar tabs
        composeTestRule.onNodeWithText("Trabajos Disponibles").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mis Trabajos").assertIsDisplayed()
    }

    @Test
    fun cambioEntreTabs() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val authRepository = AuthRepository(context)
        val authViewModel = AuthViewModel(authRepository)
        val mechanicViewModel = MechanicViewModel()

        composeTestRule.setContent {
            val navController = rememberNavController()
            MechanicHomeScreen(
                authViewModel = authViewModel,
                mechanicViewModel = mechanicViewModel,
                navControllerApp = navController
            )
        }

        // Por defecto, debe mostrar "Trabajos Disponibles"
        composeTestRule.onNodeWithText("No hay trabajos disponibles por el momento.").assertIsDisplayed()

        // Click en "Mis Trabajos"
        composeTestRule.onNodeWithText("Mis Trabajos").performClick()

        // Debe mostrar el mensaje de "Mis Trabajos"
        composeTestRule.onNodeWithText("Aún no has aceptado ningún trabajo.").assertIsDisplayed()
    }
}
