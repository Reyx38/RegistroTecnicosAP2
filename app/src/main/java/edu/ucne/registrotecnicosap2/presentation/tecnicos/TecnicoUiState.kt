package edu.ucne.registrotecnicosap2.presentation.tecnicos

import edu.ucne.registrotecnicosap2.Data.Entities.TecnicoEntity

data class TecnicoUiState (
    val tecnicoId: Int? = null,
    val nombre: String? = " ",
    val nombreErrorMessage: String? = null,
    val sueldoHora: Float? = 0.0f,
    val sueldoErrorMessage: String? = null,
    val tecnicos: List<TecnicoEntity> = emptyList()
)