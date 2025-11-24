package com.example.revicar_rgi.ui.screens.mechanic

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.revicar_rgi.ui.viewmodel.MechanicReportViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MechanicReportScreen(
    inspectionId: String,
    viewModel: MechanicReportViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(inspectionId) {
        viewModel.loadInspectionReport(inspectionId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Informe de Inspección", fontSize = 20.sp) },
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
                .padding(16.dp)
        ) {
            when {
                uiState.isLoading -> {
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
                    ) {
                        Text(
                            text = "${inspection.make} ${inspection.model} (${inspection.year})",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "Vehículo inspeccionado el ${inspection.dateMillis?.let { com.example.revicar_rgi.utils.ValidationUtils.convertMillisToDate(it) }}",
                            style = MaterialTheme.typography.labelMedium
                        )

                        Spacer(Modifier.height(24.dp))

                        Text(
                            "Resumen del Mecánico",
                            style = MaterialTheme.typography.titleMedium
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Text(
                            text = inspection.reportText ?: "El mecánico no ha dejado comentarios.",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        if (uiState.imageUrls.isNotEmpty()) {
                            Spacer(Modifier.height(24.dp))
                            Text(
                                "Fotos Adjuntas",
                                style = MaterialTheme.typography.titleMedium
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(uiState.imageUrls) { url ->
                                    AsyncImage(
                                        model = url,
                                        contentDescription = "Foto de inspección",
                                        modifier = Modifier
                                            .size(120.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .border(
                                                1.dp,
                                                MaterialTheme.colorScheme.outline,
                                                RoundedCornerShape(8.dp)
                                            ),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                }
                else -> {
                    Text(
                        "No se pudo cargar el informe.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}