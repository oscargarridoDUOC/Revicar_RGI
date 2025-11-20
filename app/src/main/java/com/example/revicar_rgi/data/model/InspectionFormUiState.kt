package com.example.revicar_rgi.data.model

data class InspectionFormUiState(
    val dateMillis: Long? = null,
    val time: String = "",
    val make: String = "",
    val model: String = "",
    val year: String = "",
    val comuna: String = "",
    val direccion: String = "",
    val serviceType: String = "",
    val error: String? = null,
    val isSubmitted: Boolean = false,
    val isLoading: Boolean = false,
    val marcas: List<Marca> = emptyList(),
    val modelos: List<Modelo> = emptyList(),
    val isLoadingMarcas: Boolean = false,
    val isLoadingModelos: Boolean = false,
    val selectedMarcaId: Int? = null
)