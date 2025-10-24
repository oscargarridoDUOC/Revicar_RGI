package com.example.revicar_rgi.ui.screens.mechanic

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.revicar_rgi.data.model.Inspection
import com.example.revicar_rgi.utils.ValidationUtils
import java.text.NumberFormat
import java.util.*

@Composable
fun AvailableJobsScreen(
    jobs: List<Inspection>,
    isLoading: Boolean,
    error: String?,
    onJobClick: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading && jobs.isEmpty() -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            error != null -> {
                Text(
                    text = "Error: $error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
            jobs.isEmpty() -> {
                Text(
                    text = "No hay trabajos disponibles por el momento.",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(jobs) { job ->
                        JobCard(job = job, onClick = { onJobClick(job.id) })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobCard(job: Inspection, onClick: () -> Unit) {


    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    currencyFormat.maximumFractionDigits = 0
    val formattedPrice = currencyFormat.format(job.servicePrice)

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${job.make} ${job.model} (${job.year})",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Lugar: ${job.direccion}, ${job.comuna}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Cita: ${job.dateMillis?.let { ValidationUtils.convertMillisToDate(it) }} a las ${job.time}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Servicio: ${job.serviceType.substringBefore(" - $")}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Estado: ${job.status}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formattedPrice,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}