package com.example.revicar_rgi.ui.screens.buyer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.revicar_rgi.ui.components.InspectionCard
import com.example.revicar_rgi.ui.components.UserType
import com.example.revicar_rgi.ui.viewmodel.InspectionsViewModel

@Composable
fun InspectionsScreen(viewModel: InspectionsViewModel,navController: NavController) {
    val inspections by viewModel.inspections.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Mis Solicitudes")
        }
        items(inspections) { inspection ->
            InspectionCard(
                userType = UserType.BUYER,
                status = inspection.status,
                onCardClick = {
                    navController.navigate("inspection_detail/${inspection.id}")
                }
            )
        }
    }
}