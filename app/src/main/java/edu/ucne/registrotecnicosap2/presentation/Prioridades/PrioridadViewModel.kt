package edu.ucne.registrotecnicosap2.presentation.Prioridades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.registrotecnicosap2.Data.Entities.PrioridadEntity
import edu.ucne.registrotecnicosap2.Data.repository.PrioridadRepository
import kotlinx.coroutines.launch

class PrioridadViewModel(
    private val prioridadRepository: PrioridadRepository
): ViewModel(){

    fun savePrioriddes(prioridad: PrioridadEntity){
        viewModelScope.launch{
            prioridadRepository.save(prioridad)
        }
    }
    fun getPrioridadById(id: Int?, onResult: (PrioridadEntity?) -> Unit) {
        viewModelScope.launch {
            val tecnico = prioridadRepository.find(id)
            onResult(tecnico)
        }
    }
    fun deletePrioridad(prioridad: PrioridadEntity){
        viewModelScope.launch {
            prioridadRepository.delete(prioridad)
        }
    }

}