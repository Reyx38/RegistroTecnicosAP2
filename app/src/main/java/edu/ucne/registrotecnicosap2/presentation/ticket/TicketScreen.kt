package edu.ucne.registrotecnicosap2.presentation.ticket

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
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
import java.util.Date

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

    var tecnicoSeleccionado by remember { mutableStateOf<TecnicoEntity?>(null) }
    var prioridadSeleccionada by remember { mutableStateOf<PrioridadEntity?>(null) }

    var expandedTecnico by remember { mutableStateOf(false) }
    var expandedPrioridad by remember { mutableStateOf(false) }

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

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(
                        onClick = { navController?.popBackStack() },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Registro de Técnicos",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

            }

            OutlinedTextField(
                value = asunto,
                onValueChange = { asunto = it },
                label = { Text("Asunto") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = cliente,
                onValueChange = { cliente = it },
                label = { Text("Cliente") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Dropdown Técnico
            ExposedDropdownMenuBox(
                expanded = expandedTecnico,
                onExpandedChange = { expandedTecnico = !expandedTecnico }
            ) {
                OutlinedTextField(
                    value = tecnicoSeleccionado?.nombre ?: "Seleccionar técnico",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Técnico") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTecnico) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
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

            Spacer(Modifier.height(16.dp))

            // Dropdown Prioridad
            ExposedDropdownMenuBox(
                expanded = expandedPrioridad,
                onExpandedChange = { expandedPrioridad = !expandedPrioridad }
            ) {
                OutlinedTextField(
                    value = prioridadSeleccionada?.descripcion ?: "Seleccionar prioridad",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Prioridad") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPrioridad) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
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

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (tecnicoSeleccionado != null && prioridadSeleccionada != null) {
                        val nuevoTicket = TicketEntity(
                            fecha = Date(),
                            prioridadId = prioridadSeleccionada!!.prioridadId,
                            cliente = cliente,
                            asunto = asunto,
                            descripcion = descripcion,
                            tecnicoId = tecnicoSeleccionado!!.tecnicoId
                        )
                        viewModel?.saveTicket(nuevoTicket)
                        navController?.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Ticket")
            }
            Button(
                onClick = {
                    asunto = ""
                    cliente = ""
                    descripcion = ""
                    tecnicoSeleccionado = null
                    prioridadSeleccionada = null
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Nuevo Ticket")
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
