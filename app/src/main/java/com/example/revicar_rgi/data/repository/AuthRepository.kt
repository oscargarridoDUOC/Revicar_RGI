package com.example.revicar_rgi.data.repository

import android.content.Context
import com.example.revicar_rgi.data.local.SessionManager
import com.google.firebase.Firebase
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class AuthRepository(context: Context) {

    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    private val sessionManager = SessionManager(context)

    suspend fun register(
        email: String,
        password: String,
        isMechanic: Boolean,
        name: String,
        lastName: String,
        run: String,
        phone: String
    ): Result<String> {
        return try {

            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: return Result.failure(Exception("No se pudo obtener UID"))


            val userMap = mapOf(
                "uid" to uid,
                "email" to email,
                "mechanic" to isMechanic,
                "name" to name,
                "lastName" to lastName,
                "run" to run,
                "phone" to phone
            )

            firestore.collection("users").document(uid).set(userMap).await()

            sessionManager.saveUid(uid)
            Result.success(uid)
        } catch (e: Exception) {
            val spanishMessage = when (e) {
                is FirebaseAuthUserCollisionException ->
                    "Este correo electrónico ya está registrado."
                is FirebaseNetworkException ->
                    "No hay conexión a internet. Revisa tu conexión."
                else ->
                    "Error desconocido en el registro. Intenta más tarde."
            }
            Result.failure(Exception(spanishMessage))
        }
    }

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: return Result.failure(Exception("No se pudo obtener UID"))
            sessionManager.saveUid(uid)
            Result.success(uid)
        } catch (e: Exception) {
            val spanishMessage = when (e) {
                is FirebaseAuthInvalidUserException ->
                    "El correo electrónico no está registrado."
                is FirebaseAuthInvalidCredentialsException ->
                    "La contraseña es incorrecta. Inténtalo de nuevo."
                is FirebaseNetworkException ->
                    "No hay conexión a internet. Revisa tu conexión."
                else ->
                    "Error de inicio de sesión. Intenta más tarde."
            }
            Result.failure(Exception(spanishMessage))
        }
    }

    suspend fun getUserRole(uid: String): Result<Boolean> {
        return try {
            val document = firestore.collection("users").document(uid).get().await()
            val isMechanic = document.getBoolean("mechanic") ?: false
            Result.success(isMechanic)
        } catch (e: Exception) {
            val spanishMessage = when (e) {
                is FirebaseNetworkException ->
                    "No se pudo verificar tu rol. Revisa tu conexión."
                else ->
                    "No se pudo verificar tu rol. (Usuario no encontrado)."
            }
            Result.failure(Exception(spanishMessage))
        }
    }

    suspend fun logout() {
        auth.signOut()
        sessionManager.clearSession()
    }

    fun getUidFlow() = sessionManager.userUidFlow
}