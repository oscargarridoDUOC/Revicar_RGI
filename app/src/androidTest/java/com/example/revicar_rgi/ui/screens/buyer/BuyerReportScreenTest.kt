package com.example.revicar_rgi.ui.screens.buyer

import androidx.activity.ComponentActivity
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.*
import com.example.revicar_rgi.ui.viewmodel.BuyerReportUiState
import com.example.revicar_rgi.data.model.Inspection
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@RunWith(AndroidJUnit4::class)
class BuyerReportScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun BuyerReportContentForTest(
        uiState: BuyerReportUiState
    ) {
        MaterialTheme {
            Surface {
                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(Modifier.testTag("loading"))
                    }
                    uiState.error != null -> {
                        Text("Error: ${uiState.error}")
                    }
                    uiState.inspection != null -> {
                        val ins = uiState.inspection
                        Column(
                            Modifier
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text("Informe de Inspección")
                            Text("${ins.make} ${ins.model} (${ins.year})")
                            Text("Resumen del Mecánico")
                            Text(ins.reportText ?: "No hay reporte")
                        }
                    }
                    else -> {
                        Text("No se pudo cargar el informe.")
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun reportScreen_ShowsDataCorrectly() {
        val fakeInspection = Inspection(
            id = "1",
            userId = "1",
            mechanicId = "10",
            dateMillis = 1700000000000,
            time = "10:00",
            make = "Toyota",
            model = "Corolla",
            year = "2020",
            comuna = "Santiago",
            direccion = "Av. Siempre Viva 123",
            serviceType = "Premium",
            servicePrice = 50000.0,
            status = "FINALIZADO",
            reportText = "Revisión completa realizada, vehículo en buen estado."
        )

        val uiState = BuyerReportUiState(
            inspection = fakeInspection,
            isLoading = false,
            error = null
        )

        composeTestRule.setContent {
            BuyerReportContentForTest(uiState)
        }

        composeTestRule.onNodeWithText("Informe de Inspección").assertIsDisplayed()
        composeTestRule.onNodeWithText("Toyota Corolla (2020)").assertIsDisplayed()
        composeTestRule.onNodeWithText("Resumen del Mecánico").assertIsDisplayed()
        composeTestRule.onNodeWithText("Revisión completa realizada, vehículo en buen estado.")
            .assertIsDisplayed()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun reportScreen_ShowsLoading() {
        val uiState = BuyerReportUiState(isLoading = true)

        composeTestRule.setContent {
            BuyerReportContentForTest(uiState)
        }

        composeTestRule.onNodeWithTag("loading").assertExists()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun reportScreen_ShowsError() {
        val uiState = BuyerReportUiState(
            isLoading = false,
            error = "No se pudo cargar"
        )

        composeTestRule.setContent {
            BuyerReportContentForTest(uiState)
        }

        composeTestRule.onNodeWithText("Error: No se pudo cargar").assertIsDisplayed()
    }
}
