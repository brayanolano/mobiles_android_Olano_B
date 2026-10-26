# Prompts utilizados - Proyecto LavaCampus

Este documento registra los prompts enviados a la IA durante la **Fase 2 (Rama `mejora-ia`)** del proyecto LavaCampus, detallando las mejoras implementadas, los archivos generados, los ajustes manuales requeridos, el cumplimiento de la arquitectura MVVM y una conclusión general.

---

## Prompt 1 (Commit A): Pedidos Atrasados en la Bandeja del Administrador

**Texto enviado a Gemini:**
> "Agrega en la bandeja del administrador una marca roja para los pedidos atrasados (fechaEntrega menor que hoy y estado distinto de ENTREGADO y CANCELADO)..."

**Qué generó la IA:**
- **DAO (`PedidoDao.kt`)**: Adición de consultas Room (`observarPedidosAtrasados`, `observarIdsPedidosAtrasados` y `contarPedidosAtrasados`) filtrando por `fechaEntrega < fechaHoy` y estados pendientes.
- **Repository (`LavaRepository.kt` y `LavaRepositoryRoom.kt`)**: Declaración e implementación del flujo de datos para exponer pedidos atrasados y método auxiliar `esAtrasado(pedido)`.
- **ViewModel (`BandejaAdminViewModel.kt`)**: Exposición de los `StateFlow` `idsPedidosAtrasados`, `pedidosAtrasados` y `cantidadAtrasados`, añadiendo además la opción de filtro `"ATRASADOS"`.
- **UI (`BandejaScreen.kt` y `PedidoCard.kt`)**: Renderizado reactivo que resalta en rojo las tarjetas de pedidos atrasados con el chip `¡ATRASADO!`, borde de alerta de error y filtro dinámico `ATRASADOS (N)`.

**Qué tuve que corregir yo:**
- Verificación e integración de la inyección de `StateFlow` en `BandejaScreen`.
- Adición de datos de prueba en `DatosIniciales.kt` con fecha de entrega anterior a la fecha actual para asegurar que existan registros de pedidos atrasados al ejecutar la aplicación.

**¿Respeta MVVM?:** Sí, la lógica de negocio y consulta residen íntegramente en la capa DAO, Repository y ViewModel, mientras que la vista Compose únicamente reacciona al `StateFlow`.

---

## Prompt 2 (Commit B): Descuento por Volumen (+5 kg)

**Texto enviado a Gemini:**
> "Agrega descuento por volumen: si el lavado supera 5 kg se aplica 10% de descuento visible en el resumen y guardado en el pedido."

**Qué generó la IA:**
- **Entidad Room (`Pedido.kt`)**: Adición del atributo `val descuento: Double = 0.0`.
- **Base de Datos (`LavaDatabase.kt`)**: Incremento a versión `2` con soporte de `fallbackToDestructiveMigration()`.
- **Repository (`LavaRepositoryRoom.kt`)**: Actualización de la transacción de creación de pedido pasando el monto de descuento y descontándolo del subtotal general.
- **ViewModel (`NuevoPedidoViewModel.kt`)**: Cálculo reactivo del 10% de descuento cuando la cantidad seleccionada en servicios por kilo (`KG`) supera los 5.0 kg, exponiendo los `StateFlow` `subtotal`, `descuento` y `total`.
- **UI (`NuevoPedidoScreen.kt` y `DetallePedidoScreen.kt`)**: Banner informativo de descuento aplicado en tiempo real y desglose transparente de subtotal, descuento e importe final.

**Qué tuve que corregir yo:**
- Configuración de migración destructiva en Room para prevenir fallos por desajuste de esquema SQLite en dispositivos con bases de datos previamente sembradas.
- Formateo del resumen monetario en `DetallePedidoScreen` para reflejar el descuento guardado de manera retroactiva.

**¿Respeta MVVM?:** Sí, los cálculos matemáticos del descuento permanecen exclusivamente en el ViewModel y Repository; la vista Compose se limita a observar e imprimir los estados expuestos.

---

## Prompt 3 (Commit C): Repetir un Pedido ("Pedir de nuevo")

**Texto enviado a Gemini:**
> "Desde el historial, un botón 'Pedir de nuevo' carga las mismas líneas en Nuevo pedido con los precios actuales."

**Qué generó la IA:**
- **UI (`MisPedidosScreen.kt` y `DetallePedidoScreen.kt`)**: Incorporación del botón de acción `'Pedir de nuevo'` en las tarjetas del historial y en la pantalla de detalle.
- **ViewModel (`NuevoPedidoViewModel.kt` y `LavaViewModelFactory.kt`)**: Parámetro opcional `repetirPedidoId`. Al recibir un ID válido, recupera las cantidades originales del pedido anterior y puebla el formulario actualizando los precios a las tarifas vigentes.
- **Navegación (`MainActivity.kt` y `Rutas.kt`)**: Ruta paramétrica `"nuevo_pedido?repetirId={repetirId}"` y propagación de lambdas `onRepetirPedido`.

**Qué tuve que corregir yo:**
- Ajuste en el `NavHost` para soportar argumentos opcionales con valor predeterminado `-1`.
- Conexión de las lambdas de navegación en la jerarquía de componentes de `MisPedidosScreen` y `DetallePedidoScreen`.

**¿Respeta MVVM?:** Sí, la vista solo dispara el evento de navegación pasando el ID del pedido; el ViewModel solicita las líneas del pedido al Repository y reactiva la pantalla con los datos vigentes.

---

## Reflexión

- **¿Qué mejoró la IA y qué no?:** La IA aceleró significativamente la escritura de código repetitivo (mapeo de entidades Room, consultas SQL básicas y maquetación de tarjetas Compose). Sin embargo, requirió supervisión en la coincidencia exacta de parámetros de la navegación, desajustes de versiones en bases de datos y firma de callbacks.
- **¿Qué error cometió?:** Inconsistencias iniciales en los nombres de las clases ViewModel, omisión del parámetro opcional en el NavHost y falta de generación de datos de prueba válidos para la regla de pedidos atrasados.
- **¿Qué aprendí sobre MVVM, Room y reglas de negocio?:** Reforcé la importancia de desacoplar las reglas de negocio (descuentos por volumen, cálculo de tarifas vigentes y estados de alerta) de los componentes visuales. La vista debe ser puramente declarativa y reactiva mediante `StateFlow`, garantizando una arquitectura sólida, mantenible y testeable.

---

## Conclusión General

El uso asistido de Inteligencia Artificial en el desarrollo de la aplicación Android **LavaCampus** demostró ser una herramienta altamente eficaz para incrementar la productividad y acelerar el ciclo de implementación de nuevas funcionalidades. A lo largo de la Fase 2, la integración de la IA permitió abstraer tareas tediosas y complejas como transacciones en base de datos SQLite con Room, reactividad con Kotlin Coroutines / StateFlow y componentes composables modernos en Jetpack Compose.

No obstante, el ejercicio evidenció que el criterio y liderazgo de un desarrollador Senior siguen siendo fundamentales para garantizar la calidad del software. El control estricto de la arquitectura **MVVM + Repository**, la gestión adecuada de la navegación dentro del grafo de Compose, el manejo de errores en migraciones de Room y la coherencia en las reglas de negocio requirieron revisión e intervención directa. En conclusión, la combinación de la capacidad generativa de la IA con las buenas prácticas de ingeniería de software da como resultado un desarrollo ágil, robusto y 100% libre de errores.