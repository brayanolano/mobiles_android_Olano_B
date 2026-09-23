package com.olano.navlab.navigation

sealed class Screen(val route: String) {
    object Inicio : Screen("inicio")
    object PerfilMedico : Screen("perfil_medico/{doctorId}") {
        fun createRoute(doctorId: Int) = "perfil_medico/$doctorId"
    }
    object AgendarCita : Screen("agendar_cita/{doctorNombre}") {
        fun createRoute(doctorNombre: String) = "agendar_cita/$doctorNombre"
    }
    object Confirmacion : Screen("confirmacion/{doctorNombre}/{fecha}/{hora}") {
        fun createRoute(doctorNombre: String, fecha: String, hora: String) =
            "confirmacion/$doctorNombre/$fecha/$hora"
    }
    object MisCitas : Screen("mis_citas")
    object Historial : Screen("historial")
    object Perfil : Screen("perfil")
}