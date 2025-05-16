package edu.ucne.registrotecnicosap2.presentation.tecnicos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.registrotecnicosap2.Data.Entities.TecnicoEntity
import edu.ucne.registrotecnicosap2.Data.repository.TecnicoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TecnicosViewModel(
    private val tecnicoRepository: TecnicoRepository
): ViewModel() {

    fun saveTecnico(tecnico: TecnicoEntity){
        viewModelScope.launch {
            tecnicoRepository.save(tecnico)
        }
    }

    fun getTecnicoById(id: Int, onResult: (TecnicoEntity?) -> Unit) {
        viewModelScope.launch {
            val tecnico = tecnicoRepository.find(id)
            onResult(tecnico)
        }
    }
    fun deleteTecnico(tecnico: TecnicoEntity){
        viewModelScope.launch {
            tecnicoRepository.delete(tecnico)
        }
    }

}