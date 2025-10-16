package com.example.revicar_rgi.data.repository

import com.example.revicar_rgi.data.model.InspectionRequest
import com.google.firebase.Timestamp

class InspectionRepository {
    fun getMyInspections(): List<InspectionRequest> {
        return listOf(
            InspectionRequest(
                id = "1",
                vehicleModel = "Kia Morning",
                vehicleYear = "2018",
                address = "Dirección completa en Maipú",
                status = "PENDIENTE",
                totalCost = 90000.0
            ),
            InspectionRequest(
                id = "2",
                vehicleModel = "Suzuki Swift",
                vehicleYear = "2021",
                address = "Una calle en Providencia",
                status = "ASIGNADO",
                mechanicUid = "uid_del_mecanico_123",
                totalCost = 120000.0
            ),
            InspectionRequest(
                id = "3",
                vehicleModel = "Chevrolet Onix",
                vehicleYear = "2020",
                address = "Avenida Principal, Las Condes",
                status = "FINALIZADO",
                mechanicUid = "uid_del_mecanico_456",
                totalCost = 95000.0
            )
        )
    }
}