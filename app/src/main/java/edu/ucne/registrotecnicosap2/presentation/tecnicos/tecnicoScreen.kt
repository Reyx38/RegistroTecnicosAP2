package edu.ucne.registrotecnicosap2.presentation.tecnicos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.ucne.registrotecnicosap2.Data.Entities.TecnicoEntity
import edu.ucne.registrotecnicosap2.ui.theme.RegistroTecnicosAP2Theme

@Composable
fun TecnicoScreen(
    tecnicoId: Int? = null,
    navController: NavController? = null,
    viewModel: TecnicosViewModel? = null,
) {
    var nombre by remember { mutableStateOf("") }
    var sueldo by remember { mutableFloatStateOf(0.0f) }
    var mensajeError by remember { mutableStateOf<String?>(null) }
    var editando by remember { mutableStateOf<TecnicoEntity?>(null) }

    LaunchedEffect(tecnicoId) {
        if (tecnicoId != null && viewModel != null) {
            viewModel.getTecnicoById(tecnicoId) { tecnico ->
                tecnico?.let {
                    nombre = it.nombre
                    sueldo = it.sueldoHora
                    editando = it
                }
            }
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
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
                        if (navController != null) {
                            IconButton(
                                onClick = { navController.popBackStack() },
                                modifier = Modifier.align(Alignment.CenterVertically)
                            ) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Registro de Técnicos",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        label = { Text("Nombre") },
                        value = nombre,
                        onValueChange = { nombre = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        label = { Text("Sueldo por hora") },
                        value = if (sueldo == 0.0f) "" else sueldo.toString(),
                        onValueChange = { newValue ->
                            sueldo = newValue.toFloatOrNull() ?: 0.0f
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    mensajeError?.let {
                        Text(text = it, color = Color.Red)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(onClick = {
                            nombre = ""
                            sueldo = 0.0f
                            mensajeError = null
                        }) {
                            Icon(Icons.Default.Add, contentDescription = "Nuevo")
                            Text("Nuevo")
                        }

                        OutlinedButton(
                            onClick = {
                                if (nombre.isBlank()) {
                                    mensajeError = "El nombre no puede estar vacío."
                                    return@OutlinedButton
                                }

                                if (sueldo <= 0.0) {
                                    mensajeError = "El sueldo no puede ser cero o menor."
                                    return@OutlinedButton
                                }

                                viewModel?.saveTecnico(
                                    TecnicoEntity(
                                        tecnicoId = editando?.tecnicoId,
                                        nombre = nombre,
                                        sueldoHora = sueldo
                                    )
                                )

                                navController?.navigateUp()
                            },
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Guardar"
                            )
                            Text(text = "Guardar")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewTecnicoScreen() {
    RegistroTecnicosAP2Theme {
        TecnicoScreen()
    }
}
