package com.example.revicar_rgi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.revicar_rgi.data.model.InspectionRequest
import com.example.revicar_rgi.data.repository.AuthRepository
import com.example.revicar_rgi.data.repository.InspectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MechanicViewModel(
    private val inspectionRepository: InspectionRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _availableJobs = MutableStateFlow<List<InspectionRequest>>(emptyList())
    val availableJobs: StateFlow<List<InspectionRequest>> = _availableJobs.asStateFlow()

    private val _myJobs = MutableStateFlow<List<InspectionRequest>>(emptyList())
    val myJobs: StateFlow<List<InspectionRequest>> = _myJobs.asStateFlow()

    init {
        loadAvailableJobs()
        loadMyJobs()
    }

    private fun loadAvailableJobs() {
        viewModelScope.launch {
            _availableJobs.value = inspectionRepository.getAvailableJobs()
        }
    }

    private fun loadMyJobs() {
        viewModelScope.launch {
            val mechanicUid = authRepository.getUidFlow().firstOrNull()
            if (mechanicUid != null) {
                _myJobs.value = inspectionRepository.getMyAssignedJobs(mechanicUid)
            }
        }
    }
}

class MechanicViewModelFactory(
    private val inspectionRepository: InspectionRepository,
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MechanicViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MechanicViewModel(inspectionRepository, authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}