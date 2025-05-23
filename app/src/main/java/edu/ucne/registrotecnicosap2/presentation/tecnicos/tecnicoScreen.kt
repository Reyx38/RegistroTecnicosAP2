package edu.ucne.registrotecnicosap2.presentation.tecnicos

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicosap2.Data.Entities.TecnicoEntity


@Composable
fun TecnicoScreen(
    viewModel: TecnicosViewModel = hiltViewModel(),
    tecnicoId: Int?,
    goBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(tecnicoId) {
        tecnicoId?.let {
            if (it > 0) {
                viewModel.getTecnicoById(tecnicoId)
            }
        }
    }
    TecnicoBodyScreen(
        uiState = uiState,
        viewModel::onEvent,
        goBack = goBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecnicoBodyScreen(
    uiState: TecnicoUiState,
    onEvent: (TecnicoEvent) -> Unit,
    goBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.tecnicoId != 0) "Editar técnico" else "Registrar técnico",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { goBack }) {
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
                            text = if (uiState.tecnicoId != null && uiState.tecnicoId != 0) "Editar técnico" else "Nuevo técnico",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }

                    OutlinedTextField(
                        label = { Text("Nombre del técnico") },
                        value = uiState.nombre ?: "",
                        onValueChange = { onEvent(TecnicoEvent.NombreChange(it)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        singleLine = true,
                    )

                    OutlinedTextField(
                        label = { Text("Sueldo por hora (RD$)") },
                        value = uiState.sueldoHora.toString(),
                        onValueChange = {
                            onEvent(TecnicoEvent.SueldoHoraChange(it.toFloatOrNull()))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                    )

                    // Mensaje de error
                    AnimatedVisibility(visible = uiState.nombreErrorMessage != null) {
                        Text(
                            text = uiState.nombreErrorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    AnimatedVisibility(visible = uiState.sueldoErrorMessage != null) {
                        Text(
                            text = uiState.sueldoErrorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    // Botones
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        Button(
                            onClick = {
                                onEvent(TecnicoEvent.New)
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
                                onEvent(TecnicoEvent.Save)
                                goBack()
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
fun PreviewTecnicoScreenWithErrors() {
    val mockUiState = TecnicoUiState(
        tecnicoId = null,
        nombre = "",
        sueldoHora = 0.0f,
        nombreErrorMessage = "El nombre no puede estar vacío",
        sueldoErrorMessage = "El sueldo debe ser mayor que cero",
        tecnicos = emptyList()
    )

    TecnicoBodyScreen(
        uiState = mockUiState,
        onEvent = { /* Mock function */ },
        goBack = { /* Mock function */ },
    )

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewTecnicoScreenFilled() {
    val mockUiState = TecnicoUiState(
        tecnicoId = null,
        nombre = "Carlos Martínez",
        sueldoHora = 3200.0f,
        nombreErrorMessage = null,
        sueldoErrorMessage = null,
        tecnicos = emptyList()
    )

    TecnicoBodyScreen(
        uiState = mockUiState,
        onEvent = { /* Mock function */ },
        goBack = { /* Mock function */ },
    )

}

