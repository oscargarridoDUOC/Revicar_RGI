package com.example.revicar_rgi.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Inspection(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val mechanicId: String? = null,
    val dateMillis: Long? = null,
    val time: String = "",
    val make: String = "",
    val model: String = "",
    val year: String = "",
    val comuna: String = "",
    val direccion: String = "",
    val serviceType: String = "",
    val servicePrice: Double = 0.0,
    val status: String = "PENDIENTE",
    @ServerTimestamp
    val timestamp: Date? = null,
    val reportText: String? = null,
    val imageUrls: List<String> = emptyList()
)