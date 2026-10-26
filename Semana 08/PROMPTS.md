# Prompts utilizados

## Prompt 1 (Commit A)
**Texto enviado a Gemini:** 
"Agrega en la bandeja del administrador una marca roja para los pedidos atrasados (fechaEntrega menor que hoy y estado distinto de ENTREGADO y CANCELADO)..."

**Qué generó la IA:**
- Modificación en PedidoDao, LavaRepository, LavaRepositoryRoom, BandejaAdminViewModel y BandejaScreen.
- Creación de este archivo PROMPTS.md.

**Qué tuve que corregir yo:**
- Verificación e integración de la inyección de StateFlows de pedidos atrasados en BandejaScreen y resaltado en PedidoCard con compatibilidad Material 3.

**¿Respeta MVVM?:** Sí, la lógica de negocio y consulta residen en DAO/Repository/ViewModel y la vista solo reacciona al StateFlow.

## Prompt 2 (Commit B)
**Texto enviado a Gemini:** 
"Agrega descuento por volumen: si el lavado supera 5 kg se aplica 10% de descuento visible en el resumen y guardado en el pedido."

**Qué generó la IA:**
- Cálculo automático de descuento en NuevoPedidoViewModel.
- Actualización en el resumen de NuevoPedidoScreen y en el guardado de la transacción en LavaRepositoryRoom.
- Actualización del archivo PROMPTS.md.

**Qué tuve que corregir yo:**
- Configuración de migración destructiva en Room (Base de Datos versión 2) para el nuevo campo `descuento` en la entidad `Pedido` y formateo en DetallePedidoScreen.

**¿Respeta MVVM?:** Sí, la lógica del descuento se maneja íntegramente en el ViewModel y la capa de datos.