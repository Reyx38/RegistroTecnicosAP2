package edu.ucne.registrotecnicosap2.presentation.tecnicos

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
import edu.ucne.registrotecnicosap2.Data.Entities.TecnicoEntity
import edu.ucne.registrotecnicosap2.presentation.navigation.Screen
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecnicoListScreen(
    tecnicoList: List<TecnicoEntity?>,
    onEdit: (Int?) -> Unit,
    onDelete: (TecnicoEntity) -> Unit,
    onNavigateToPrioridades: () -> Unit,
    onNavigateToTickets: () -> Unit,
    navController: NavController?
) {
    var showDialog by remember { mutableStateOf(false) }
    var tecnicoAEliminar by remember { mutableStateOf<TecnicoEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Lista de técnicos",
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
                    onClick = onNavigateToPrioridades,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Ir a Prioridades")
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
                    items(tecnicoList) { tecnico ->
                        TecnicoRow(
                            tecnico = tecnico,
                            onEdit = { onEdit(tecnico?.tecnicoId) },
                            onDelete = {
                                tecnicoAEliminar = tecnico
                                showDialog = true
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // Diálogo de confirmación
        if (showDialog && tecnicoAEliminar != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirmar eliminación") },
                text = { Text("¿Estás seguro de que deseas eliminar a ${tecnicoAEliminar?.nombre}?") },
                confirmButton = {
                    TextButton(onClick = {
                        tecnicoAEliminar?.let { onDelete(it) }
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
    onDelete: (TecnicoEntity?) -> Unit
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
                Text(text = "ID: ${tecnico?.tecnicoId}", style = MaterialTheme.typography.labelSmall)
                Text(text = tecnico?.nombre ?: "", style = MaterialTheme.typography.titleMedium)
                Text(text = formatoMoneda.format(tecnico?.sueldoHora), style = MaterialTheme.typography.bodyMedium)
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
                IconButton(onClick = { onDelete(tecnico) }) {
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

    TecnicoListScreen(
        tecnicoList = tecnico,
        onEdit = {},
        onDelete = {},
        onNavigateToPrioridades = {},
        onNavigateToTickets = {},
        navController = null
    )
}
