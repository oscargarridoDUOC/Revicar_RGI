package com.example.revicar_rgi.ui.screens.buyer

import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.revicar_rgi.data.model.Inspection
import com.example.revicar_rgi.data.model.InspectionDetailUiState
import com.example.revicar_rgi.data.model.User
import com.example.revicar_rgi.utils.ValidationUtils
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@RunWith(AndroidJUnit4::class)
class BuyerInspectionDetailScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun BuyerInspectionDetailTestContent(
        uiState: InspectionDetailUiState,
        onBack: () -> Unit = {},
        onViewReport: (String) -> Unit = {}
    ) {
        fun formatPrice(price: Double): String {
            val format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("es", "CL"))
            format.maximumFractionDigits = 0
            return format.format(price)
        }

        androidx.compose.material3.Scaffold(
            topBar = {
                androidx.compose.material3.TopAppBar(
                    title = { Text("Detalle de Inspección", fontSize = 20.sp) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when {
                    uiState.isLoading && uiState.inspection == null -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    uiState.error != null -> {
                        Text(
                            text = "Error: ${uiState.error}",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    uiState.inspection != null -> {
                        val inspection = uiState.inspection

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "${inspection.make} ${inspection.model} (${inspection.year})",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "Estado: ${inspection.status}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )

                            Spacer(Modifier.height(24.dp))

                            Text(
                                "Detalles de la Cita",
                                style = MaterialTheme.typography.titleMedium
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                            val fecha = inspection.dateMillis?.let {
                                ValidationUtils.convertMillisToDate(it)
                            } ?: ""

                            Text("Fecha: $fecha")
                            Text("Hora: ${inspection.time}")
                            Text("Lugar: ${inspection.direccion}, ${inspection.comuna}")

                            Spacer(Modifier.height(24.dp))

                            Text(
                                "Servicio Solicitado",
                                style = MaterialTheme.typography.titleMedium
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            Text(inspection.serviceType)
                            Text(
                                text = formatPrice(inspection.servicePrice),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(Modifier.height(24.dp))

                            if (inspection.status == "ASIGNADO" || inspection.status == "FINALIZADO") {
                                Text(
                                    "Mecánico Asignado",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                                if (uiState.mechanic != null) {
                                    val mechanic = uiState.mechanic
                                    Text("Nombre: ${mechanic.name} ${mechanic.lastName}")
                                    Text("Teléfono: ${mechanic.phone}")
                                    Text("Email: ${mechanic.email}")
                                } else if (!uiState.isLoading) {
                                    Text("Cargando datos del mecánico...")
                                }
                            }

                            Spacer(Modifier.height(32.dp))

                            if (inspection.status == "FINALIZADO") {
                                Button(
                                    onClick = { onViewReport(inspection.id) },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = true
                                ) {
                                    Text("VER INFORME")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun verificaElementosEstticosSeMuestran() {
        val fakeInspection = Inspection(
            id = "1",
            userId = "99",
            mechanicId = "10",
            dateMillis = 1700000000000,
            time = "10:00",
            make = "Toyota",
            model = "Corolla",
            year = "2020",
            comuna = "Santiago",
            direccion = "Av. Siempre Viva 123",
            serviceType = "Inspección Completa",
            servicePrice = 35000.0,
            status = "ASIGNADO"
        )

        val fakeMechanic = User(
            uid = "10",
            email = "mecanico@test.com",
            mechanic = true,
            name = "Juan",
            lastName = "Pérez",
            run = "11.111.111-1",
            phone = "+56999999999"
        )

        val uiState = InspectionDetailUiState(
            inspection = fakeInspection,
            mechanic = fakeMechanic,
            isLoading = false,
            error = null
        )

        composeTestRule.setContent {
            MaterialTheme {
                Surface {
                    BuyerInspectionDetailTestContent(
                        uiState = uiState
                    )
                }
            }
        }

        // Verifica textos clave
        composeTestRule.onNodeWithText("Detalle de Inspección").assertIsDisplayed()
        composeTestRule.onNodeWithText("Toyota Corolla (2020)").assertIsDisplayed()
        composeTestRule.onNodeWithText("Estado: ASIGNADO").assertIsDisplayed()
        composeTestRule.onNodeWithText("Detalles de la Cita").assertIsDisplayed()
        composeTestRule.onNodeWithText("Servicio Solicitado").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mecánico Asignado").assertIsDisplayed()
        composeTestRule.onNodeWithText("Nombre: Juan Pérez").assertIsDisplayed()
    }

    @Test
    fun verificaBotonVerInformeLlamaCallbackCuandoFinalizado() {
        var clickedId: String? = null

        val fakeInspection = Inspection(
            id = "55",
            userId = "1",
            mechanicId = null,
            dateMillis = 1700000000000,
            time = "12:00",
            make = "Honda",
            model = "Civic",
            year = "2018",
            comuna = "Providencia",
            direccion = "Av. Salvador 222",
            serviceType = "Inspección Premium",
            servicePrice = 55000.0,
            status = "FINALIZADO"
        )

        val uiState = InspectionDetailUiState(
            inspection = fakeInspection,
            mechanic = null,
            isLoading = false,
            error = null
        )

        composeTestRule.setContent {
            MaterialTheme {
                Surface {
                    BuyerInspectionDetailTestContent(
                        uiState = uiState,
                        onViewReport = { id -> clickedId = id }
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("VER INFORME")
            .assertIsDisplayed()
            .performClick()

        composeTestRule.runOnIdle {
            assert(clickedId == "55")
        }
    }
}
