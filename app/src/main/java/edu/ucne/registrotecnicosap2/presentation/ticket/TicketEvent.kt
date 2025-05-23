package edu.ucne.registrotecnicosap2.presentation.ticket

import java.util.Date

sealed interface TicketEvent {
    data class ticketIdChage(val ticketId: Int?) : TicketEvent
    data class clienteChage(val cliente: String?) : TicketEvent
    data class asuntoChage(val asunto: String?) : TicketEvent
    data class descripcionChage(val descripcion: String?) : TicketEvent
    data class fechaChange(val fecha: Date) : TicketEvent
    data class prioridadIdChage(val prioridadId: Int?) : TicketEvent
    data class tecnicoIdChage(val tecnicoId: Int?) : TicketEvent
    data object Save : TicketEvent
    data object New : TicketEvent
    data object Deleta : TicketEvent
}