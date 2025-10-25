package com.example.revicar_rgi.ui.screens.mechanic

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.revicar_rgi.data.model.Inspection
import com.example.revicar_rgi.ui.components.InspectionCard
import com.example.revicar_rgi.ui.components.UserType

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
                        InspectionCard(
                            inspection = job,
                            userType = UserType.MECHANIC,
                            onCardClick = { onJobClick(job.id) },
                            onButtonClick = {
                                onJobClick(job.id)
                            }
                        )
                    }
                }
            }
        }
    }
}