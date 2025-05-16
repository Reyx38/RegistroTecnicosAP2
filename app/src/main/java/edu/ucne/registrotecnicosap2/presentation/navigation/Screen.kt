package edu.ucne.registrotecnicosap2.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen{
    @Serializable
    data object TecnicoList : Screen()
    @Serializable
    data class Tecnico (val tecnicoId: Int?) : Screen()
}