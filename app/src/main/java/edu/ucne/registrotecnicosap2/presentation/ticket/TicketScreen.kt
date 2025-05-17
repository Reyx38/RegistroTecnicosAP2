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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun TicketScreen(
    ticketId: Int? = null,
    tecnicos: List<TecnicoEntity?>,
    prioridades: List<PrioridadEntity?>,
    navController: NavController? = null,
    viewModel: TicketViewModel?
) {
    var asunto by remember { mutableStateOf("") }
    var cliente by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    val fechaActual by remember { mutableStateOf(Date()) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    var tecnicoSeleccionado by remember { mutableStateOf<TecnicoEntity?>(null) }
    var prioridadSeleccionada by remember { mutableStateOf<PrioridadEntity?>(null) }

    var expandedTecnico by remember { mutableStateOf(false) }
    var expandedPrioridad by remember { mutableStateOf(false) }

    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val fechaFormateada = remember(fechaActual) { dateFormat.format(fechaActual) }

    LaunchedEffect(ticketId) {
        if (ticketId != null) {
            viewModel?.getTicketById(ticketId) { ticket ->
                ticket?.let {
                    asunto = it.asunto
                    cliente = it.cliente
                    descripcion = it.descripcion
                    tecnicoSeleccionado = tecnicos.find { t -> t?.tecnicoId == it.tecnicoId }
                    prioridadSeleccionada =
                        prioridades.find { p -> p?.prioridadId == it.prioridadId }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (ticketId != null && ticketId != 0) "Editar ticket" else "Registrar ticket",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
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
                            text = if (ticketId != null && ticketId != 0) "Editar ticket" else "Nuevo ticket",
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
                        value = asunto,
                        onValueChange = { asunto = it },
                        label = { Text("Asunto") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        isError = asunto.isBlank() && mensajeError != null
                    )

                    OutlinedTextField(
                        value = cliente,
                        onValueChange = { cliente = it },
                        label = { Text("Cliente") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        isError = cliente.isBlank() && mensajeError != null
                    )

                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        minLines = 3,
                        isError = descripcion.isBlank() && mensajeError != null
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
                            isError = tecnicoSeleccionado == null && mensajeError != null
                        )
                        ExposedDropdownMenu(
                            expanded = expandedTecnico,
                            onDismissRequest = { expandedTecnico = false }
                        ) {
                            tecnicos.forEach { tecnico ->
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
                            isError = prioridadSeleccionada == null && mensajeError != null
                        )
                        ExposedDropdownMenu(
                            expanded = expandedPrioridad,
                            onDismissRequest = { expandedPrioridad = false }
                        ) {
                            prioridades.forEach { prioridad ->
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

                    AnimatedVisibility(visible = mensajeError != null) {
                        Text(
                            text = mensajeError ?: "",
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
                                asunto = ""
                                cliente = ""
                                descripcion = ""
                                tecnicoSeleccionado = null
                                prioridadSeleccionada = null
                                mensajeError = null
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
                                when {
                                    asunto.isBlank() -> {
                                        mensajeError = "El asunto no puede estar vacío."
                                        return@Button
                                    }
                                    cliente.isBlank() -> {
                                        mensajeError = "El cliente no puede estar vacío."
                                        return@Button
                                    }
                                    descripcion.isBlank() -> {
                                        mensajeError = "La descripción no puede estar vacía."
                                        return@Button
                                    }
                                    tecnicoSeleccionado == null -> {
                                        mensajeError = "Debe seleccionar un técnico."
                                        return@Button
                                    }
                                    prioridadSeleccionada == null -> {
                                        mensajeError = "Debe seleccionar una prioridad."
                                        return@Button
                                    }
                                }

                                // Guardar
                                val nuevoTicket = TicketEntity(
                                    fecha = fechaActual,
                                    prioridadId = prioridadSeleccionada!!.prioridadId,
                                    cliente = cliente,
                                    asunto = asunto,
                                    descripcion = descripcion,
                                    tecnicoId = tecnicoSeleccionado!!.tecnicoId
                                )
                                viewModel?.saveTicket(nuevoTicket)
                                navController?.popBackStack()
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
    TicketScreen(
        ticketId = null,
        tecnicos = tecnicosFake,
        prioridades = prioridadesFake,
        navController = null,
        viewModel = null
    )
}
