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
import edu.ucne.registrotecnicosap2.Data.repository.PrioridadRepository
import edu.ucne.registrotecnicosap2.Data.repository.TecnicoRepository
import edu.ucne.registrotecnicosap2.Data.repository.TicketRepository
import edu.ucne.registrotecnicosap2.presentation.Prioridades.PrioridadViewModel
import edu.ucne.registrotecnicosap2.presentation.navigation.TecnicoNavHost
import edu.ucne.registrotecnicosap2.presentation.tecnicos.TecnicosViewModel
import edu.ucne.registrotecnicosap2.presentation.ticket.TicketViewModel
import edu.ucne.registrotecnicosap2.ui.theme.RegistroTecnicosAP2Theme

class MainActivity : ComponentActivity() {
    private lateinit var tecnicoDb: TecnicoDb
    private lateinit var tecnicosRepository: TecnicoRepository
    private lateinit var tecnicosViewModel: TecnicosViewModel
    private lateinit var prioridadRepository: PrioridadRepository
    private lateinit var prioridadViewModel: PrioridadViewModel
    private lateinit var ticketRepository: TicketRepository
    private lateinit var ticketViewModel: TicketViewModel

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
        prioridadRepository = PrioridadRepository(tecnicoDb.prioridadDao())
        prioridadViewModel = PrioridadViewModel(prioridadRepository)
        ticketRepository = TicketRepository(tecnicoDb.ticketDao())
        ticketViewModel = TicketViewModel(ticketRepository)

        setContent {
            val lifecycleOwner = LocalLifecycleOwner.current
            val tecnicoList by tecnicoDb.tecnicoDao().getAll()
                .collectAsStateWithLifecycle(
                    initialValue = emptyList(),
                    lifecycleOwner = lifecycleOwner,
                    minActiveState = Lifecycle.State.STARTED
                )
            val prioridadList by tecnicoDb.prioridadDao().getAll()
                .collectAsStateWithLifecycle(
                    initialValue = emptyList(),
                    lifecycleOwner = lifecycleOwner,
                    minActiveState = Lifecycle.State.STARTED
                )
            val ticketList by tecnicoDb.ticketDao().getAll()
                .collectAsStateWithLifecycle(
                    initialValue = emptyList(),
                    lifecycleOwner = lifecycleOwner,
                    minActiveState = Lifecycle.State.STARTED
                )


            RegistroTecnicosAP2Theme {
                val nav = rememberNavController()
                TecnicoNavHost(
                    nav,
                    tecnicoList,
                    prioridadList,
                    ticketList,
                    tecnicosViewModel,
                    prioridadViewModel,
                    ticketViewModel,
                    nav
                )
            }
        }
    }
}
