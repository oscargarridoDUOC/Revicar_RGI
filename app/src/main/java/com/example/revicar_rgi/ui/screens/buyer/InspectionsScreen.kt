package com.example.revicar_rgi.ui.screens.buyer


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.revicar_rgi.navigation.AppRoutes
import com.example.revicar_rgi.ui.components.InspectionCard
import com.example.revicar_rgi.ui.components.UserType
import com.example.revicar_rgi.ui.viewmodel.InspectionsViewModel

@Composable
fun InspectionsScreen(
    viewModel: InspectionsViewModel = viewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Mis Inspecciones",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null -> {
                Text(
                    text = "Error: ${uiState.error}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            uiState.inspections.isEmpty() -> {
                Text(
                    text = "Aún no tienes inspecciones agendadas.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            else -> {
                uiState.inspections.forEach { inspection ->
                    InspectionCard(
                        inspection = inspection,
                        userType = UserType.BUYER,
                        mechanicName = null,
                        onCardClick = {
                            navController.navigate("${AppRoutes.BUYER_INSPECTION_DETAIL_ROUTE}/${inspection.id}")
                        },
                        onButtonClick = {
                            navController.navigate("${AppRoutes.BUYER_INSPECTION_DETAIL_ROUTE}/${inspection.id}")
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}