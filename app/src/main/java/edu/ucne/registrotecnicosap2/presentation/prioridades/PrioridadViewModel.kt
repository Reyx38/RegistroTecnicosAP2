package edu.ucne.registrotecnicosap2.presentation.prioridades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicosap2.Data.Entities.PrioridadEntity
import edu.ucne.registrotecnicosap2.Data.repository.PrioridadRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PrioridadViewModel @Inject constructor(
    private val prioridadRepository: PrioridadRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PrioridadUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getPrioridades()
    }

    fun onEvent(event: PrioridadEvent) {
        when (event) {
            is PrioridadEvent.PrioridadIdChange -> onPrioridadIdChange(event.prioridadId)
            is PrioridadEvent.DescripcionChange -> onDescripcionChange(event.descripcion)
            PrioridadEvent.New -> newPrioridades()
            PrioridadEvent.Save -> savePrioriddes()
            PrioridadEvent.Delete -> deletePrioridad()

        }
    }

    private fun onDescripcionChange(descripcion: String?) {
        _uiState.update {
            it.copy(
                descripcion = descripcion
            )
        }
    }

    private fun onPrioridadIdChange(id: Int?) {
        _uiState.update {
            it.copy(
                prioridadId = id
            )
        }

    }

    private fun newPrioridades() {
        _uiState.update {
            it.copy(
                prioridadId = null,
                descripcion = ""
            )
        }
    }

    fun savePrioriddes() {
        viewModelScope.launch {
            if (_uiState.value.descripcion!!.isBlank()) {
                _uiState.update {
                    it.copy(
                        descripcionErrorMensaje = "La descripcion no puede estar vacia"
                    )
                }
            } else {
                prioridadRepository.save(_uiState.value.toEntity())
            }
        }
    }

    fun getPrioridadById(id: Int?) {
        viewModelScope.launch {
            if (id != null) {
                val prioridad = prioridadRepository.find(id)
                _uiState.update {
                    it.copy(
                        prioridadId = prioridad?.prioridadId,
                        descripcion = prioridad?.descripcion
                    )
                }
            }
        }
    }

    fun deletePrioridad() {
        viewModelScope.launch {
            prioridadRepository.delete(_uiState.value.toEntity())
        }
    }

    private fun getPrioridades() {
        viewModelScope.launch {
            prioridadRepository.getAll().collect { priooridad ->
                _uiState.update {
                    it.copy(
                        prioridades = priooridad
                    )
                }
            }
        }
    }

    fun PrioridadUiState.toEntity() = PrioridadEntity(
        prioridadId = prioridadId,
        descripcion = descripcion ?: ""
    )
}