package edu.ucne.registrotecnicosap2.presentation.ticket

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
    onNavigationToPrioridad: () -> Unit,
    navController: NavController? = null
) {
    var showDialog by remember { mutableStateOf(false) }
    var ticketByEliminar by remember { mutableStateOf<TicketEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Lista de tickets",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
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
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onNavigationToTecnico,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Ir a Técnicos")
                }
                Button(
                    onClick = onNavigationToPrioridad,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Ir a Prioridades")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .padding(12.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(ticketList) { ticket ->
                        val tecnicoNombre =
                            tecnicoList.find { it?.tecnicoId == ticket?.tecnicoId }?.nombre
                                ?: "Desconocido"
                        val prioridadNombre =
                            prioridadList.find { it?.prioridadId == ticket?.prioridadId }?.descripcion
                                ?: "Desconocido"

                        TicketRow(
                            ticket = ticket,
                            tecnicoNombre = tecnicoNombre,
                            prioridadNombre = prioridadNombre,
                            onEdit = { onEdit(ticket?.ticketId) },
                            onDelete = {
                                ticketByEliminar = ticket
                                showDialog = true
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // Diálogo de confirmación
        if (showDialog && ticketByEliminar != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirmar eliminación") },
                text = { Text("¿Estás seguro de que deseas eliminar el ticket \"${ticketByEliminar?.asunto}\"?") },
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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(2f)) {
                    Text(
                        text = "ID: ${ticket?.ticketId}",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = ticket?.asunto ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onEdit(ticket?.ticketId) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { onDelete(ticket) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "Descripción: ",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    overflow = TextOverflow.Ellipsis

                )
                Text(
                    text = ticket?.descripcion ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    Text(
                        text = "Fecha:",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = dateFormatted,
                        style = MaterialTheme.typography.bodySmall
                    )
                }


                Column {
                    Text(
                        text = "Técnico:",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = tecnicoNombre,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Column {
                    Text(
                        text = "Prioridad:",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = prioridadNombre,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Cliente
                Column {
                    Text(
                        text = "Cliente:",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = ticket?.cliente ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
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


