package com.example.revicar_rgi.data.repository

import com.example.revicar_rgi.data.model.InspectionRequest
import com.google.firebase.Timestamp

class InspectionRepository {

    private val allInspections = listOf(
        InspectionRequest(
            id = "1",
            vehicleModel = "Kia Morning",
            vehicleYear = "2018",
            address = "Dirección completa en Maipú",
            status = "PENDIENTE", // <-- ESTE SE MOSTRARÁ EN "DISPONIBLES"
            totalCost = 90000.0
        ),
        InspectionRequest(
            id = "4", // Otro trabajo pendiente para que la lista tenga más de uno
            vehicleModel = "Hyundai Accent",
            vehicleYear = "2019",
            address = "Otra dirección, Santiago",
            status = "PENDIENTE", // <-- ESTE TAMBIÉN SE MOSTRARÁ
            totalCost = 85000.0
        ),
        InspectionRequest(
            id = "2",
            vehicleModel = "Suzuki Swift",
            vehicleYear = "2021",
            address = "Una calle en Providencia",
            status = "ASIGNADO", // <-- ESTE SE MOSTRARÁ EN "MIS TRABAJOS"
            mechanicUid = "uid_del_mecanico_123",
            totalCost = 120000.0
        ),
        InspectionRequest(
            id = "3",
            vehicleModel = "Chevrolet Onix",
            vehicleYear = "2020",
            address = "Avenida Principal, Las Condes",
            status = "FINALIZADO", // <-- ESTE TAMBIÉN SE MOSTRARÁ EN "MIS TRABAJOS"
            mechanicUid = "uid_del_mecanico_456",
            totalCost = 95000.0
        )
    )

    fun getMyInspections(): List<InspectionRequest> {
        return allInspections
    }

    fun getAvailableJobs(): List<InspectionRequest> {
        // Esta función ahora encontrará y devolverá los dos trabajos PENDIENTE.
        return allInspections.filter { it.status == "PENDIENTE" }
    }

    fun getMyAssignedJobs(mechanicUid: String): List<InspectionRequest> {
        // Esta función devolverá los trabajos ASIGNADO y FINALIZADO.
        return allInspections.filter { it.status == "ASIGNADO" || it.status == "FINALIZADO" }
    }
}