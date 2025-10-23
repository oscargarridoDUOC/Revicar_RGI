package com.example.revicar_rgi.data.model

data class InspectionDetailUiState(
    val inspection: Inspection? = null,
    val mechanic: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val actionSuccess: Boolean = false
)