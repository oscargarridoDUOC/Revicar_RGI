package com.example.revicar_rgi.ui.screens.mechanic

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.revicar_rgi.data.repository.InspectionRepository
import com.example.revicar_rgi.data.repository.LocalImageRepository
import com.example.revicar_rgi.ui.viewmodel.MechanicReportViewModel
import org.junit.Rule
import org.junit.Test

class MechanicReportScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val inspectionId = "testInspection"

    @Test
    fun muestraContenidoDeLaPantalla() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val repository = InspectionRepository()
        val imageRepository = LocalImageRepository(context)
        val viewModel = MechanicReportViewModel(repository, imageRepository)

        composeTestRule.setContent {
            val navController = rememberNavController()
            MechanicReportScreen(
                inspectionId = inspectionId,
                viewModel = viewModel,
                navController = navController
            )
        }
        
        // Verificar que la pantalla se carga correctamente
        composeTestRule.onNode(isRoot()).assertExists()
    }

    @Test
    fun muestraTitulo() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val repository = InspectionRepository()
        val imageRepository = LocalImageRepository(context)
        val viewModel = MechanicReportViewModel(repository, imageRepository)

        composeTestRule.setContent {
            val navController = rememberNavController()
            MechanicReportScreen(
                inspectionId = inspectionId,
                viewModel = viewModel,
                navController = navController
            )
        }
        
        // El título "Informe de Inspección" debería estar visible
        composeTestRule.onNodeWithText("Informe de Inspección").assertExists()
    }
}
