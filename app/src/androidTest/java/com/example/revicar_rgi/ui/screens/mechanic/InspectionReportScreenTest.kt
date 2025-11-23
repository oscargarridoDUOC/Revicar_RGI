package com.example.revicar_rgi.ui.screens.mechanic

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.revicar_rgi.data.repository.InspectionRepository
import com.example.revicar_rgi.data.repository.LocalImageRepository
import com.example.revicar_rgi.ui.viewmodel.InspectionReportViewModel
import org.junit.Rule
import org.junit.Test

class InspectionReportScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val inspectionId = "testInspection"

    @Test
    fun muestraTituloYBotones() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val repository = InspectionRepository()
        val imageRepository = LocalImageRepository(context)
        val viewModel = InspectionReportViewModel(repository, imageRepository)

        composeTestRule.setContent {
            val navController = rememberNavController()
            InspectionReportScreen(
                inspectionId = inspectionId,
                viewModel = viewModel,
                navController = navController
            )
        }
        
        composeTestRule.onNodeWithText("Enviar Informe").assertIsDisplayed()
        composeTestRule.onNodeWithText("Galería").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cámara").assertIsDisplayed()
    }

    @Test
    fun muestraCampoDeTextoParaInforme() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val repository = InspectionRepository()
        val imageRepository = LocalImageRepository(context)
        val viewModel = InspectionReportViewModel(repository, imageRepository)

        composeTestRule.setContent {
            val navController = rememberNavController()
            InspectionReportScreen(
                inspectionId = inspectionId,
                viewModel = viewModel,
                navController = navController
            )
        }
        
        // Verificar que existe el campo de texto para el informe
        composeTestRule.onNodeWithText("Escribe los detalles de la inspección...")
            .assertExists()
    }
}
