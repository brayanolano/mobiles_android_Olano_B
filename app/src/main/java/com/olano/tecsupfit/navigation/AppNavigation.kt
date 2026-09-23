package com.olano.navlab.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.olano.navlab.screens.*
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                usuarioNombre = "Brayan Olano",
                onNavigate = { ruta -> navController.navigate(ruta) },
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Inicio.route
        ) {
            composable(Screen.Inicio.route) {
                InicioScreen(
                    onOpenDrawer = { scope.launch { drawerState.open() } },
                    onDoctorSelect = { doctorId ->
                        navController.navigate(Screen.PerfilMedico.createRoute(doctorId))
                    }
                )
            }

            composable(
                route = Screen.PerfilMedico.route,
                arguments = listOf(navArgument("doctorId") { type = NavType.IntType })
            ) { backStack ->
                val doctorId = backStack.arguments?.getInt("doctorId") ?: 1
                PerfilMedicoScreen(
                    doctorId = doctorId,
                    onBack = { navController.popBackStack() },
                    onAgendarClick = { doctorNombre ->
                        navController.navigate(Screen.AgendarCita.createRoute(doctorNombre))
                    }
                )
            }

            composable(
                route = Screen.AgendarCita.route,
                arguments = listOf(navArgument("doctorNombre") { type = NavType.StringType })
            ) { backStack ->
                val doctorNombre = backStack.arguments?.getString("doctorNombre") ?: ""
                AgendarCitaScreen(
                    doctorNombre = doctorNombre,
                    onBack = { navController.popBackStack() },
                    onConfirmar = { fecha, hora ->
                        navController.navigate(Screen.Confirmacion.createRoute(doctorNombre, fecha, hora))
                    }
                )
            }

            composable(
                route = Screen.Confirmacion.route,
                arguments = listOf(
                    navArgument("doctorNombre") { type = NavType.StringType },
                    navArgument("fecha") { type = NavType.StringType },
                    navArgument("hora") { type = NavType.StringType }
                )
            ) { backStack ->
                val doc = backStack.arguments?.getString("doctorNombre") ?: ""
                val fecha = backStack.arguments?.getString("fecha") ?: ""
                val hora = backStack.arguments?.getString("hora") ?: ""

                ConfirmacionScreen(
                    doctorNombre = doc,
                    fecha = fecha,
                    hora = hora,
                    onVolverInicio = {
                        navController.navigate(Screen.MisCitas.route) {
                            popUpTo(Screen.Inicio.route)
                        }
                    }
                )
            }

            composable(Screen.MisCitas.route) { MisCitasScreen() }
            composable(Screen.Historial.route) { Text("Pantalla Historial") }
            composable(Screen.Perfil.route) { PerfilScreen() }
        }
    }
}