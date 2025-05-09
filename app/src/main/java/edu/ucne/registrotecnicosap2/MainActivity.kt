package edu.ucne.registrotecnicosap2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import edu.ucne.registrotecnicosap2.Data.Database.TecnicoDb
import edu.ucne.registrotecnicosap2.Data.Database.tecnicoDb_Impl
import edu.ucne.registrotecnicosap2.Data.Entities.TecnicoEntity
import edu.ucne.registrotecnicosap2.ui.theme.RegistroTecnicosAP2Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    //Instancia de la base de datos
    private lateinit var tecnicoDb: TecnicoDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        tecnicoDb = Room.databaseBuilder(
            applicationContext,
            TecnicoDb::class.java,
            "Tecnico.db"
        ).fallbackToDestructiveMigration()
            .build()

        setContent {
            RegistroTecnicosAP2Theme {
                TecnicoScreen()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun TecnicoScreen() {
        var Nombre: String by remember { mutableStateOf("") }
        var SueldoTexto: String by remember { mutableStateOf("") }
        var SueldoHora: Float by remember { mutableStateOf(0.0f) }
        var ErrorMensaje: String? by remember { mutableStateOf("") }

        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp)
            ) {
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        OutlinedTextField(
                            label = { Text(text = "Nombre") },
                            value = Nombre,
                            onValueChange = { Nombre = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            label = { Text(text = "Sueldo por hora") },
                            value = SueldoTexto,
                            onValueChange = {
                                SueldoTexto = it
                                SueldoHora = it.toFloatOrNull() ?: 0f
                            },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        ErrorMensaje?.let {
                            Text(text = it, color = Color.Red)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            OutlinedButton(
                                onClick = {

                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "New button"

                                )
                                Text(text = "Nuevo")
                            }
                            val scope = rememberCoroutineScope()
                            OutlinedButton(
                                onClick = {
                                    if (Nombre.isBlank() || SueldoTexto.isBlank())
                                        ErrorMensaje = "Hay datos vacios"

                                    scope.launch {
                                        saveTecnico(
                                            TecnicoEntity(
                                                nombre = Nombre,
                                                sueldoHora = SueldoHora
                                            )
                                        )
                                        Nombre = ""
                                        SueldoTexto = ""
                                        SueldoHora = 0.0f
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Save Button"
                                )
                                Text( text = "Guardar")
                            }

                        }


                    }
                }


            }

        }

    }

    private suspend fun saveTecnico(tecnico: TecnicoEntity) {

        tecnicoDb.tecnicoDao().save(tecnico)

    }
}