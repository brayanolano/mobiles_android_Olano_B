package com.olano.lavacampus.data.local

import com.olano.lavacampus.data.local.entity.DetallePedido
import com.olano.lavacampus.data.local.entity.Pedido
import com.olano.lavacampus.data.local.entity.Servicio
import com.olano.lavacampus.data.local.entity.Usuario
import com.olano.lavacampus.model.EstadoPedido
import com.olano.lavacampus.model.Rol
import com.olano.lavacampus.model.Unidad
import java.time.DayOfWeek
import java.time.LocalDate

object DatosIniciales {

    suspend fun sembrar(db: LavaDatabase) {
        if (db.usuarioDao().contar() > 0) return

        // 1. Servicios iniciales
        val servicios = listOf(
            Servicio(
                id = 1,
                nombre = "Lavado y secado",
                descripcion = "Lavado y secado profesional por kilo",
                unidad = Unidad.KG.name,
                precio = 8.0,
                horasEntrega = 24,
                activo = true,
                icono = "wash"
            ),
            Servicio(
                id = 2,
                nombre = "Planchado",
                descripcion = "Planchado a vapor por prenda",
                unidad = Unidad.PRENDA.name,
                precio = 2.5,
                horasEntrega = 48,
                activo = true,
                icono = "iron"
            ),
            Servicio(
                id = 3,
                nombre = "Lavado en seco",
                descripcion = "Tratamiento especial seco por prenda",
                unidad = Unidad.PRENDA.name,
                precio = 12.0,
                horasEntrega = 48,
                activo = true,
                icono = "shirt"
            ),
            Servicio(
                id = 4,
                nombre = "Edredón / frazada",
                descripcion = "Edredones, frazadas y cobertores pesados",
                unidad = Unidad.UNIDAD.name,
                precio = 20.0,
                horasEntrega = 48,
                activo = false,
                icono = "wash"
            )
        )
        db.servicioDao().insertarTodos(servicios)

        // 2. Usuarios iniciales (3 estudiantes, 1 administrador)
        val usuarios = listOf(
            Usuario(
                id = 1,
                nombre = "Ana García",
                codigo = "U202310234",
                correo = "ana@correo.test",
                clave = "1234",
                rol = Rol.CLIENTE.name
            ),
            Usuario(
                id = 2,
                nombre = "Luis Ramos",
                codigo = "U202310877",
                correo = "luis@correo.test",
                clave = "1234",
                rol = Rol.CLIENTE.name
            ),
            Usuario(
                id = 3,
                nombre = "Marta Paredes",
                codigo = "U202411022",
                correo = "marta@correo.test",
                clave = "1234",
                rol = Rol.CLIENTE.name
            ),
            Usuario(
                id = 4,
                nombre = "Administración",
                codigo = "",
                correo = "admin@lavacampus.test",
                clave = "admin",
                rol = Rol.ADMIN.name
            )
        )
        db.usuarioDao().insertarTodos(usuarios)

        // 3. Cálculo dinámico de fechas
        val hoy = LocalDate.now()
        val lunesEstaSemana = hoy.with(DayOfWeek.MONDAY)

        fun calcularEntrega(fechaBase: LocalDate, dias: Int): String {
            var f = fechaBase.plusDays(dias.toLong())
            if (f.dayOfWeek == DayOfWeek.SUNDAY) f = f.plusDays(1)
            return f.toString()
        }

        // 4. Pedidos iniciales con sus detalles

        // --- Pedido #1 (Ana García) ENTREGADO y pagado
        val p1Id = db.pedidoDao().insertar(
            Pedido(
                usuarioId = 1,
                fechaRegistro = lunesEstaSemana.toString(),
                fechaEntrega = calcularEntrega(lunesEstaSemana, 1),
                estado = EstadoPedido.ENTREGADO.name,
                total = 36.0,
                casillero = "C-01",
                pagado = true,
                nota = "Sin suavizante"
            )
        ).toInt()
        db.detalleDao().insertarTodos(
            listOf(
                DetallePedido(pedidoId = p1Id, servicioId = 1, cantidad = 4.5, precioUnitario = 8.0, subtotal = 36.0)
            )
        )

        // --- Pedido #2 (Ana García) LISTO en casillero C-07
        val p2Id = db.pedidoDao().insertar(
            Pedido(
                usuarioId = 1,
                fechaRegistro = hoy.minusDays(1).toString(),
                fechaEntrega = hoy.toString(),
                estado = EstadoPedido.LISTO.name,
                total = 16.0,
                casillero = "C-07",
                pagado = false,
                nota = "Recojo por la tarde"
            )
        ).toInt()
        db.detalleDao().insertarTodos(
            listOf(
                DetallePedido(pedidoId = p2Id, servicioId = 1, cantidad = 2.0, precioUnitario = 8.0, subtotal = 16.0)
            )
        )

        // --- Pedido #3 (Ana García) EN_PROCESO
        val p3Id = db.pedidoDao().insertar(
            Pedido(
                usuarioId = 1,
                fechaRegistro = hoy.toString(),
                fechaEntrega = calcularEntrega(hoy, 2),
                estado = EstadoPedido.EN_PROCESO.name,
                total = 40.5,
                casillero = null,
                pagado = false,
                nota = "Ropa de color separada"
            )
        ).toInt()
        db.detalleDao().insertarTodos(
            listOf(
                DetallePedido(pedidoId = p3Id, servicioId = 1, cantidad = 3.5, precioUnitario = 8.0, subtotal = 28.0),
                DetallePedido(pedidoId = p3Id, servicioId = 2, cantidad = 5.0, precioUnitario = 2.5, subtotal = 12.5)
            )
        )

        // --- Pedido #4 (Luis Ramos) RECIBIDO
        val p4Id = db.pedidoDao().insertar(
            Pedido(
                usuarioId = 2,
                fechaRegistro = hoy.toString(),
                fechaEntrega = calcularEntrega(hoy, 1),
                estado = EstadoPedido.RECIBIDO.name,
                total = 16.0,
                casillero = null,
                pagado = false,
                nota = ""
            )
        ).toInt()
        db.detalleDao().insertarTodos(
            listOf(
                DetallePedido(pedidoId = p4Id, servicioId = 1, cantidad = 2.0, precioUnitario = 8.0, subtotal = 16.0)
            )
        )

        // --- Pedido #5 (Luis Ramos) LISTO en casillero C-05
        val p5Id = db.pedidoDao().insertar(
            Pedido(
                usuarioId = 2,
                fechaRegistro = hoy.minusDays(1).toString(),
                fechaEntrega = hoy.toString(),
                estado = EstadoPedido.LISTO.name,
                total = 24.0,
                casillero = "C-05",
                pagado = false,
                nota = "Urgente"
            )
        ).toInt()
        db.detalleDao().insertarTodos(
            listOf(
                DetallePedido(pedidoId = p5Id, servicioId = 1, cantidad = 3.0, precioUnitario = 8.0, subtotal = 24.0)
            )
        )

        // --- Pedido #6 (Marta Paredes) EN_PROCESO
        val p6Id = db.pedidoDao().insertar(
            Pedido(
                usuarioId = 3,
                fechaRegistro = hoy.toString(),
                fechaEntrega = calcularEntrega(hoy, 2),
                estado = EstadoPedido.EN_PROCESO.name,
                total = 48.0,
                casillero = null,
                pagado = false,
                nota = "Tratamiento delicado"
            )
        ).toInt()
        db.detalleDao().insertarTodos(
            listOf(
                DetallePedido(pedidoId = p6Id, servicioId = 3, cantidad = 4.0, precioUnitario = 12.0, subtotal = 48.0)
            )
        )
    }
}