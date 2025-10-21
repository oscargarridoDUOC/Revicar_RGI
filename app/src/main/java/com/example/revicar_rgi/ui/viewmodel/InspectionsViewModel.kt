package com.example.revicar_rgi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.revicar_rgi.data.model.Inspection
import com.example.revicar_rgi.data.repository.InspectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class InspectionsUiState(
    val inspections: List<Inspection> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class InspectionsViewModel(
    private val repository: InspectionRepository = InspectionRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(InspectionsUiState())
    val uiState: StateFlow<InspectionsUiState> = _uiState.asStateFlow()

    val inspections: StateFlow<List<Inspection>> = _uiState.map { it.inspections }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        loadInspections()
    }

    private fun loadInspections() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val result = repository.getMyInspections()

            result.fold(
                onSuccess = { inspectionList ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            inspections = inspectionList
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
            )
        }
    }
}