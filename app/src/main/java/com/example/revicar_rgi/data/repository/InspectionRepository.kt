package com.example.revicar_rgi.data.repository

import com.example.revicar_rgi.data.model.Inspection
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
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
        serviceType: String,
        servicePrice: Double
    ): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("Usuario no autenticado."))

            val inspectionData = mapOf(
                "userId" to userId,
                "mechanicId" to null,
                "dateMillis" to dateMillis,
                "time" to time,
                "make" to make,
                "model" to model,
                "year" to year,
                "comuna" to comuna,
                "direccion" to direccion,
                "serviceType" to serviceType,
                "servicePrice" to servicePrice,
                "status" to "PENDIENTE",
                "timestamp" to FieldValue.serverTimestamp()
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

    suspend fun getAvailableInspections(): Result<List<Inspection>> {
        return try {
            val querySnapshot = inspectionsCollection
                .whereEqualTo("status", "PENDIENTE")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val inspections = querySnapshot.toObjects<Inspection>()
            Result.success(inspections)

        } catch (e: Exception) {
            Result.failure(Exception("Error al obtener trabajos disponibles: ${e.message}"))
        }
    }

    suspend fun getMyJobs(): Result<List<Inspection>> {
        return try {
            val mechanicId = auth.currentUser?.uid
                ?: return Result.failure(Exception("Mecánico no autenticado."))

            val querySnapshot = inspectionsCollection
                .whereEqualTo("mechanicId", mechanicId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val inspections = querySnapshot.toObjects<Inspection>()
            Result.success(inspections)

        } catch (e: Exception) {
            Result.failure(Exception("Error al obtener mis trabajos: ${e.message}"))
        }
    }

    suspend fun acceptInspection(inspectionId: String): Result<Unit> {
        return try {
            val mechanicId = auth.currentUser?.uid
                ?: return Result.failure(Exception("Mecánico no autenticado."))

            inspectionsCollection.document(inspectionId).update(
                mapOf(
                    "status" to "ASIGNADO",
                    "mechanicId" to mechanicId
                )
            ).await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(Exception("Error al aceptar el trabajo: ${e.message}"))
        }
    }

    suspend fun completeInspection(inspectionId: String): Result<Unit> {
        return try {
            val currentMechanicId = auth.currentUser?.uid
                ?: return Result.failure(Exception("Mecánico no autenticado."))

            val inspectionRef = inspectionsCollection.document(inspectionId)

            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(inspectionRef)
                val assignedMechanicId = snapshot.getString("mechanicId")

                if (assignedMechanicId == currentMechanicId) {
                    transaction.update(inspectionRef, "status", "FINALIZADO")
                } else {
                    throw Exception("No tienes permiso para finalizar este trabajo.")
                }
            }.await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Error al finalizar el trabajo"))
        }
    }
    suspend fun getInspectionById(inspectionId: String): Result<Inspection> {
        return try {
            val document = inspectionsCollection.document(inspectionId).get().await()
            val inspection = document.toObject(Inspection::class.java)

            if (inspection != null) {
                Result.success(inspection)
            } else {
                Result.failure(Exception("Inspección no encontrada."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error al obtener la inspección: ${e.message}"))
        }
    }
}