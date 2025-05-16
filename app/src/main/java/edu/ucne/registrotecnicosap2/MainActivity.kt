package edu.ucne.registrotecnicosap2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import edu.ucne.registrotecnicosap2.Data.Database.TecnicoDb
import edu.ucne.registrotecnicosap2.Data.repository.TecnicoRepository
import edu.ucne.registrotecnicosap2.presentation.navigation.TecnicoNavHost
import edu.ucne.registrotecnicosap2.presentation.tecnicos.TecnicosViewModel
import edu.ucne.registrotecnicosap2.ui.theme.RegistroTecnicosAP2Theme

class MainActivity : ComponentActivity() {
    private lateinit var tecnicoDb: TecnicoDb
    private lateinit var tecnicosRepository: TecnicoRepository
    private lateinit var tecnicosViewModel: TecnicosViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        tecnicoDb = Room.databaseBuilder(
            applicationContext,
            TecnicoDb::class.java,
            "Tecnico.db"
        ).fallbackToDestructiveMigration().build()

        tecnicosRepository = TecnicoRepository(tecnicoDb.tecnicoDao())
        tecnicosViewModel = TecnicosViewModel(tecnicosRepository)

        setContent {
            val lifecycleOwner = LocalLifecycleOwner.current
            val tecnicoList by tecnicoDb.tecnicoDao().getAll()
                .collectAsStateWithLifecycle(
                    initialValue = emptyList(),
                    lifecycleOwner = lifecycleOwner,
                    minActiveState = Lifecycle.State.STARTED
                )

            RegistroTecnicosAP2Theme {
                val nav = rememberNavController()
                TecnicoNavHost(nav, tecnicoList, tecnicosViewModel, nav)
            }
        }
    }
}
