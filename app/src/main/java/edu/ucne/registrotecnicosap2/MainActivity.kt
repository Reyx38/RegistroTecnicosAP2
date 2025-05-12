// MainActivity.kt
package edu.ucne.registrotecnicosap2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.Room
import edu.ucne.registrotecnicosap2.Data.Database.TecnicoDb
import edu.ucne.registrotecnicosap2.Data.Entities.TecnicoEntity
import edu.ucne.registrotecnicosap2.ui.theme.RegistroTecnicosAP2Theme
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

class MainActivity : ComponentActivity() {
    private lateinit var tecnicoDb: TecnicoDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        tecnicoDb = Room.databaseBuilder(
            applicationContext,
            TecnicoDb::class.java,
            "Tecnico.db"
        ).fallbackToDestructiveMigration().build()

        setContent {
            RegistroTecnicosAP2Theme {
                TecnicoScreen()
            }
        }
    }

    @Composable
    fun TecnicoScreen() {
        var nombre by remember { mutableStateOf("") }
        var sueldoTexto by remember { mutableStateOf("") }
        var sueldoHora by remember { mutableStateOf(0.0f) }
        var errorMensaje by remember { mutableStateOf<String?>(null) }

        var tecnicoEnEdicion by remember { mutableStateOf<TecnicoEntity?>(null) }
        var mostrarConfirmacion by remember { mutableStateOf(false) }
        var tecnicoAEliminar by remember { mutableStateOf<TecnicoEntity?>(null) }

        val scope = rememberCoroutineScope()
        val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
        val tecnicoList by tecnicoDb.tecnicoDao().getAll()
            .collectAsStateWithLifecycle(
                initialValue = emptyList(),
                lifecycleOwner = lifecycleOwner,
                minActiveState = Lifecycle.State.STARTED
            )

        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(bottom = 8.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Registro de Técnicos", style = MaterialTheme.typography.headlineSmall)
                    }
                }

                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        OutlinedTextField(
                            label = { Text("Nombre") },
                            value = nombre,
                            onValueChange = { nombre = it },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            label = { Text("Sueldo por hora") },
                            value = sueldoTexto,
                            onValueChange = {
                                sueldoTexto = it
                                sueldoHora = it.toFloatOrNull() ?: 0f
                            },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        errorMensaje?.let {
                            Text(text = it, color = Color.Red)
                        }

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Spacer(modifier = Modifier.width(25.dp))

                            OutlinedButton(onClick = {
                                nombre = ""
                                sueldoTexto = ""
                                sueldoHora = 0.0f
                                tecnicoEnEdicion = null
                                errorMensaje = null
                            }) {
                                Icon(Icons.Default.Add, contentDescription = "Nuevo")
                                Text("Nuevo")
                            }

                            Spacer(modifier = Modifier.width(85.dp))

                            OutlinedButton(onClick = {
                                if (nombre.isBlank() || sueldoTexto.isBlank()) {
                                    errorMensaje = "Hay datos vacíos"
                                    return@OutlinedButton
                                }

                                scope.launch {
                                    if (tecnicoEnEdicion != null) {
                                        editarTecnico(tecnicoEnEdicion!!.copy(nombre = nombre, sueldoHora = sueldoHora))
                                    } else {
                                        saveTecnico(TecnicoEntity(nombre = nombre, sueldoHora = sueldoHora))
                                    }
                                    nombre = ""
                                    sueldoTexto = ""
                                    sueldoHora = 0.0f
                                    tecnicoEnEdicion = null
                                }
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Guardar")
                                Text("Guardar")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Lista de Técnicos",
                    fontSize = 30.sp

                )
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(tecnicoList) { tecnico ->
                        TecnicoRow(
                            tecnico = tecnico,
                            onEditar = {
                                nombre = it.nombre
                                sueldoTexto = it.sueldoHora.toString()
                                sueldoHora = it.sueldoHora
                                tecnicoEnEdicion = it
                            },
                            onEliminar = {
                                tecnicoAEliminar = it
                                mostrarConfirmacion = true
                            }
                        )
                    }
                }
            }

            if (mostrarConfirmacion && tecnicoAEliminar != null) {
                AlertDialog(
                    onDismissRequest = { mostrarConfirmacion = false },
                    confirmButton = {
                        TextButton(onClick = {
                            scope.launch {
                                eliminarTecnico(tecnicoAEliminar!!)
                                mostrarConfirmacion = false
                                tecnicoAEliminar = null
                            }
                        }) {
                            Text("Sí, eliminar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            mostrarConfirmacion = false
                            tecnicoAEliminar = null
                        }) {
                            Text("Cancelar")
                        }
                    },
                    title = { Text("Confirmar eliminación") },
                    text = { Text("¿Estás seguro de que deseas eliminar este técnico?") }
                )
            }
        }
    }

    @Composable
    fun TecnicoRow(tecnico: TecnicoEntity, onEditar: (TecnicoEntity) -> Unit, onEliminar: (TecnicoEntity) -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(2f)) {
                Text("ID: ${tecnico.tecnicoId}")
                Text(tecnico.nombre)
            }

            Spacer(modifier = Modifier.weight(0.3f))
            val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "DO"))
            Text(modifier = Modifier.weight(3f), text = formatoMoneda.format(tecnico.sueldoHora))

            IconButton(onClick = { onEditar(tecnico) }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }

            Spacer(modifier = Modifier.weight(0.5f))

            IconButton(onClick = { onEliminar(tecnico) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
        HorizontalDivider()
    }

    private suspend fun saveTecnico(tecnico: TecnicoEntity) {
        tecnicoDb.tecnicoDao().save(tecnico)
    }

    private suspend fun editarTecnico(tecnico: TecnicoEntity) {
        tecnicoDb.tecnicoDao().save(tecnico)
    }

    private suspend fun eliminarTecnico(tecnico: TecnicoEntity) {
        tecnicoDb.tecnicoDao().delete(tecnico)
    }
}
