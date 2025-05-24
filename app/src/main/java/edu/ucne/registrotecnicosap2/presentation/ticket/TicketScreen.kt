package edu.ucne.registrotecnicosap2.presentation.ticket

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicosap2.Data.Entities.PrioridadEntity
import edu.ucne.registrotecnicosap2.Data.Entities.TecnicoEntity
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TicketScreen(
    viewModel: TicketViewModel = hiltViewModel(),
    ticketId: Int? = null,
    goBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TicketBodyScreen(
        uiState,
        viewModel::onEvent,
        goBack
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketBodyScreen(
    uiState: TicketUiState,
    onEvent: (TicketEvent) -> Unit,
    goBack: () -> Unit
) {

    var tecnicoSeleccionado by remember { mutableStateOf<TecnicoEntity?>(null) }
    var prioridadSeleccionada by remember { mutableStateOf<PrioridadEntity?>(null) }

    var expandedTecnico by remember { mutableStateOf(false) }
    var expandedPrioridad by remember { mutableStateOf(false) }

    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val fechaFormateada = remember(uiState.fecha) { dateFormat.format(uiState.fecha!!) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.ticketId != null ) "Editar ticket" else "Registrar ticket",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .wrapContentHeight()
                    .padding(top = 12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 24.dp)
                    ) {
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = if (uiState.ticketId != null) "Editar ticket" else "Nuevo ticket",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }

                    OutlinedTextField(
                        value = fechaFormateada,
                        onValueChange = { },
                        label = { Text("Fecha") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        readOnly = true,
                        enabled = false,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Fecha"
                            )
                        }
                    )

                    OutlinedTextField(
                        value = uiState.asunto!!,
                        onValueChange = {onEvent(TicketEvent.asuntoChage(it)) },
                        label = { Text("Asunto") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                    )

                    OutlinedTextField(
                        value = uiState.cliente!!,
                        onValueChange = { onEvent(TicketEvent.clienteChage(it)) },
                        label = { Text("Cliente") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                    )

                    OutlinedTextField(
                        value = uiState.descripcion!!,
                        onValueChange = {onEvent(TicketEvent.descripcionChage(it))},
                        label = { Text("Descripción") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        minLines = 3,
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandedTecnico,
                        onExpandedChange = { expandedTecnico = !expandedTecnico },
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        OutlinedTextField(
                            value = tecnicoSeleccionado?.nombre ?: "Seleccionar técnico",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Técnico") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTecnico) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                        )
                        ExposedDropdownMenu(
                            expanded = expandedTecnico,
                            onDismissRequest = { expandedTecnico = false }
                        ) {
                            uiState.tecnicos.forEach { tecnico ->
                                DropdownMenuItem(
                                    text = { Text(text = "${tecnico?.nombre}") },
                                    onClick = {
                                        tecnicoSeleccionado = tecnico
                                        expandedTecnico = false
                                    }
                                )
                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = expandedPrioridad,
                        onExpandedChange = { expandedPrioridad = !expandedPrioridad },
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        OutlinedTextField(
                            value = prioridadSeleccionada?.descripcion ?: "Seleccionar prioridad",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Prioridad") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPrioridad) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                        )
                        ExposedDropdownMenu(
                            expanded = expandedPrioridad,
                            onDismissRequest = { expandedPrioridad = false }
                        ) {
                            uiState.prioridades.forEach { prioridad ->
                                DropdownMenuItem(
                                    text = { Text(text = "${prioridad?.descripcion}") },
                                    onClick = {
                                        prioridadSeleccionada = prioridad
                                        expandedPrioridad = false
                                    }
                                )
                            }
                        }
                    }

                    AnimatedVisibility(visible = uiState.descripcionErrorMensaje != null) {
                        Text(
                            text = uiState.descripcionErrorMensaje ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    AnimatedVisibility(visible = uiState.clienteErrorMensaje != null) {
                        Text(
                            text = uiState.clienteErrorMensaje ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    AnimatedVisibility(visible = uiState.asuntoErrorMensaje != null) {
                        Text(
                            text = uiState.asuntoErrorMensaje ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                    ) {
                        Button(
                            onClick = {
                                onEvent(TicketEvent.New)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Nuevo",
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                            Text("Limpiar")
                        }

                        Button(
                            onClick = {
                                onEvent(TicketEvent.Save)
                                goBack()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Guardar",
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview() {
    val tecnicosFake = listOf(
        TecnicoEntity(tecnicoId = 1, nombre = "Juan Pérez", sueldoHora = 300.0f),
        TecnicoEntity(tecnicoId = 2, nombre = "Ana Gómez", sueldoHora = 280.0f)
    )

    val prioridadesFake = listOf(
        PrioridadEntity(prioridadId = 1, descripcion = "Alta"),
        PrioridadEntity(prioridadId = 2, descripcion = "Media"),
        PrioridadEntity(prioridadId = 3, descripcion = "Baja")
    )

}
