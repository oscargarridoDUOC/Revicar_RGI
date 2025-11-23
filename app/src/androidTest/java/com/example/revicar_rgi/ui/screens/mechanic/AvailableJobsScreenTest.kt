package com.example.revicar_rgi.ui.screens.mechanic

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import com.example.revicar_rgi.data.model.Inspection
import org.junit.Rule
import org.junit.Test

class AvailableJobsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun muestraMensajeCuandoNoHayTrabajos() {
        composeTestRule.setContent {
            AvailableJobsScreen(
                jobs = emptyList(),
                isLoading = false,
                error = null,
                onJobClick = {}
            )
        }

        composeTestRule.onNodeWithText("No hay trabajos disponibles por el momento.")
            .assertIsDisplayed()
    }

    @Test
    fun muestraIndicadorDeCarga() {
        composeTestRule.setContent {
            AvailableJobsScreen(
                jobs = emptyList(),
                isLoading = true,
                error = null,
                onJobClick = {}
            )
        }

        composeTestRule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
            .assertIsDisplayed()
    }

    @Test
    fun muestraMensajeDeError() {
        val mensajeError = "Error de conexión"
        
        composeTestRule.setContent {
            AvailableJobsScreen(
                jobs = emptyList(),
                isLoading = false,
                error = mensajeError,
                onJobClick = {}
            )
        }

        composeTestRule.onNodeWithText("Error: $mensajeError")
            .assertIsDisplayed()
    }

    @Test
    fun muestraListaDeTrabajos() {
        val trabajos = listOf(
            Inspection(
                id = "1",
                make = "Toyota",
                model = "Corolla",
                year = "2020",
                status = "PENDIENTE",
                serviceType = "Revisión técnica",
                servicePrice = 30000.0,
                time = "10:00",
                direccion = "Av. Principal 123",
                comuna = "Santiago",
                dateMillis = 1700000000000L
            )
        )

        composeTestRule.setContent {
            AvailableJobsScreen(
                jobs = trabajos,
                isLoading = false,
                error = null,
                onJobClick = {}
            )
        }

        // Verificar que se muestra información del vehículo
        composeTestRule.onNodeWithText("Toyota Corolla (2020)", substring = true)
            .assertIsDisplayed()
    }
}
