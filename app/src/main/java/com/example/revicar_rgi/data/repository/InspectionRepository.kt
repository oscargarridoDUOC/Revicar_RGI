package com.example.revicar_rgi.data.repository

import android.util.Log
import com.example.revicar_rgi.data.model.Inspection
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.channels.awaitClose

class InspectionRepository {

    private val auth = Firebase.auth
    private val firestore = Firebase.firestore
    private val inspectionsCollection = firestore.collection("inspections")

    private val TAG = "REVICAR_DEBUG"

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

    suspend fun completeInspection(
        inspectionId: String,
        reportText: String
    ): Result<Unit> {
        Log.d(TAG, "REPO: Iniciando completeInspection en $inspectionId")
        return try {
            val currentMechanicId = auth.currentUser?.uid
                ?: return Result.failure(Exception("Mecánico no autenticado."))

            val inspectionRef = inspectionsCollection.document(inspectionId)

            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(inspectionRef)
                val assignedMechanicId = snapshot.getString("mechanicId")

                if (assignedMechanicId == currentMechanicId) {
                    Log.d(TAG, "REPO: ¡Permiso concedido! Actualizando...")
                    transaction.update(
                        inspectionRef, mapOf(
                            "status" to "FINALIZADO",
                            "reportText" to reportText
                        )
                    )
                } else {
                    Log.d(TAG, "REPO: ¡Permiso DENEGADO! IDs no coinciden.")
                    throw Exception("No tienes permiso para finalizar este trabajo.")
                }
            }.await()

            Log.d(TAG, "REPO: Transacción .await() completada con éxito.")
            Result.success(Unit)

        } catch (e: Exception) {
            Log.d(TAG, "REPO: CATCH! La transacción falló: ${e.message}")
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

    fun getMyInspectionsFlow(): Flow<List<Inspection>> = callbackFlow {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            close(Exception("Usuario no autenticado."))
            return@callbackFlow
        }

        val listener = inspectionsCollection
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val inspections = snapshot.toObjects<Inspection>()
                    trySend(inspections)
                }
            }

        awaitClose { listener.remove() }
    }

    fun getAvailableInspectionsFlow(): Flow<List<Inspection>> = callbackFlow {
        val listener = inspectionsCollection
            .whereEqualTo("status", "PENDIENTE")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val inspections = snapshot.toObjects<Inspection>()
                    trySend(inspections)
                }
            }

        awaitClose { listener.remove() }
    }

    fun getMyJobsFlow(): Flow<List<Inspection>> = callbackFlow {
        val mechanicId = auth.currentUser?.uid
        if (mechanicId == null) {
            close(Exception("Mecánico no autenticado."))
            return@callbackFlow
        }

        val listener = inspectionsCollection
            .whereEqualTo("mechanicId", mechanicId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val inspections = snapshot.toObjects<Inspection>()
                    trySend(inspections)
                }
            }

        awaitClose { listener.remove() }
    }

    fun getInspectionByIdFlow(inspectionId: String): Flow<Inspection?> = callbackFlow {
        val listener = inspectionsCollection.document(inspectionId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val inspection = snapshot.toObject(Inspection::class.java)
                    trySend(inspection)
                } else {
                    trySend(null)
                }
            }

        awaitClose { listener.remove() }
    }
}