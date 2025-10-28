package com.example.revicar_rgi.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class LocalImageRepository(private val context: Context) {

    private val baseDir = File(context.filesDir, "inspections")

    init {
        if (!baseDir.exists()) {
            baseDir.mkdirs()
        }
    }

    suspend fun saveImages(inspectionId: String, tempUris: List<Uri>) = withContext(Dispatchers.IO) {
        val inspectionDir = File(baseDir, inspectionId)
        if (!inspectionDir.exists()) {
            inspectionDir.mkdirs()
        }

        tempUris.forEach { uri ->
            try {
                val fileName = "${UUID.randomUUID()}.jpg"
                val destinationFile = File(inspectionDir, fileName)

                context.contentResolver.openInputStream(uri)?.use { input ->
                    FileOutputStream(destinationFile).use { output ->
                        input.copyTo(output)
                    }
                }
            } catch (e: Exception) {
                Log.d("LocalImageRepo", "Error al guardar imagen: ${e.message}")
            }
        }
    }

    suspend fun getImageUrisForInspection(inspectionId: String): List<Uri> = withContext(Dispatchers.IO) {
        val inspectionDir = File(baseDir, inspectionId)
        if (!inspectionDir.exists() || !inspectionDir.isDirectory) {
            return@withContext emptyList()
        }

        return@withContext inspectionDir.listFiles { file -> file.isFile && file.extension == "jpg" }
            ?.map { Uri.fromFile(it) }
            ?: emptyList()
    }
}