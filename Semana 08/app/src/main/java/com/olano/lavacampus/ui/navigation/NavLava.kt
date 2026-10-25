package com.olano.lavacampus.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.olano.lavacampus.LavaApp
import com.olano.lavacampus.ui.administrador.ServiciosScreen
import com.olano.lavacampus.ui.components.AppTopBar
import com.olano.lavacampus.ui.estudiante.SplashScreen
import com.olano.lavacampus.viewmodel.LavaViewModelFactory
import com.olano.lavacampus.viewmodel.ServiciosViewModel

@Composable
fun NavLava() {
    val nav = rememberNavController()
    val app = LocalContext.current.applicationContext as LavaApp
    val factory = LavaViewModelFactory(app.repository)

    NavHost(navController = nav, startDestination = Rutas.SPLASH) {
        composable(Rutas.SPLASH) {
            SplashScreen(onTerminar = {
                // Paso 6: aquí decidirá entre Login o la pantalla principal del perfil guardado
                nav.navigate(Rutas.SERVICIOS) { popUpTo(Rutas.SPLASH) { inclusive = true } }
            })
        }
        composable(Rutas.SERVICIOS) {
            val vm: ServiciosViewModel = viewModel(factory = factory)
            Scaffold(topBar = { AppTopBar("Servicios") }) { padding ->
                Box(Modifier.padding(padding)) {
                    ServiciosScreen(vm = vm, onEditar = { /* Paso 13: FormularioServicio */ })
                }
            }
        }
        // TODO (Pasos 6 al 13): resto de destinos y grafos por perfil
    }
}
