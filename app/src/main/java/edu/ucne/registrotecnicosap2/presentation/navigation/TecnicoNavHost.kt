package edu.ucne.registrotecnicosap2.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrotecnicosap2.Data.Entities.TecnicoEntity
import edu.ucne.registrotecnicosap2.presentation.tecnicos.TecnicoListScreen
import edu.ucne.registrotecnicosap2.presentation.tecnicos.TecnicoScreen
import edu.ucne.registrotecnicosap2.presentation.tecnicos.TecnicosViewModel

@Composable
fun TecnicoNavHost(
    navHostController: NavHostController,
    tecnicoList: List<TecnicoEntity>,
    viewModel: TecnicosViewModel,
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
                }
            )
        }

        composable<Screen.Tecnico> { backStack ->
            val tecnicoId = backStack.toRoute<Screen.Tecnico>().tecnicoId
            TecnicoScreen(tecnicoId, navcontrol, viewModel)
        }
    }
}