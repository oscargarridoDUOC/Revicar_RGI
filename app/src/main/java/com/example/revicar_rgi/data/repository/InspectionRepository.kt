package com.example.revicar_rgi.data.repository

import com.example.revicar_rgi.data.model.Inspection
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

class InspectionRepository {

    private val auth = Firebase.auth
    private val firestore = Firebase.firestore
    private val inspectionsCollection = firestore.collection("inspections")

    suspend fun submitInspection(
        dateMillis: Long,
        time: String,
        make: String,
        model: String,
        year: String,
        comuna: String,
        direccion: String,
        serviceType: String
    ): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("Usuario no autenticado."))

            val inspectionData = hashMapOf(
                "userId" to userId,
                "dateMillis" to dateMillis,
                "time" to time,
                "make" to make,
                "model" to model,
                "year" to year,
                "comuna" to comuna,
                "direccion" to direccion,
                "serviceType" to serviceType,
                "status" to "Agendada",
                "timestamp" to System.currentTimeMillis()
            )

            inspectionsCollection.add(inspectionData).await()
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(Exception("Error al guardar la inspección: ${e.message}"))
        }
    }

    suspend fun getMyInspections(): Result<List<Inspection>> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("Usuario no autenticado."))

            val querySnapshot = inspectionsCollection
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val inspections = querySnapshot.toObjects<Inspection>()

            Result.success(inspections)

        } catch (e: Exception) {
            Result.failure(Exception("Error al obtener las inspecciones: ${e.message}"))
        }
    }
}