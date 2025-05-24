package edu.ucne.registrotecnicosap2.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrotecnicosap2.presentation.prioridades.PrioridadScreen
import edu.ucne.registrotecnicosap2.presentation.prioridades.PrioridadesListScreen
import edu.ucne.registrotecnicosap2.presentation.tecnicos.TecnicoListScreen
import edu.ucne.registrotecnicosap2.presentation.tecnicos.TecnicoScreen
import edu.ucne.registrotecnicosap2.presentation.ticket.TicketListScreen
import edu.ucne.registrotecnicosap2.presentation.ticket.TicketScreen

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
               prioridadId =  prioridadId,
                goBack = {
                    navHostController.navigateUp()
                }
            )

        }

        composable<Screen.TicketList> {
            TicketListScreen(
                onEdit = { ticketId ->
                    navHostController.navigate(Screen.Ticket(ticketId))
                },
                createTicket = {
                    navHostController.navigate(Screen.Ticket(0))
                }

            )
        }
        composable<Screen.Ticket> { backStack ->
            val ticketId = backStack.toRoute<Screen.Ticket>().ticketId
            TicketScreen(
                ticketId = ticketId,
                goBack = {
                    navHostController.navigateUp()
                }
            )
        }
    }
}