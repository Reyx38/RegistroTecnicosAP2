package edu.ucne.registrotecnicosap2.presentation.prioridades

import edu.ucne.registrotecnicosap2.Data.Entities.PrioridadEntity

data class PrioridadUiState (
    val prioridadId: Int? = null,
    val descripcion: String? = "",
    val descripcionErrorMensaje: String? = "",
    val prioridades: List<PrioridadEntity?> = emptyList()
)
