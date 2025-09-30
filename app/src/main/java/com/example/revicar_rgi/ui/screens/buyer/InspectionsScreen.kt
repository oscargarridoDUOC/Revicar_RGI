package com.example.revicar_rgi.ui.screens.buyer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.revicar_rgi.data.model.Inspection
import com.example.revicar_rgi.ui.viewmodel.InspectionsViewModel

@Composable
fun InspectionsScreen(viewModel: InspectionsViewModel) {
    val inspections by viewModel.inspections.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            Text(
                text = "Mis Inspecciones",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(inspections) { inspection ->
            InspectionCard(inspection = inspection)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionCard(inspection: Inspection) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = inspection.carModel, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Mecánico: ${inspection.mechanicName}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Fecha: ${inspection.date}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Estado: ${inspection.status}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}