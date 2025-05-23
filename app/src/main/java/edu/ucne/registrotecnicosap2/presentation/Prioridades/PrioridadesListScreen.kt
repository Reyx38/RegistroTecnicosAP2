package edu.ucne.registrotecnicosap2.presentation.Prioridades

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicosap2.Data.Entities.PrioridadEntity


@Composable
fun PrioridadesListScreen(
    viewModel: PrioridadViewModel = hiltViewModel(),
    onEdit: (Int?) -> Unit,
    createPrioridad: () -> Unit
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PrioridadesListBodyScreen(
        uiState,
        createPrioridad,
        onEdit,
        onDelete = {viewModel.onEvent(PrioridadEvent.Delete)}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadesListBodyScreen(
    uiState: PrioridadUiState,
    createPrioridad: () -> Unit,
    onEdit: (Int?) -> Unit,
    onDelete: (PrioridadEntity) -> Unit,
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
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { createPrioridad }) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Nuevo")
            }
        },

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
                    items(uiState.prioridades) {
                        PrioridadRow(
                            it,
                            onEdit,
                            onDelete = {
                                prioridadByEliminar = it
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
    onDelete: () -> Unit
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
                IconButton(onClick = { onDelete() }) {
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

}
