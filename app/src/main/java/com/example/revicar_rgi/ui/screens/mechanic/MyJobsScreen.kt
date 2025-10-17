package com.example.revicar_rgi.ui.screens.mechanic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.revicar_rgi.ui.components.InspectionCard
import com.example.revicar_rgi.ui.components.UserType
import com.example.revicar_rgi.ui.viewmodel.MechanicViewModel

@Composable
fun MyJobsScreen(
    viewModel: MechanicViewModel,
    navController: NavController
) {
    val jobs by viewModel.myJobs.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(jobs) { job ->
            InspectionCard(
                userType = UserType.MECHANIC,
                status = job.status,
                onCardClick = {
                    navController.navigate("inspection_detail/${job.id}")
                }
            )
        }
    }
}