package edu.ucne.registrotecnicosap2.presentation.ticket

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.ucne.registrotecnicosap2.Data.Entities.PrioridadEntity
import edu.ucne.registrotecnicosap2.Data.Entities.TecnicoEntity
import edu.ucne.registrotecnicosap2.Data.Entities.TicketEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(
    ticketList: List<TicketEntity?>,
    tecnicoList: List<TecnicoEntity?>,
    prioridadList: List<PrioridadEntity?>,
    onEdit: (Int?) -> Unit,
    onDelete: (TicketEntity) -> Unit,
    onNavigationToTecnico: () -> Unit,
    onNavigationToPrioridad: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var ticketByEliminar by remember { mutableStateOf<TicketEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Tickets") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEdit(0) }) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Nuevo")
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = onNavigationToTecnico) {
                    Text("Ir a Tecnicos")
                }
                Button(onClick = onNavigationToPrioridad) {
                    Text("Ir a Prioridades")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(ticketList) { ticket ->
                    TicketRow(
                        ticket = ticket,
                        tecnicoNombre =  tecnicoList.find
                        { it?.tecnicoId == ticket?.tecnicoId }?.nombre ?: "Desconocido",
                        prioridadNombre =  prioridadList.find
                        { it?.prioridadId == ticket?.prioridadId }?.descripcion ?: "Desconocido",
                        onEdit = { onEdit(ticket?.ticketId) },
                        onDelete = {
                            ticketByEliminar = ticket
                            showDialog = true
                        }
                    )
                }
            }
        }
        if (showDialog && ticketByEliminar != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirmar eliminación") },
                text = { Text("¿Estás seguro de que deseas eliminar a ${ticketByEliminar?.asunto}?") },
                confirmButton = {
                    TextButton(onClick = {
                        ticketByEliminar?.let { onDelete(it) }
                        showDialog = false
                    }) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
@Composable
fun TicketRow(
    ticket: TicketEntity?,
    tecnicoNombre: String,
    prioridadNombre: String,
    onEdit: (Int?) -> Unit,
    onDelete: (TicketEntity?) -> Unit
) {
    val dateFormatted = try {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        formatter.format(ticket?.fecha)
    } catch (e: Exception) {
        "Fecha inválida"
    }

    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.weight(1.2f)) {
            Text(text = "ID: ${ticket?.ticketId}", maxLines = 1)
            Text(text = "${ticket?.asunto}", maxLines = 2, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
            Text(text = "Fecha: $dateFormatted", maxLines = 1)
        }

        Column(modifier = Modifier.weight(1.5f)) {
            Text(text = "Descripción:", maxLines = 1)
            Text(
                text = "${ticket?.descripcion}",
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }

        Column(modifier = Modifier.weight(1.2f)) {
            Text(text = "Técnico:", maxLines = 1)
            Text(text = tecnicoNombre, maxLines = 1)
        }

        Column(modifier = Modifier.weight(1.2f)) {
            Text(text = "Prioridad:", maxLines = 1)
            Text(text = prioridadNombre, maxLines = 1)
        }

        Column(modifier = Modifier.weight(1.2f)) {
            Text(text = "Cliente:", maxLines = 1)
            Text(
                text = "${ticket?.cliente}",
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IconButton(onClick = { onEdit(ticket?.ticketId) }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }

            IconButton(onClick = { onDelete(ticket) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }

    HorizontalDivider()
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewTicketListScreen() {
    val sampleTickets = listOf(
        TicketEntity(
            ticketId = 1,
            asunto = "Error en impresora",
            descripcion = "No imprime en negro",
            tecnicoId = 1,
            prioridadId = 1,
            cliente = "Juan Pérez",
            fecha = Date()
        ),
        TicketEntity(
            ticketId = 2,
            asunto = "Computadora lenta",
            descripcion = "La computadora tarda en iniciar",
            tecnicoId = 2,
            prioridadId = 2,
            cliente = "María Gómez",
            fecha = Date()
        )
    )

    val sampleTecnicos = listOf(
        TecnicoEntity(tecnicoId = 1, nombre = "Carlos Ramírez", sueldoHora = 200.0f),
        TecnicoEntity(tecnicoId = 2, nombre = "Laura Sánchez", sueldoHora = 180.0f)
    )

    val samplePrioridades = listOf(
        PrioridadEntity(prioridadId = 1, descripcion = "Alta"),
        PrioridadEntity(prioridadId = 2, descripcion = "Media")
    )

    TicketListScreen(
        ticketList = sampleTickets,
        tecnicoList = sampleTecnicos,
        prioridadList = samplePrioridades,
        onEdit = {},
        onDelete = {},
        onNavigationToTecnico = {},
        onNavigationToPrioridad = {}
    )
}


