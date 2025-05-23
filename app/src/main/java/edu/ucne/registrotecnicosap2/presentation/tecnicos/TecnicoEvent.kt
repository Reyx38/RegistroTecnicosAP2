package edu.ucne.registrotecnicosap2.presentation.tecnicos

sealed interface TecnicoEvent {
    data class TecnicoChange (val tecnicoId: Int): TecnicoEvent
    data class NombreChange (val nombre: String): TecnicoEvent
    data class SueldoHoraChange (val sueldoHora: Float?): TecnicoEvent
    data object Save: TecnicoEvent
    data object Delete: TecnicoEvent
    data object New : TecnicoEvent
}