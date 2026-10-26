package com.olano.lavacampus.model

enum class Unidad(
    val etiqueta: String,
    val paso: Double,
    val minimo: Double
) {
    KG("Kg", 0.5, 1.0),
    PRENDA("Prenda", 1.0, 1.0),
    UNIDAD("Unidad", 1.0, 1.0);

    companion object {
        fun desde(nombre: String): Unidad = try {
            valueOf(nombre.uppercase())
        } catch (e: Exception) {
            KG
        }
    }
}