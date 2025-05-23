package edu.ucne.registrotecnicosap2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.registrotecnicosap2.presentation.navigation.TecnicoNavHost
import edu.ucne.registrotecnicosap2.ui.theme.RegistroTecnicosAP2Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistroTecnicosAP2Theme {
                val nav = rememberNavController()
                TecnicoNavHost(nav)
            }
        }
    }
}
