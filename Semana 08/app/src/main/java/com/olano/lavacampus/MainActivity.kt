package com.olano.lavacampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.olano.lavacampus.model.Rol
import com.olano.lavacampus.ui.administrador.AjustesScreen
import com.olano.lavacampus.ui.administrador.BandejaScreen
import com.olano.lavacampus.ui.administrador.ClientesScreen
import com.olano.lavacampus.ui.administrador.FormularioServicioScreen
import com.olano.lavacampus.ui.administrador.GestionPedidoScreen
import com.olano.lavacampus.ui.administrador.PanelScreen
import com.olano.lavacampus.ui.administrador.ServiciosScreen
import com.olano.lavacampus.ui.components.AppBottomBar
import com.olano.lavacampus.ui.estudiante.DetallePedidoScreen
import com.olano.lavacampus.ui.estudiante.InicioScreen
import com.olano.lavacampus.ui.estudiante.LoginScreen
import com.olano.lavacampus.ui.estudiante.MisPedidosScreen
import com.olano.lavacampus.ui.estudiante.NuevoPedidoScreen
import com.olano.lavacampus.ui.estudiante.PerfilScreen
import com.olano.lavacampus.ui.estudiante.SplashScreen
import com.olano.lavacampus.ui.estudiante.TarifasScreen
import com.olano.lavacampus.ui.theme.LavaTheme
import com.olano.lavacampus.viewmodel.AjustesViewModel
import com.olano.lavacampus.viewmodel.BandejaAdminViewModel
import com.olano.lavacampus.viewmodel.ClientesViewModel
import com.olano.lavacampus.viewmodel.DetallePedidoViewModel
import com.olano.lavacampus.viewmodel.FormularioServicioViewModel
import com.olano.lavacampus.viewmodel.GestionPedidoViewModel
import com.olano.lavacampus.viewmodel.InicioViewModel
import com.olano.lavacampus.viewmodel.LavaViewModelFactory
import com.olano.lavacampus.viewmodel.MisPedidosViewModel
import com.olano.lavacampus.viewmodel.NuevoPedidoViewModel
import com.olano.lavacampus.viewmodel.PanelAdminViewModel
import com.olano.lavacampus.viewmodel.ServiciosViewModel
import com.olano.lavacampus.viewmodel.SesionViewModel
import com.olano.lavacampus.viewmodel.TarifasViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as LavaApp

        setContent {
            val perfilGuardado by app.preferenciasStore.perfilActivo.collectAsState(initial = "")
            val usuarioIdGuardado by app.preferenciasStore.usuarioId.collectAsState(initial = -1)
            val modoOscuro by app.preferenciasStore.modoOscuro.collectAsState(initial = false)

            val rolActual = if (perfilGuardado == Rol.ADMIN.name) Rol.ADMIN else Rol.CLIENTE

            LavaTheme(rol = rolActual, modoOscuro = modoOscuro) {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val rutaActual = navBackStackEntry?.destination?.route

                val rutasEstudiante = listOf("inicio", "mis_pedidos", "tarifas", "perfil")
                val rutasAdmin = listOf("panel_admin", "bandeja_admin", "servicios_admin", "clientes_admin", "ajustes_admin")

                val esRutaEstudiante = rutaActual in rutasEstudiante
                val esRutaAdmin = rutaActual in rutasAdmin
                val mostrarBottomBar = esRutaEstudiante || esRutaAdmin

                Scaffold(
                    bottomBar = {
                        if (mostrarBottomBar) {
                            val rootDestination = if (esRutaAdmin) "panel_admin" else "inicio"
                            AppBottomBar(
                                rutaActual = rutaActual,
                                esAdmin = esRutaAdmin,
                                onNavegar = { nuevaRuta ->
                                    if (rutaActual != nuevaRuta) {
                                        navController.navigate(nuevaRuta) {
                                            popUpTo(rootDestination) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = "splash",
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        // Splash & Autenticación
                        composable("splash") {
                            SplashScreen(
                                onTerminar = {
                                    val destino = when (perfilGuardado) {
                                        Rol.CLIENTE.name -> "inicio"
                                        Rol.ADMIN.name -> "panel_admin"
                                        else -> "login"
                                    }
                                    navController.navigate(destino) {
                                        popUpTo("splash") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("login") {
                            val factory = LavaViewModelFactory(app.repository, app.preferenciasStore, usuarioIdGuardado)
                            LoginScreen(
                                vm = viewModel<SesionViewModel>(factory = factory),
                                onLoginExito = { rol ->
                                    val destino = if (rol == Rol.ADMIN) "panel_admin" else "inicio"
                                    navController.navigate(destino) {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // Pantallas Estudiante
                        composable("inicio") {
                            val factory = LavaViewModelFactory(app.repository, app.preferenciasStore, usuarioIdGuardado)
                            InicioScreen(
                                vm = viewModel<InicioViewModel>(factory = factory),
                                onNuevoPedido = { navController.navigate("nuevo_pedido") },
                                onVerPedidos = { navController.navigate("mis_pedidos") },
                                onVerTarifas = { navController.navigate("tarifas") },
                                onVerDetalle = { id -> navController.navigate("detalle_pedido/$id") }
                            )
                        }

                        composable("mis_pedidos") {
                            val factory = LavaViewModelFactory(app.repository, app.preferenciasStore, usuarioIdGuardado)
                            MisPedidosScreen(
                                vm = viewModel<MisPedidosViewModel>(factory = factory),
                                onAtras = { navController.popBackStack() },
                                onVerDetalle = { id -> navController.navigate("detalle_pedido/$id") },
                                onRepetirPedido = { repetirId ->
                                    navController.navigate("nuevo_pedido?repetirId=$repetirId")
                                }
                            )
                        }

                        composable("tarifas") {
                            val factory = LavaViewModelFactory(app.repository, app.preferenciasStore, usuarioIdGuardado)
                            TarifasScreen(
                                vm = viewModel<TarifasViewModel>(factory = factory),
                                onAtras = { navController.popBackStack() }
                            )
                        }

                        composable("perfil") {
                            val factory = LavaViewModelFactory(app.repository, app.preferenciasStore, usuarioIdGuardado)
                            PerfilScreen(
                                vm = viewModel<AjustesViewModel>(factory = factory),
                                onAtras = { navController.popBackStack() },
                                onCerrarSesion = {
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                },
                                onCambiarPerfilExito = { nuevoRol ->
                                    val destino = if (nuevoRol == "ADMIN") "panel_admin" else "inicio"
                                    navController.navigate(destino) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(
                            route = "nuevo_pedido?repetirId={repetirId}",
                            arguments = listOf(
                                navArgument("repetirId") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) { backStackEntry ->
                            val repetirId = backStackEntry.arguments?.getInt("repetirId") ?: -1
                            val factory = LavaViewModelFactory(
                                repo = app.repository,
                                prefs = app.preferenciasStore,
                                usuarioId = usuarioIdGuardado,
                                repetirPedidoId = repetirId
                            )
                            NuevoPedidoScreen(
                                vm = viewModel<NuevoPedidoViewModel>(factory = factory),
                                onAtras = { navController.popBackStack() },
                                onPedidoCreado = { id ->
                                    navController.navigate("detalle_pedido/$id") {
                                        popUpTo("inicio") { saveState = false }
                                    }
                                }
                            )
                        }

                        composable(
                            route = "detalle_pedido/{pedidoId}",
                            arguments = listOf(navArgument("pedidoId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val pedidoId = backStackEntry.arguments?.getInt("pedidoId") ?: -1
                            val factory = LavaViewModelFactory(app.repository, app.preferenciasStore, usuarioIdGuardado, pedidoId = pedidoId)
                            DetallePedidoScreen(
                                vm = viewModel<DetallePedidoViewModel>(factory = factory),
                                onAtras = { navController.popBackStack() },
                                onRepetirPedido = { repetirId ->
                                    navController.navigate("nuevo_pedido?repetirId=$repetirId")
                                }
                            )
                        }

                        // Pantallas Administrador
                        composable("panel_admin") {
                            val factory = LavaViewModelFactory(app.repository, app.preferenciasStore, usuarioIdGuardado)
                            PanelScreen(
                                vm = viewModel<PanelAdminViewModel>(factory = factory),
                                onIrBandeja = { navController.navigate("bandeja_admin") },
                                onIrServicios = { navController.navigate("servicios_admin") },
                                onIrClientes = { navController.navigate("clientes_admin") },
                                onIrAjustes = { navController.navigate("ajustes_admin") }
                            )
                        }

                        composable("bandeja_admin") {
                            val factory = LavaViewModelFactory(app.repository, app.preferenciasStore, usuarioIdGuardado)
                            BandejaScreen(
                                vm = viewModel<BandejaAdminViewModel>(factory = factory),
                                onAtras = { navController.popBackStack() },
                                onGestionarPedido = { id -> navController.navigate("gestion_pedido/$id") }
                            )
                        }

                        composable(
                            route = "gestion_pedido/{pedidoId}",
                            arguments = listOf(navArgument("pedidoId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val pedidoId = backStackEntry.arguments?.getInt("pedidoId") ?: -1
                            val factory = LavaViewModelFactory(app.repository, app.preferenciasStore, usuarioIdGuardado, pedidoId = pedidoId)
                            GestionPedidoScreen(
                                vm = viewModel<GestionPedidoViewModel>(factory = factory),
                                onAtras = { navController.popBackStack() }
                            )
                        }

                        composable("servicios_admin") {
                            val factory = LavaViewModelFactory(app.repository, app.preferenciasStore, usuarioIdGuardado)
                            ServiciosScreen(
                                vm = viewModel<ServiciosViewModel>(factory = factory),
                                onEditar = { id -> navController.navigate("formulario_servicio/$id") }
                            )
                        }

                        composable(
                            route = "formulario_servicio/{servicioId}",
                            arguments = listOf(navArgument("servicioId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val servicioId = backStackEntry.arguments?.getInt("servicioId") ?: 0
                            val factory = LavaViewModelFactory(app.repository, app.preferenciasStore, usuarioIdGuardado, servicioId = servicioId)
                            FormularioServicioScreen(
                                vm = viewModel<FormularioServicioViewModel>(factory = factory),
                                onAtras = { navController.popBackStack() }
                            )
                        }

                        composable("clientes_admin") {
                            val factory = LavaViewModelFactory(app.repository, app.preferenciasStore, usuarioIdGuardado)
                            ClientesScreen(
                                vm = viewModel<ClientesViewModel>(factory = factory),
                                onAtras = { navController.popBackStack() }
                            )
                        }

                        composable("ajustes_admin") {
                            val factory = LavaViewModelFactory(app.repository, app.preferenciasStore, usuarioIdGuardado)
                            AjustesScreen(
                                vm = viewModel<AjustesViewModel>(factory = factory),
                                onAtras = { navController.popBackStack() },
                                onCerrarSesion = {
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                },
                                onCambiarPerfilExito = { nuevoRol ->
                                    val destino = if (nuevoRol == "ADMIN") "panel_admin" else "inicio"
                                    navController.navigate(destino) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}