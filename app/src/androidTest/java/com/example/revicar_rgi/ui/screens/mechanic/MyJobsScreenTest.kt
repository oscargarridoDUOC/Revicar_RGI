package com.example.revicar_rgi.ui.screens.mechanic

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import com.example.revicar_rgi.data.model.Inspection
import org.junit.Rule
import org.junit.Test

class MyJobsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun muestraMensajeCuandoNoHayTrabajosAceptados() {
        composeTestRule.setContent {
            MyJobsScreen(
                jobs = emptyList(),
                isLoading = false,
                error = null,
                onJobClick = {}
            )
        }

        composeTestRule.onNodeWithText("Aún no has aceptado ningún trabajo.")
            .assertIsDisplayed()
    }

    @Test
    fun muestraIndicadorDeCarga() {
        composeTestRule.setContent {
            MyJobsScreen(
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
        val mensajeError = "Error al cargar trabajos"
        
        composeTestRule.setContent {
            MyJobsScreen(
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
    fun muestraListaDeTrabajosAceptados() {
        val trabajos = listOf(
            Inspection(
                id = "2",
                make = "Honda",
                model = "Civic",
                year = "2021",
                status = "ASIGNADO",
                serviceType = "Inspección completa",
                servicePrice = 45000.0,
                time = "14:00",
                direccion = "Calle Secundaria 456",
                comuna = "Providencia",
                dateMillis = 1700100000000L
            )
        )

        composeTestRule.setContent {
            MyJobsScreen(
                jobs = trabajos,
                isLoading = false,
                error = null,
                onJobClick = {}
            )
        }

        // Verificar que se muestra información del vehículo
        composeTestRule.onNodeWithText("Honda Civic (2021)", substring = true)
            .assertIsDisplayed()
    }
}
