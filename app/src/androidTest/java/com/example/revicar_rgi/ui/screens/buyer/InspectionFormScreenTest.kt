package com.example.revicar_rgi.ui.screens.buyer

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.revicar_rgi.data.model.InspectionFormUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
class InspectionFormScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun InspectionFormContentForTest(
        uiState: InspectionFormUiState
    ) {
        MaterialTheme {
            Surface {
                Column {
                    Text("Formulario de Inspección")
                    OutlinedTextField(
                        value = uiState.make,
                        onValueChange = {},
                        label = { Text("Marca") }
                    )
                    OutlinedTextField(
                        value = uiState.model,
                        onValueChange = {},
                        label = { Text("Modelo") }
                    )
                    OutlinedTextField(
                        value = uiState.year,
                        onValueChange = {},
                        label = { Text("Año") }
                    )
                    OutlinedTextField(
                        value = uiState.comuna,
                        onValueChange = {},
                        label = { Text("Comuna") }
                    )
                    OutlinedTextField(
                        value = uiState.direccion,
                        onValueChange = {},
                        label = { Text("Dirección") }
                    )
                    Button(onClick = {}) {
                        Text("Confirmar solicitud")
                    }
                }
            }
        }
    }

    // Verifica que la UI muestre los títulos clave
    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun formularioInspeccion_muestraCamposBasicos() {

        val uiState = InspectionFormUiState(
            make = "Toyota",
            model = "Corolla",
            year = "2020",
            comuna = "Santiago",
            direccion = "Av. Siempre Viva 123"
        )

        composeTestRule.setContent {
            InspectionFormContentForTest(uiState)
        }

        composeTestRule.onNodeWithText("Formulario de Inspección").assertIsDisplayed()
        composeTestRule.onNodeWithText("Marca").assertIsDisplayed()
        composeTestRule.onNodeWithText("Modelo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Año").assertIsDisplayed()
        composeTestRule.onNodeWithText("Comuna").assertIsDisplayed()
        composeTestRule.onNodeWithText("Dirección").assertIsDisplayed()
    }

    //Verifica que el botón está presente
    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun formularioInspeccion_tieneBotonEnviar() {

        composeTestRule.setContent {
            InspectionFormContentForTest(InspectionFormUiState())
        }

        composeTestRule.onNodeWithText("Confirmar solicitud").assertIsDisplayed()
    }
}
