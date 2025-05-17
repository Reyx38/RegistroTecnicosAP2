package edu.ucne.registrotecnicosap2.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrotecnicosap2.Data.Entities.PrioridadEntity
import edu.ucne.registrotecnicosap2.Data.Entities.TecnicoEntity
import edu.ucne.registrotecnicosap2.presentation.Prioridades.PrioridadScreen
import edu.ucne.registrotecnicosap2.presentation.Prioridades.PrioridadViewModel
import edu.ucne.registrotecnicosap2.presentation.Prioridades.PrioridadesListScreen
import edu.ucne.registrotecnicosap2.presentation.tecnicos.TecnicoListScreen
import edu.ucne.registrotecnicosap2.presentation.tecnicos.TecnicoScreen
import edu.ucne.registrotecnicosap2.presentation.tecnicos.TecnicosViewModel

@Composable
fun TecnicoNavHost(
    navHostController: NavHostController,
    tecnicoList: List<TecnicoEntity>,
    prioridadesList: List<PrioridadEntity?>,
    viewModel: TecnicosViewModel,
    PrioridadviewModel: PrioridadViewModel,
    navcontrol: NavController
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.TecnicoList
    ) {

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
                onNavigateToTickets ={
                    navHostController.navigate(Screen.TicketList)
                }
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
                onDelete = { prioridad  ->
                    PrioridadviewModel.deletePrioridad(prioridad = prioridad)
                },
                navController = navcontrol
            )
        }

        composable<Screen.Prioridad> { backStack ->
            val prioridadId = backStack.toRoute<Screen.Prioridad>().prioridad
            PrioridadScreen(prioridadId, navcontrol, PrioridadviewModel)
        }

        composable <Screen.TicketList>{ }
        composable <Screen.Ticket> { }
    }
}