package edu.ucne.registrotecnicosap2.presentation.ticket

import edu.ucne.registrotecnicosap2.Data.Entities.TicketEntity
import java.util.Date

data class TicketUiState (
    val ticketId: Int? = null,
    val fecha: Date? = Date(),
    val prioridadId: Int? = null,
    val cliente: String? = " ",
    val clienteErrorMensaje: String? = "",
    val asunto: String? = " ",
    val asuntoErrorMensaje: String? = "",
    val descripcion: String? = " ",
    val descripcionErrorMensaje: String? = "",
    val tecnicoId: Int? = null,
    val tickets: List<TicketEntity?> = emptyList()
)
