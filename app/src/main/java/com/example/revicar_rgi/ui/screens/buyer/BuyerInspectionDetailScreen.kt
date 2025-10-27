package com.example.revicar_rgi.ui.screens.buyer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.revicar_rgi.navigation.AppRoutes
import com.example.revicar_rgi.ui.viewmodel.InspectionDetailViewModel
import com.example.revicar_rgi.utils.ValidationUtils
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyerInspectionDetailScreen(
    inspectionId: String,
    viewModel: InspectionDetailViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(inspectionId) {
        viewModel.loadDetails(inspectionId)
    }


    fun formatPrice(price: Double): String {
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
        currencyFormat.maximumFractionDigits = 0
        return currencyFormat.format(price)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Inspección", fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                    val inspection = uiState.inspection!!
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

                        Text("Detalles de la Cita", style = MaterialTheme.typography.titleMedium)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Text("Fecha: ${inspection.dateMillis?.let { ValidationUtils.convertMillisToDate(it) }}")
                        Text("Hora: ${inspection.time}")
                        Text("Lugar: ${inspection.direccion}, ${inspection.comuna}")

                        Spacer(Modifier.height(24.dp))

                        Text("Servicio Solicitado", style = MaterialTheme.typography.titleMedium)

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Text(inspection.serviceType)
                        Text(
                            text = formatPrice(inspection.servicePrice),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(Modifier.height(24.dp))

                        if (inspection.status == "ASIGNADO" || inspection.status == "FINALIZADO") {
                            Text("Mecánico Asignado", style = MaterialTheme.typography.titleMedium)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                            if (uiState.mechanic != null) {
                                val mechanic = uiState.mechanic!!
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
                                onClick = {
                                    navController.navigate("${AppRoutes.BUYER_REPORT_ROUTE}/${inspection.id}")
                                },
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