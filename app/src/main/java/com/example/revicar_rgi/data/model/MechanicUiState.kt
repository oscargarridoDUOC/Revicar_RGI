package com.example.revicar_rgi.data.model

data class MechanicUiState(
    val availableJobs: List<Inspection> = emptyList(),
    val myJobs: List<Inspection> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)