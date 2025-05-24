package edu.ucne.registrotecnicosap2.presentation.tecnicos

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
import edu.ucne.registrotecnicosap2.Data.Entities.TecnicoEntity
import java.text.NumberFormat
import java.util.*


@Composable
fun TecnicoListScreen(
    viewModel: TecnicosViewModel = hiltViewModel(),
    createTecnico: () -> Unit,
    onEditTecnico: (Int?) -> Unit,

    ) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TecnicoListBodyScreen(
        uiState,
        createTecnico,
        onEditTecnico,
        onDeleteTecnico = { tecnico -> viewModel.onEvent(TecnicoEvent.Delete)
        }

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecnicoListBodyScreen(
    uiState: TecnicoUiState,
    createTecnico: () -> Unit,
    onEditTecnico: (Int?) -> Unit,
    onDeleteTecnico: (TecnicoEntity) -> Unit,

) {
    var showDialog by remember { mutableStateOf(false) }
    var tecnicoDelete by remember { mutableStateOf<TecnicoEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Lista de técnicos",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = createTecnico) {
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
                    items(uiState.tecnicos) {
                        TecnicoRow(
                            it,
                            onEditTecnico,
                            onDelete = {
                                tecnicoDelete = it
                                showDialog = true
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // Diálogo de confirmación
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirmar eliminación") },
                text = { Text("¿Estás seguro de que deseas eliminar a $?") },
                confirmButton = {
                    TextButton(onClick = {
                        tecnicoDelete?.let { onDeleteTecnico(it) }
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
private fun TecnicoRow(
    tecnico: TecnicoEntity?,
    onEdit: (Int?) -> Unit,
    onDelete: () -> Unit
) {
    val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "DO"))

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
                    text = "ID: ${tecnico?.tecnicoId}",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(text = tecnico?.nombre ?: "", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = formatoMoneda.format(tecnico?.sueldoHora),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onEdit(tecnico?.tecnicoId) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDelete) {
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
    val tecnico = listOf(
        TecnicoEntity(
            tecnicoId = 1,
            nombre = "Reyphill Nuñez",
            sueldoHora = 2500f
        ),
        TecnicoEntity(
            tecnicoId = 2,
            nombre = "Darvyn Luis",
            sueldoHora = 2300f
        )
    )
    val mockUiState = TecnicoUiState(
        tecnicos = tecnico
    )
    TecnicoListBodyScreen(
        uiState = mockUiState,
        createTecnico = { /* Mock function */ },
        onEditTecnico = { /* Mock function */ },
        onDeleteTecnico = { /* Mock function */ }
    )

}
