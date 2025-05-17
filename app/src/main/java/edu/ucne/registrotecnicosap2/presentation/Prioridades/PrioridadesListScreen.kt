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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadesListScreen(
    prioridadesList: List<PrioridadEntity?>,
    onEdit: (Int?) -> Unit,
    onDelete: (PrioridadEntity) -> Unit,
    navController: NavController?
) {
    var showDialog by remember { mutableStateOf(false) }
    var prioridadByEliminar by remember { mutableStateOf<PrioridadEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Prioridades") },
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
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
                }
            }
        }

        if (showDialog && prioridadByEliminar != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirmar Eliminación") },
                text = { Text("¿Estás seguro de que deseas eliminar la prioridad ${prioridadByEliminar?.descripcion}?") },
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
fun PrioridadRow(
    prioridad: PrioridadEntity?,
    onEdit: (Int?) -> Unit,
    onDelete: (PrioridadEntity?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp) // ya tenemos horizontal en LazyColumn
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween, // ⬅️ Distribuye elementos
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(text = "Id: ${prioridad?.prioridadId}")
                Text(text = "${prioridad?.descripcion}")
            }

            Row {
                IconButton(onClick = { onEdit(prioridad?.prioridadId) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = { onDelete(prioridad) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
        HorizontalDivider()
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
        navController = null
    )
}
