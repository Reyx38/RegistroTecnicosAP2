package edu.ucne.registrotecnicosap2.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrotecnicosap2.Data.Entities.PrioridadEntity
import edu.ucne.registrotecnicosap2.Data.Entities.TecnicoEntity
import edu.ucne.registrotecnicosap2.Data.Entities.TicketEntity
import edu.ucne.registrotecnicosap2.presentation.Prioridades.PrioridadScreen
import edu.ucne.registrotecnicosap2.presentation.Prioridades.PrioridadViewModel
import edu.ucne.registrotecnicosap2.presentation.Prioridades.PrioridadesListScreen
import edu.ucne.registrotecnicosap2.presentation.tecnicos.TecnicoListScreen
import edu.ucne.registrotecnicosap2.presentation.tecnicos.TecnicoScreen
import edu.ucne.registrotecnicosap2.presentation.tecnicos.TecnicosViewModel
import edu.ucne.registrotecnicosap2.presentation.ticket.TicketListScreen
import edu.ucne.registrotecnicosap2.presentation.ticket.TicketScreen
import edu.ucne.registrotecnicosap2.presentation.ticket.TicketViewModel

@Composable
fun TecnicoNavHost(
    navHostController: NavHostController,
    tecnicoList: List<TecnicoEntity?>,
    prioridadesList: List<PrioridadEntity?>,
    ticketList: List<TicketEntity?>,
    viewModel: TecnicosViewModel,
    PrioridadviewModel: PrioridadViewModel,
    ticketViewModel: TicketViewModel,
    navcontrol: NavController
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home // Cambiamos la pantalla inicial a Home
    ) {
        // Pantalla Home (nueva)
        composable<Screen.Home> {
            HomeScreen(
                navController = navcontrol
            )
        }

        composable<Screen.TecnicoList> {
            TecnicoListScreen(
                tecnicoList = tecnicoList,
                onEdit = { tecnicoId ->
                    navHostController.navigate(Screen.Tecnico(tecnicoId))
                },
                onDelete = { tecnico ->
                    viewModel.deleteTecnico(tecnico)
                },
                onNavigateToPrioridades = {
                    navHostController.navigate(Screen.PrioridadList)
                },
                onNavigateToTickets = {
                    navHostController.navigate(Screen.TicketList)
                },
                navController = navcontrol
            )
        }

        composable<Screen.Tecnico> { backStack ->
            val tecnicoId = backStack.toRoute<Screen.Tecnico>().tecnicoId
            TecnicoScreen(tecnicoId, navcontrol, viewModel)
        }

        composable<Screen.PrioridadList> {
            PrioridadesListScreen(
                prioridadesList = prioridadesList,
                onEdit = { prioridadId ->
                    navHostController.navigate(Screen.Prioridad(prioridadId))

                },
                onDelete = { prioridad ->
                    PrioridadviewModel.deletePrioridad(prioridad = prioridad)
                },
                navController = navcontrol,
                onNavigateToTecnicos = {
                    navHostController.navigate(Screen.TecnicoList)
                },
                onNavigateToTickets = {
                    navHostController.navigate(Screen.TicketList)
                },
            )
        }

        composable<Screen.Prioridad> { backStack ->
            val prioridadId = backStack.toRoute<Screen.Prioridad>().prioridad
            PrioridadScreen(prioridadId, navcontrol, PrioridadviewModel)
        }

        composable<Screen.TicketList> {
            TicketListScreen(
                ticketList = ticketList,
                tecnicoList = tecnicoList,
                prioridadList = prioridadesList,
                onEdit = { ticketId ->
                    navHostController.navigate(Screen.Ticket(ticketId))
                },
                onDelete = { ticket ->
                    ticketViewModel.deleteTicket(ticket)
                },
                onNavigationToTecnico = {
                    navHostController.navigate(Screen.TecnicoList)
                },
                onNavigationToPrioridad = {
                    navHostController.navigate(Screen.PrioridadList)
                },
                navcontrol
            )
        }
        composable<Screen.Ticket> { backStack ->
            val ticketId = backStack.toRoute<Screen.Ticket>().ticketId
            TicketScreen(
                ticketId = ticketId,
                tecnicos = tecnicoList,
                prioridades = prioridadesList,
                navcontrol,
                ticketViewModel
            )
        }
    }
}