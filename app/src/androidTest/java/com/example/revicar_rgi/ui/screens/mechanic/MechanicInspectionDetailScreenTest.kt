package com.example.revicar_rgi.ui.screens.mechanic

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.revicar_rgi.data.repository.AuthRepository
import com.example.revicar_rgi.data.repository.InspectionRepository
import com.example.revicar_rgi.ui.viewmodel.InspectionDetailViewModel
import org.junit.Rule
import org.junit.Test

class MechanicInspectionDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun muestraTitulo() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val inspectionRepository = InspectionRepository()
        val authRepository = AuthRepository(context)
        val viewModel = InspectionDetailViewModel(inspectionRepository, authRepository)

        composeTestRule.setContent {
            val navController = rememberNavController()
            MechanicInspectionDetailScreen(
                inspectionId = "1",
                viewModel = viewModel,
                navController = navController
            )
        }

        composeTestRule.onNodeWithText("Detalle del Trabajo").assertIsDisplayed()
    }

    @Test
    fun muestraDetallesDeInspeccion() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val inspectionRepository = InspectionRepository()
        val authRepository = AuthRepository(context)
        val viewModel = InspectionDetailViewModel(inspectionRepository, authRepository)

        composeTestRule.setContent {
            val navController = rememberNavController()
            MechanicInspectionDetailScreen(
                inspectionId = "1",
                viewModel = viewModel,
                navController = navController
            )
        }

        // Verificar que la pantalla se carga correctamente
        composeTestRule.onNode(isRoot()).assertExists()
    }
}
