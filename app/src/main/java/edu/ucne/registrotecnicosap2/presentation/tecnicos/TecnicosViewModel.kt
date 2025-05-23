package edu.ucne.registrotecnicosap2.presentation.tecnicos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.registrotecnicosap2.Data.Entities.TecnicoEntity
import edu.ucne.registrotecnicosap2.Data.repository.TecnicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class TecnicosViewModel @Inject constructor(
    private val tecnicoRepository: TecnicoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TecnicoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getTecnico()
    }

    fun onEvent(event: TecnicoEvent) {
        when (event) {
            is TecnicoEvent.TecnicoChange -> onTecnicoChange(event.tecnicoId)
            is TecnicoEvent.NombreChange -> onNombreChange(event.nombre)
            is TecnicoEvent.SueldoHoraChange -> onSueldoHoraChange(event.sueldoHora)
            TecnicoEvent.Delete -> deleteTecnico()
            TecnicoEvent.New -> new()
            TecnicoEvent.Save -> saveTecnico()
        }
    }

    private fun onSueldoHoraChange(sueldoHora: Float?) {
        _uiState.update {
            it.copy(
                sueldoHora = sueldoHora
            )
        }
    }

    private fun onNombreChange(nombre: String) {
        _uiState.update {
            it.copy(
                nombre = nombre
            )
        }
    }

    private fun onTecnicoChange(tecnicoId: Int) {
        _uiState.update {
            it.copy(
                tecnicoId = tecnicoId
            )
        }
    }

    private fun getTecnico() {
        viewModelScope.launch {
            tecnicoRepository.getAll().collect { tecnicos ->
                _uiState.update {
                    it.copy(
                        tecnicos = tecnicos
                    )
                }
            }
        }
    }

    private fun new() {
        _uiState.update {
            it.copy(
                tecnicoId = null,
                nombre = "",
                sueldoHora = 0.00f,
                nombreErrorMessage = null,
                sueldoErrorMessage = null
            )
        }
    }

    fun saveTecnico() {
        viewModelScope.launch {
            if (_uiState.value.nombre.isNullOrBlank()) {
                _uiState.update {
                    it.copy(
                        nombreErrorMessage = "El nombre esta vacio"
                    )
                }

            } else {
                if (_uiState.value.sueldoHora!! < 0.0f) {
                    _uiState.update {
                        it.copy(
                            sueldoErrorMessage = "El sueldo no puede ser menor a cero"
                        )
                    }
                } else {
                    tecnicoRepository.save(_uiState.value.toEntity())
                }
            }
        }
    }

    fun getTecnicoById(tecnicoId: Int) {
        viewModelScope.launch {
            if (tecnicoId > 0) {
                val tecnico = tecnicoRepository.find(tecnicoId)
                _uiState.update {
                    it.copy(
                        tecnicoId = tecnico?.tecnicoId,
                        nombre = tecnico?.nombre,
                        sueldoHora = tecnico?.sueldoHora
                    )
                }
            }

        }
    }

    fun deleteTecnico() {
        viewModelScope.launch {
            tecnicoRepository.delete(_uiState.value.toEntity())
        }
    }

    fun TecnicoUiState.toEntity() = TecnicoEntity(
        tecnicoId = tecnicoId,
        nombre = nombre ?: "",
        sueldoHora = sueldoHora ?: 0.0f
    )

}

