package edu.ucne.registrotecnicosap2.presentation.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import edu.ucne.registrotecnicosap2.Data.Entities.TicketEntity
import edu.ucne.registrotecnicosap2.Data.repository.TicketRepository
import kotlinx.coroutines.launch

class TicketViewModel(
    private val ticketRepository: TicketRepository
) : ViewModel() {

    fun saveTicket(ticket: TicketEntity) {
        viewModelScope.launch {
            ticketRepository.save(ticket)

        }
    }

    fun getTicketById(id: Int, onResult: (TicketEntity?) -> Unit) {
        viewModelScope.launch {
            val ticket = ticketRepository.find(id)
            onResult(ticket)
        }
    }

    fun deleteTicket(ticket: TicketEntity) {
        viewModelScope.launch {
            ticketRepository.delete(ticket)
        }
    }
}