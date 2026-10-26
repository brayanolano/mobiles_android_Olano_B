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