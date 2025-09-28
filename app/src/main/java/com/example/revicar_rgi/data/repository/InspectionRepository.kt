package com.example.revicar_rgi.data.repository

import com.example.revicar_rgi.data.model.Inspection

class InspectionRepository {
    fun getMyInspections(): List<Inspection> {
        return listOf(
            Inspection(1, "Mazda 3 2018", "Completada", "25/09/2025", "Juan Pérez"),
            Inspection(2, "Suzuki Swift 2021", "En Progreso", "28/09/2025", "Ana Torres"),
            Inspection(3, "Chevrolet Onix 2020", "Agendada", "02/10/2025", "Carlos Soto")
        )
    }
}