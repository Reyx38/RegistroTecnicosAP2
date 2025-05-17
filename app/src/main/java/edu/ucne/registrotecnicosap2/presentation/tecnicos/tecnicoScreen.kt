package edu.ucne.registrotecnicosap2.presentation.tecnicos

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
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

@OptIn(ExperimentalMaterial3Api::class)
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
        if (tecnicoId != null && tecnicoId != 0 && viewModel != null) {
            viewModel.getTecnicoById(tecnicoId) { tecnico ->
                tecnico?.let {
                    nombre = it.nombre
                    sueldo = it.sueldoHora
                    editando = it
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (tecnicoId != null && tecnicoId != 0) "Editar técnico" else "Registrar técnico",
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
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight()
                    .padding(top = 32.dp),
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

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (tecnicoId != null && tecnicoId != 0) "Editar técnico" else "Nuevo técnico",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }

                    OutlinedTextField(
                        label = { Text("Nombre del técnico") },
                        value = nombre,
                        onValueChange = { nombre = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        singleLine = true,
                        isError = nombre.isBlank() && mensajeError != null
                    )

                    OutlinedTextField(
                        label = { Text("Sueldo por hora (RD$)") },
                        value = if (sueldo == 0.0f && editando == null) "" else sueldo.toString(),
                        onValueChange = { newValue ->
                            sueldo = newValue.toFloatOrNull() ?: 0.0f
                            if (mensajeError?.contains("sueldo") == true) {
                                mensajeError = null
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        isError = (sueldo <= 0) && mensajeError != null
                    )

                    // Mensaje de error
                    AnimatedVisibility(visible = mensajeError != null) {
                        Text(
                            text = mensajeError ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botones
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                    ) {
                        Button(
                            onClick = {
                                nombre = ""
                                sueldo = 0.0f
                                mensajeError = null
                                editando = null
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
                                if (nombre.isBlank()) {
                                    mensajeError = "El nombre no puede estar vacío."
                                    return@Button
                                }

                                if (sueldo <= 0.0f) {
                                    mensajeError = "El sueldo debe ser mayor que cero."
                                    return@Button
                                }

                                // Guardar
                                viewModel?.saveTecnico(
                                    TecnicoEntity(
                                        tecnicoId = editando?.tecnicoId,
                                        nombre = nombre,
                                        sueldoHora = sueldo
                                    )
                                )

                                navController?.navigateUp()
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
fun PreviewTecnicoScreen() {
        TecnicoScreen()

}
