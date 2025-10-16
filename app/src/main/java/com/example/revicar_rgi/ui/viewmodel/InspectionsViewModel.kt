package com.example.revicar_rgi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.revicar_rgi.data.model.InspectionRequest
import com.example.revicar_rgi.data.repository.InspectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InspectionsViewModel(
    private val repository: InspectionRepository = InspectionRepository()
) : ViewModel() {

    private val _inspections = MutableStateFlow<List<InspectionRequest>>(emptyList())
    val inspections: StateFlow<List<InspectionRequest>> = _inspections.asStateFlow()

    init {
        viewModelScope.launch {
            _inspections.value = repository.getMyInspections()
        }
    }
}