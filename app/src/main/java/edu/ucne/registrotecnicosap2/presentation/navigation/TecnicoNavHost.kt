package edu.ucne.registrotecnicosap2.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
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
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home
    ) {
        composable<Screen.Home> {
            HomeScreen(
                navController = navHostController
            )
        }

        composable<Screen.TecnicoList> {
            TecnicoListScreen(
                createTecnico = {
                    navHostController.navigate(Screen.Tecnico(0))
                },
                onEditTecnico = { tecnicoId ->
                    navHostController.navigate(Screen.Tecnico(tecnicoId))
                }

            )
        }

        composable<Screen.Tecnico> { backStack ->
            val tecnicoId = backStack.toRoute<Screen.Tecnico>().tecnicoId
            TecnicoScreen(
                tecnicoId = tecnicoId,
                goBack = {
                    navHostController.navigateUp()
                }
            )
        }

        composable<Screen.PrioridadList> {
            PrioridadesListScreen(
                onEdit = { prioridadId ->
                    navHostController.navigate(Screen.Prioridad(prioridadId))
                },
                createPrioridad = {
                    navHostController.navigate(Screen.Prioridad(0))
                }

            )
        }

        composable<Screen.Prioridad> { backStack ->
            val prioridadId = backStack.toRoute<Screen.Prioridad>().prioridad
            PrioridadScreen(
                prioridadId,
                goBack = {
                    navHostController.navigateUp()
                }
            )

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