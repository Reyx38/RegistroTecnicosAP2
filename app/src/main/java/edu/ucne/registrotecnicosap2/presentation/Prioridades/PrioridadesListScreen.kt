package edu.ucne.registrotecnicosap2.presentation.Prioridades

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.ucne.registrotecnicosap2.Data.Entities.PrioridadEntity
import edu.ucne.registrotecnicosap2.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadesListScreen(
    prioridadesList: List<PrioridadEntity?>,
    onEdit: (Int?) -> Unit,
    onDelete: (PrioridadEntity) -> Unit,
    onNavigateToTecnicos: () -> Unit,
    onNavigateToTickets: () -> Unit,
    navController: NavController?
) {
    var showDialog by remember { mutableStateOf(false) }
    var prioridadByEliminar by remember { mutableStateOf<PrioridadEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Lista de prioridades",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController?.navigate(Screen.Home) }) {
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
                    onClick = onNavigateToTecnicos,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Ir a Técnicos")
                }
                Button(
                    onClick = onNavigateToTickets,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Ir a Tickets")
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
                    items(prioridadesList) { prioridad ->
                        PrioridadRow(
                            prioridad = prioridad,
                            onEdit = { onEdit(prioridad?.prioridadId) },
                            onDelete = {
                                prioridadByEliminar = prioridad
                                showDialog = true
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // Diálogo de confirmación
        if (showDialog && prioridadByEliminar != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirmar eliminación") },
                text = { Text("¿Estás seguro de que deseas eliminar la prioridad \"${prioridadByEliminar?.descripcion}\"?") },
                confirmButton = {
                    TextButton(onClick = {
                        prioridadByEliminar?.let { onDelete(it) }
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
private fun PrioridadRow(
    prioridad: PrioridadEntity?,
    onEdit: (Int?) -> Unit,
    onDelete: (PrioridadEntity?) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(2f)) {
                Text(
                    text = "ID: ${prioridad?.prioridadId}",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = prioridad?.descripcion ?: "",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onEdit(prioridad?.prioridadId) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = { onDelete(prioridad) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewList() {
    val prioridades = listOf(
        PrioridadEntity(prioridadId = 1, descripcion = "Urgente"),
        PrioridadEntity(prioridadId = 2, descripcion = "Normal")
    )

    PrioridadesListScreen(
        prioridadesList = prioridades,
        onEdit = {},
        onDelete = {},
        navController = null,
        onNavigateToTecnicos = {},
        onNavigateToTickets = {}
    )
}
