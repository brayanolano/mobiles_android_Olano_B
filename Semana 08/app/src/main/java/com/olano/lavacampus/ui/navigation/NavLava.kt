package com.olano.lavacampus.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.olano.lavacampus.LavaApp
import com.olano.lavacampus.model.Rol
import com.olano.lavacampus.ui.administrador.ServiciosScreen
import com.olano.lavacampus.ui.components.AppTopBar
import com.olano.lavacampus.ui.estudiante.InicioScreen
import com.olano.lavacampus.ui.estudiante.LoginScreen
import com.olano.lavacampus.ui.estudiante.SplashScreen
import com.olano.lavacampus.ui.theme.LavaTheme
import com.olano.lavacampus.viewmodel.InicioViewModel
import com.olano.lavacampus.viewmodel.LavaViewModelFactory
import com.olano.lavacampus.viewmodel.ServiciosViewModel
import com.olano.lavacampus.viewmodel.SesionViewModel

@Composable
fun NavLava() {
    val nav = rememberNavController()
    val app = LocalContext.current.applicationContext as LavaApp

    val perfilGuardado by app.preferenciasStore.perfilActivo.collectAsState(initial = "")
    val usuarioIdGuardado by app.preferenciasStore.usuarioId.collectAsState(initial = -1)
    val modoOscuro by app.preferenciasStore.modoOscuro.collectAsState(initial = false)

    val rolActual = if (perfilGuardado == Rol.ADMIN.name) Rol.ADMIN else Rol.CLIENTE

    val factory = LavaViewModelFactory(app.repository, app.preferenciasStore, usuarioIdGuardado)

    LavaTheme(rol = rolActual, modoOscuro = modoOscuro) {
        NavHost(navController = nav, startDestination = Rutas.SPLASH) {
            composable(Rutas.SPLASH) {
                SplashScreen(onTerminar = {
                    val destino = when (perfilGuardado) {
                        Rol.CLIENTE.name -> Rutas.INICIO
                        Rol.ADMIN.name -> Rutas.PANEL
                        else -> Rutas.LOGIN
                    }
                    nav.navigate(destino) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                })
            }

            composable(Rutas.LOGIN) {
                val vm: SesionViewModel = viewModel(factory = factory)
                LoginScreen(
                    vm = vm,
                    onLoginExito = { rol ->
                        val destino = if (rol == Rol.ADMIN) Rutas.PANEL else Rutas.INICIO
                        nav.navigate(destino) {
                            popUpTo(Rutas.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

            composable(Rutas.INICIO) {
                val vm: InicioViewModel = viewModel(factory = factory)
                InicioScreen(
                    vm = vm,
                    onNuevoPedido = { nav.navigate(Rutas.NUEVO_PEDIDO) },
                    onVerPedidos = { nav.navigate(Rutas.MIS_PEDIDOS) },
                    onVerTarifas = { nav.navigate(Rutas.TARIFAS) },
                    onVerDetalle = { id -> nav.navigate(Rutas.detallePedido(id)) }
                )
            }

            composable(Rutas.SERVICIOS) {
                val vm: ServiciosViewModel = viewModel(factory = factory)
                Scaffold(topBar = { AppTopBar("Servicios") }) { padding ->
                    Box(Modifier.padding(padding)) {
                        ServiciosScreen(vm = vm, onEditar = { /* Paso 13 */ })
                    }
                }
            }
        }
    }
}