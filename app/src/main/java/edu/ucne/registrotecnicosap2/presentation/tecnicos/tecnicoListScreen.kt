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
import edu.ucne.registrotecnicosap2.Data.Entities.TecnicoEntity
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecnicoListScreen(
    tecnicoList: List<TecnicoEntity>,
    onEdit: (Int?) -> Unit,
    onDelete: (TecnicoEntity) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var tecnicoAEliminar by remember { mutableStateOf<TecnicoEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de técnicos") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEdit(0) }) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Nuevo")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(tecnicoList) { tecnico ->
                    TecnicoRow(
                        tecnico = tecnico,
                        onEdit = { onEdit(tecnico.tecnicoId) },
                        onDelete = {
                            tecnicoAEliminar = tecnico
                            showDialog = true
                        }
                    )
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
    tecnico: TecnicoEntity,
    onEdit: (Int?) -> Unit,
    onDelete: (TecnicoEntity) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = tecnico.nombre)
            Text(text = "Id: ${tecnico.tecnicoId}")
        }

        val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "DO"))
        Text(
            modifier = Modifier.weight(1f),
            text = formatoMoneda.format(tecnico.sueldoHora)
        )

        IconButton(onClick = { onEdit(tecnico.tecnicoId) }) {
            Icon(Icons.Default.Edit, contentDescription = "Editar")
        }

        IconButton(onClick = { onDelete(tecnico) }) {
            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
        }
    }
    HorizontalDivider()
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
        onDelete = {}
    )
}
