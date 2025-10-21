package com.example.revicar_rgi.data.repository

import android.content.Context
import com.example.revicar_rgi.data.local.SessionManager
import com.example.revicar_rgi.data.model.User
import com.google.firebase.Firebase
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

            val user = User(
                uid = uid,
                email = email,
                isMechanic = isMechanic,
                name = name,
                lastName = lastName,
                run = run,
                phone = phone
            )

            firestore.collection("users").document(uid).set(user).await()

            sessionManager.saveUid(uid)
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: return Result.failure(Exception("No se pudo obtener UID"))
            sessionManager.saveUid(uid)
            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserRole(uid: String): Result<Boolean> {
        return try {
            val document = firestore.collection("users").document(uid).get().await()
            val isMechanic = document.getBoolean("mechanic") ?: false
            Result.success(isMechanic)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        auth.signOut()
        sessionManager.clearSession()
    }

    fun getUidFlow() = sessionManager.userUidFlow
}