# POO-09 · APIs cruzadas de Minecraft

**Entrega individual de Nicolás Rodríguez.** A1 es Nicolás Rodríguez, A2 es
Amir y A3 es Sofi.

**Escenario.** A1 registra un ataque simulado de un Creeper local si el
Zombie de prueba de Amir sobrevive al daño indicado y Sofi confirma que el
objetivo registrado es un Zombie vivo.

| Rol | OpenAPI JSON | Swagger UI |
| --- | --- | --- |
| A1 · Nicolás | [Nicolás](http://100.83.179.97:8080/v3/api-docs) | [Swagger UI](http://100.83.179.97:8080/swagger-ui/index.html) |
| A2 · Amir | [Amir](http://100.78.121.31:8080/v3/api-docs) | [Swagger UI](http://100.78.121.31:8080/swagger-ui/index.html) |
| A3 · Sofi | [Sofi](http://100.79.75.27:8081/v3/api-docs) | [Swagger UI](http://100.79.75.27:8081/swagger-ui/index.html) |

## Cómo probarlo

1. Conectá Tailscale. Verificá los OpenAPI de Amir y Sofi y arrancá A1 con
   `./mvnw spring-boot:run` y las variables indicadas en el [README](README.md).
2. Importá [la colección de Insomnia](insomnia/POO09-Minecraft.postman_collection.json).
   Configurá las bases y copiá un `creeperId` de A1 desde
   `GET /api/minecraft/mobs`.
3. Ejecutá `GET /api/minecraft/jugadas-cruzadas` antes y después de cada
   `POST /api/minecraft/jugadas-cruzadas`. Usá el mismo Creeper en los tres.

| Solicitud | `objetivoId` | `danio` | Resultado |
| --- | --- | ---: | --- |
| Ambos aceptan | `11111111-1111-1111-1111-111111111111` | 0 | `201`. El GET muestra una jugada más. |
| Rechaza Amir | `11111111-1111-1111-1111-111111111111` | 25 | `409`, `rechazoDe: A2`. El listado conserva sus filas. |
| Rechaza Sofi | `00000000-0000-4000-8000-000000000009` | 0 | `409`, `rechazoDe: A3`. El listado conserva sus filas. |

El 23/11/2026 se verificaron los tres resultados con Amir y Sofi por
Tailscale y una H2 aislada para A1. Las respuestas están guardadas en la
colección. La suite automatizada pasó 63 pruebas.

**Enlace al commit de la solución:** pendiente de publicación.
