package com.olano.navlab.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.olano.navlab.screens.*
import com.olano.tecsupfit.screens.MisCitasScreen
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
                onNavigate = { ruta ->
                    scope.launch { drawerState.close() }
                    navController.navigate(ruta)
                },
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

            composable(Screen.MisCitas.route) {
                MisCitasScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Historial.route) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Historial de Citas Médicas")
                }
            }

            // Redirige la ruta del Perfil hacia el PerfilMedicoScreen usando el doctor 1 por defecto
            composable(Screen.Perfil.route) {
                PerfilMedicoScreen(
                    doctorId = 1,
                    onBack = { navController.popBackStack() },
                    onAgendarClick = { doctorNombre ->
                        navController.navigate(Screen.AgendarCita.createRoute(doctorNombre))
                    }
                )
            }
        }
    }
}