package com.example.revicar_rgi.data.model

import com.google.firebase.Timestamp

data class InspectionRequest(
    val id: String = "",
    val buyerUid: String = "",
    val mechanicUid: String? = null,
    val status: String = "PENDIENTE", // PENDIENTE, ASIGNADO, FINALIZADO
    val requestDate: Timestamp = Timestamp.now(),
    val address: String = "",
    val vehicleModel: String = "",
    val vehicleYear: String = "",
    val totalCost: Double = 0.0
)