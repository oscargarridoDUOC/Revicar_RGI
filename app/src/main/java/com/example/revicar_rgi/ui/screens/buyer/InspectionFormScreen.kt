package com.example.revicar_rgi.ui.screens.buyer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.revicar_rgi.ui.viewmodel.InspectionFormViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionFormScreen(
    navHost: NavHostController,
    viewModel: InspectionFormViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDateModal by remember { mutableStateOf(false) }
    var showTimeModal by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSubmitted) {
        if (uiState.isSubmitted) {
            delay(2000)
            navHost.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Formulario de Inspección") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            SectionTitle("1. Fecha y Hora de la Cita")

            Box {
                OutlinedTextField(
                    value = uiState.dateMillis?.let { convertMillisToDate(it) } ?: "",
                    onValueChange = {},
                    label = { Text("Fecha") },
                    trailingIcon = {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Seleccionar fecha")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    enabled = !uiState.isLoading && !uiState.isSubmitted
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable(enabled = !uiState.isLoading && !uiState.isSubmitted) {
                            showDateModal = true
                        }
                )
            }

            Spacer(Modifier.height(16.dp))

            Box {
                OutlinedTextField(
                    value = uiState.time,
                    onValueChange = {},
                    label = { Text("Hora") },
                    trailingIcon = {
                        Icon(Icons.Default.Schedule, contentDescription = "Seleccionar hora")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    enabled = !uiState.isLoading && !uiState.isSubmitted
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable(enabled = !uiState.isLoading && !uiState.isSubmitted) {
                            showTimeModal = true
                        }
                )
            }

            Spacer(Modifier.height(24.dp))
            SectionTitle("2. Datos del Vehículo")

            OutlinedTextField(
                value = uiState.make,
                onValueChange = viewModel::updateMake,
                label = { Text("Marca") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading && !uiState.isSubmitted
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.model,
                onValueChange = viewModel::updateModel,
                label = { Text("Modelo") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading && !uiState.isSubmitted
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.year,
                onValueChange = viewModel::updateYear,
                label = { Text("Año") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading && !uiState.isSubmitted
            )

            Spacer(Modifier.height(24.dp))
            SectionTitle("3. Tipo de Servicio")

            val servicios = remember {
                listOf(
                    "Revisión básica - $25.000",
                    "Revisión completa - $45.000",
                    "Revisión premium - $65.000"
                )
            }

            Column {
                servicios.forEach { servicio ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !uiState.isLoading && !uiState.isSubmitted) {
                                viewModel.updateServiceType(servicio)
                            }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(servicio)
                        RadioButton(
                            selected = uiState.serviceType == servicio,
                            onClick = { viewModel.updateServiceType(servicio) },
                            enabled = !uiState.isLoading && !uiState.isSubmitted
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = viewModel::submitForm,
                enabled = !uiState.isLoading && !uiState.isSubmitted,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Confirmar solicitud")
                }
            }

            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                uiState.error?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }

                if (uiState.isSubmitted) {
                    uiState.dateMillis?.let { fechaEnMillis ->
                        val fechaFormateada = convertMillisToDate(fechaEnMillis)
                        val mensajeExito =
                            "✅ ¡Éxito! Hora reservada a las ${uiState.time} del $fechaFormateada."

                        Text(
                            text = mensajeExito,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }

    if (showDateModal) {
        DatePickerModal(
            onDateSelected = { fechaSeleccionada ->
                viewModel.updateDate(fechaSeleccionada)
                showDateModal = false
            },
            onDismiss = { showDateModal = false }
        )
    }

    if (showTimeModal) {
        TimePickerModal(
            onTimeSelected = { horaSeleccionada ->
                viewModel.updateTime(horaSeleccionada)
                showTimeModal = false
            },
            onDismiss = { showTimeModal = false }
        )
    }
}


@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) { Text("OK") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerModal(
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState(is24Hour = true)
    AlertDialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.extraLarge, tonalElevation = 6.dp) {
            Column(
                modifier = Modifier.padding(
                    top = 24.dp,
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 12.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Seleccionar hora",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(20.dp))
                TimeInput(state = timePickerState)
                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancelar") }
                    Spacer(Modifier.width(8.dp))
                    TextButton(onClick = {
                        val hour = timePickerState.hour.toString().padStart(2, '0')
                        val minute = timePickerState.minute.toString().padStart(2, '0')
                        onTimeSelected("$hour:$minute")
                        onDismiss()
                    }) { Text("Confirmar") }
                }
            }
        }
    }
}

private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}