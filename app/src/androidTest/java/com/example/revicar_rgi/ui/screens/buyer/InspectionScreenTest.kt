package com.example.revicar_rgi.ui.screens.buyer

import androidx.activity.ComponentActivity
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.revicar_rgi.data.model.Inspection
import com.example.revicar_rgi.ui.viewmodel.InspectionsUiState
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
class InspectionsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Composable
    private fun InspectionsContentForTest(
        uiState: InspectionsUiState
    ) {
        MaterialTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text("Mis Inspecciones")

                Spacer(modifier = Modifier.height(16.dp))

                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(Modifier.testTag("loading"))
                    }

                    uiState.error != null -> {
                        Text("Error: ${uiState.error}")
                    }

                    uiState.inspections.isEmpty() -> {
                        Text("Aún no tienes inspecciones agendadas.")
                    }

                    else -> {
                        uiState.inspections.forEach { inspection ->
                            Text("${inspection.make} ${inspection.model}")
                            Text("Estado: ${inspection.status}")
                        }
                    }
                }
            }
        }
    }


    @Test
    fun inspectionsScreen_showsLoadingState() {

        val uiState = InspectionsUiState(
            inspections = emptyList(),
            isLoading = true
        )

        composeTestRule.setContent {
            InspectionsContentForTest(uiState)
        }

        composeTestRule.onNodeWithTag("loading").assertExists()
    }


    @Test
    fun inspectionsScreen_showsErrorMessage() {

        val uiState = InspectionsUiState(
            inspections = emptyList(),
            isLoading = false,
            error = "Falló la carga"
        )

        composeTestRule.setContent {
            InspectionsContentForTest(uiState)
        }

        composeTestRule.onNodeWithText("Error: Falló la carga").assertIsDisplayed()
    }


    @Test
    fun inspectionsScreen_showsEmptyListMessage() {

        val uiState = InspectionsUiState(
            inspections = emptyList(),
            isLoading = false
        )

        composeTestRule.setContent {
            InspectionsContentForTest(uiState)
        }

        composeTestRule
            .onNodeWithText("Aún no tienes inspecciones agendadas.")
            .assertIsDisplayed()
    }


    @Test
    fun inspectionsScreen_showsInspectionItems() {

        val inspection1 = Inspection(
            id = "1",
            userId = "123",
            make = "Toyota",
            model = "Corolla",
            year = "2020",
            comuna = "Santiago",
            direccion = "Av. Matta 123",
            dateMillis = 1700000000000,
            time = "10:00",
            mechanicId = null,
            serviceType = "Premium",
            servicePrice = 35000.0,
            status = "PENDIENTE"
        )

        val inspection2 = Inspection(
            id = "2",
            userId = "123",
            make = "Honda",
            model = "Civic",
            year = "2018",
            comuna = "Santiago",
            direccion = "Av. Italia 222",
            dateMillis = 1700000000000,
            time = "12:00",
            mechanicId = null,
            serviceType = "Básica",
            servicePrice = 25000.0,
            status = "ASIGNADO"
        )

        val uiState = InspectionsUiState(
            inspections = listOf(inspection1, inspection2),
            isLoading = false
        )

        composeTestRule.setContent {
            InspectionsContentForTest(uiState)
        }

        composeTestRule.onNodeWithText("Toyota Corolla").assertIsDisplayed()
        composeTestRule.onNodeWithText("Estado: PENDIENTE").assertIsDisplayed()

        composeTestRule.onNodeWithText("Honda Civic").assertIsDisplayed()
        composeTestRule.onNodeWithText("Estado: ASIGNADO").assertIsDisplayed()
    }
}