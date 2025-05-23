package edu.ucne.registrotecnicosap2.presentation.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import edu.ucne.registrotecnicosap2.Data.Entities.TicketEntity
import edu.ucne.registrotecnicosap2.Data.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject


class TicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TicketUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getTicket()
    }

    fun onEvent(event: TicketEvent) {
        when (event) {
            TicketEvent.Deleta -> deleteTicket()
            TicketEvent.New -> newTicket()
            TicketEvent.Save -> saveTicket()
            is TicketEvent.asuntoChage -> onAsuntoChange(event.asunto)
            is TicketEvent.clienteChage -> onClienteChange(event.cliente)
            is TicketEvent.descripcionChage -> onDescripcionChange(event.descripcion)
            is TicketEvent.fechaChange -> onFechaChange(event.fecha)
            is TicketEvent.prioridadIdChage -> onPrioridadIdChange(event.prioridadId)
            is TicketEvent.tecnicoIdChage -> onTecnicoIdChange(event.tecnicoId)
            is TicketEvent.ticketIdChage -> onTicketChange(event.ticketId)
        }
    }

    private fun getTicket() {
        viewModelScope.launch {
            ticketRepository.getAll().collect { tickets ->
                _uiState.update {
                    it.copy(
                        tickets = tickets
                    )
                }
            }
        }
    }

    private fun onAsuntoChange(asunto: String?) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    asunto = asunto
                )
            }
        }
    }

    private fun onClienteChange(cliente: String?) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    cliente = cliente
                )
            }
        }
    }

    private fun onDescripcionChange(descripcion: String?) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    descripcion = descripcion
                )
            }
        }
    }

    private fun onFechaChange(fecha: Date?) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    fecha = fecha
                )
            }
        }
    }

    private fun onPrioridadIdChange(prioridadId: Int?) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    prioridadId = prioridadId
                )
            }
        }
    }

    private fun onTecnicoIdChange(tecnicoId: Int?) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    tecnicoId = tecnicoId
                )
            }
        }
    }

    private fun onTicketChange(ticketId: Int?) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    ticketId = ticketId
                )
            }
        }
    }

    fun newTicket(){
        _uiState.update {
            it.copy(
                ticketId = null,
                prioridadId = null,
                tecnicoId = null,
                asunto = " ",
                cliente = " ",
                descripcion = " ",
                fecha = null
            )
        }
    }

    fun saveTicket() {
        viewModelScope.launch {
            if(_uiState.value.asunto.isNullOrBlank())
            {
                _uiState.update {
                    it.copy(
                        asuntoErrorMensaje = "El asunto no puede estar vacio"
                    )
                }
            }else{
                if(_uiState.value.cliente.isNullOrBlank()){
                    _uiState.update {
                        it.copy(
                            clienteErrorMensaje = "El cliente no puede estar vacio"
                        )
                    }
                }else
                {
                    if (_uiState.value.descripcion.isNullOrBlank()){
                        _uiState.update {
                            it.copy(
                                descripcionErrorMensaje = "La descripcion no puede estar vacio"
                            )
                        }
                    }else
                        ticketRepository.save(_uiState.value.toEntity())

                }
            }
        }
    }

    fun getTicketById(id : Int?) {
        viewModelScope.launch {
            if (id != 0) {
                val ticket = ticketRepository.find(id)
                _uiState.update {
                    it.copy(
                        ticketId = ticket?.ticketId,
                        asunto = ticket?.asunto ?: " ",
                        descripcion = ticket?.descripcion ?: " ",
                        fecha = ticket?.fecha,
                        cliente = ticket?.cliente ?: " ",
                        prioridadId = ticket?.prioridadId,
                        tecnicoId = ticket?.tecnicoId
                    )
                }
            }
        }
    }

    fun deleteTicket() {
        viewModelScope.launch {
            ticketRepository.delete(_uiState.value.toEntity())
        }
    }

    fun TicketUiState.toEntity() = TicketEntity(
        ticketId = ticketId,
        asunto = asunto ?: " ",
        descripcion = descripcion ?: " ",
        fecha = fecha,
        cliente = cliente ?: " ",
        prioridadId = prioridadId,
        tecnicoId = tecnicoId
    )

}