package com.example.revicar_rgi.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.revicar_rgi.data.model.Inspection
import com.example.revicar_rgi.data.repository.InspectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.catch
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


    init {
        loadInspections()
    }

    private fun loadInspections() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            repository.getMyInspectionsFlow()
                .catch { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
                .collect { inspectionList ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            inspections = inspectionList,
                            error = null
                        )
                    }
                }
        }
    }
}