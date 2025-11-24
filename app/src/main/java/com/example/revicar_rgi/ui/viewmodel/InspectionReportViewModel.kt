package com.example.revicar_rgi.ui.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.revicar_rgi.data.repository.InspectionRepository
import com.example.revicar_rgi.data.repository.LocalImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class InspectionReportUiState(
    val reportText: String = "",
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null,
    val images: List<Uri> = emptyList()
)

class InspectionReportViewModel(
    private val repository: InspectionRepository,
    private val localImageRepository: LocalImageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InspectionReportUiState())
    val uiState: StateFlow<InspectionReportUiState> = _uiState.asStateFlow()

    private val TAG = "REVICAR_DEBUG"

    fun onReportTextChange(newText: String) {
        _uiState.update { it.copy(reportText = newText) }
    }

    fun addImages(uris: List<Uri>) {
        _uiState.update { currentState ->
            val updatedImages = (currentState.images + uris).distinct()
            currentState.copy(images = updatedImages)
        }
    }

    fun removeImage(uri: Uri) {
        _uiState.update { currentState ->
            val updatedImages = currentState.images.filter { it != uri }
            currentState.copy(images = updatedImages)
        }
    }

    fun saveReport(inspectionId: String) {
        viewModelScope.launch {
            Log.d(TAG, "VM: Iniciando saveReport...")
            _uiState.update { it.copy(isSaving = true, error = null) }

            val uploadResult = repository.uploadImages(
                inspectionId = inspectionId,
                imageUris = _uiState.value.images
            )

            uploadResult.fold(
                onSuccess = { imageUrls ->
                    val saveResult = repository.completeInspection(
                        inspectionId = inspectionId,
                        reportText = _uiState.value.reportText,
                        imageUrls = imageUrls
                    )

                    saveResult.fold(
                        onSuccess = {
                            Log.d(TAG, "VM: saveReport tuvo ÉXITO.")
                            _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
                        },
                        onFailure = { error ->
                            Log.d(TAG, "VM: saveReport FALLÓ al guardar datos: ${error.message}")
                            _uiState.update { it.copy(isSaving = false, error = error.message) }
                        }
                    )
                },
                onFailure = { error ->
                    Log.d(TAG, "VM: saveReport FALLÓ al subir imágenes: ${error.message}")
                    _uiState.update { it.copy(isSaving = false, error = "Error al subir imágenes: ${error.message}") }
                }
            )
            Log.d(TAG, "VM: saveReport terminado.")
        }
    }

    class Factory(
        private val repository: InspectionRepository,
        private val localImageRepository: LocalImageRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InspectionReportViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return InspectionReportViewModel(repository, localImageRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}