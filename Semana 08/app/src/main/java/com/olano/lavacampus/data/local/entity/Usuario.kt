package com.olano.lavacampus.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val codigo: String,    // Código de estudiante (vacío para ADMIN)
    val correo: String,
    val clave: String,
    val rol: String        // Rol.CLIENTE.name o Rol.ADMIN.name
)