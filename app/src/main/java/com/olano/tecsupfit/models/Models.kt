package com.olano.navlab.model

data class Doctor(
    val id: Int,
    val nombre: String,
    val especialidad: String,
    val experiencia: String,
    val calificacion: Double,
    val resenas: Int,
    val biografia: String
)

data class Cita(
    val doctorNombre: String,
    val fecha: String,
    val hora: String,
    val estado: String // "Confirmada" o "Completada"
)

data class Usuario(
    val nombre: String = "Brayan Olano",
    val correo: String = "brayan.olano@tecsup.edu.pe",
    val rol: String = "Paciente Premium"
)