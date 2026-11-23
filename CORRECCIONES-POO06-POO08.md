# Seguimiento de correcciones POO-06 y POO-08

**Repositorio:** `NicoRodriguez85677/NRodriguez-taller-git-2026`
**Rama de trabajo actual:** `feat/poo09-openapi-2026`
**Última actualización del seguimiento:** 22/11/2026 (trabajo continuado durante la noche)

**Última verificación reportada por el estudiante:** 36 pruebas aprobadas, `BUILD SUCCESS`.

> **Criterio de registro:** `[x]` significa implementado y comprobado en el trabajo realizado; `[ ]` significa pendiente o no verificado. Los resultados locales se registran según las salidas y capturas compartidas por el estudiante. Los últimos commits locales todavía no se subieron a GitHub.

## Referencias

- `Corrección POO-06 - Nicolás Rodríguez.pdf`.
- `Corrección POO-08 - Nicolás Rodríguez.pdf`.
- Enunciado: [POO-09 — APIs cruzadas](https://github.com/alefq/afq-taller-git-2024/blob/main/docs/ejercicio-poo-09-apis-cruzadas-2026-11-16.md).

## Objetivo

Corregir las observaciones del profesor de POO-06 y POO-08, mantener el proyecto Minecraft funcional y preparar la integración de POO-09. Antes de cambiar código, comprobar si la funcionalidad ya existe; hacer cambios pequeños y ejecutar pruebas.

**Importante:** POO-09 reserva **3 puntos** para cerrar *todas* las observaciones de POO-08 cuando la corrección tuvo descuentos. Tener 36 pruebas aprobadas no demuestra, por sí solo, que todas las observaciones de arquitectura estén cerradas.

---

## 1. Correcciones realizadas y verificadas

### 1.1. Organización del dominio y paquetes

- [x] Mover las clases del dominio a `py.edu.uc.lp3.domain` y actualizar imports. Commit `62c8b25`.
- [x] Corregir las declaraciones de paquete de `IndexController` y `PolimorfismoController` a `py.edu.uc.lp3.rest.controller`.
- [x] Separar `domain`, `rest/controller`, `service`, `repository` y `exception`.
- [x] Confirmar que los controllers de CRUD no accedan directamente a repositorios.

### 1.2. Controllers y servicios

- [x] Delegar CRUD de `MobHostilController` a `MobHostilService`. Commit `f4e0777`.
- [x] Delegar búsqueda de mobs de `ExplosivoController` a `MobHostilService`. Commit `f4e0777`.
- [x] Trasladar a `MobHostilService.obtenerExplosivo(UUID)` la comprobación del tipo `Explosivo`, evitando que el controller decida la validez de la acción. Commit local `1230a98`.
- [x] Verificar mediante `ExplosivoControllerTest` que consultar un Zombie como explosivo responde **HTTP 409**, `ACCION_ILEGAL`, con el mensaje esperado. Prueba incluida en las 33 y posteriores 36 pruebas satisfactorias.
- [x] Crear `JugadorService`, validar nombre y coordenadas antes de construir `Jugador` y hacer que `IndexController` lo utilice. Commit local `efb6f50`.
- [x] Eliminar `MobHostil.buscarObjetivo()` duplicado; conservar la búsqueda en `MobHostilService`. Commit local `efb6f50`.
- [x] Comprobar la búsqueda de objetivos mediante pruebas de `MobHostilServiceTest`.

### 1.3. Interfaces, herencia y polimorfismo

- [x] Confirmar que `MobHostil` implementa `Atacante`; `Creeper` y `Zombie` lo heredan.
- [x] Demostrar `List<Atacante>`, `getFuerzaAtaque()` y `tipoAtaque()` mediante `GET /api/minecraft/polimorfismo/tipos-ataque` (verificación HTTP del 20/11/2026).
- [x] Confirmar, conforme al PDF de POO-08, que la interfaz debía tener al menos dos implementaciones.
- [x] Implementar `TNT` como segunda implementación de `Explosivo`, además de `Creeper`. Commit `c1b271b`.
- [x] Exponer demostración polimórfica de ambas implementaciones mediante `GET /api/minecraft/polimorfismo/explosivos`.
- [x] Agregar pruebas de `Explosivo` y comprobar que TNT no explota apagada ni vuelve a explotar después de usarse.

### 1.4. Sobrecarga y comportamiento HTTP

- [x] Verificar `Creeper(Vector3D)` y `Creeper(Vector3D, double tiempoDetonacion)`.
- [x] Verificar `Zombie(Vector3D)` y `Zombie(Vector3D, double velocidad)`; el primero delega al segundo.
- [x] Confirmar que el POST de Creeper admite tiempo de detonación opcional y selecciona el constructor correspondiente.
- [x] Revisar las dos firmas `acercarse(Entidad)` y `acercarse(Entidad, double)`.
- [x] Implementar `MobHostilService.simularAcercamientoCreeper(double)` y `AcercamientoController` para hacer la sobrecarga observable por HTTP.
- [x] Probar `GET /api/minecraft/creeper/acercamiento?distanciaMaxima=2`: sin límite `(3,64,0)`; con límite `(0,64,0)`.
- [x] Probar `GET /api/minecraft/creeper/acercamiento?distanciaMaxima=4`: ambos `(3,64,0)`.
- [x] Agregar `AcercamientoControllerTest` con los dos casos; suite completa de **36 pruebas** aprobada.
- [ ] Revisar si es necesario hacer observable también por HTTP la sobrecarga de **Zombie**; existe en Java, pero no está expuesta explícitamente como opción de creación.
- [ ] Confirmar con una prueba específica que el JSON de creación de Creeper evidencia de manera suficiente cuál constructor se usó; el DTO actual no devuelve el tiempo de detonación.

### 1.5. Errores HTTP y validación de jugador

- [x] Confirmar `HTTP 400` y `ARGUMENTO_INVALIDO` ante nombre vacío en `GET /api/minecraft/jugador` (comprobado el 20/11/2026).
- [x] Conservar los handlers de `IllegalArgumentException` (400), `MobNoEncontradoException` (404) y `AccionIlegalException` (409).
- [x] Agregar `JugadorServiceTest` con creación válida y rechazo de nombre y coordenadas inválidos. Commit local `e213d17`.
- [x] Retirar la comprobación de nombre del constructor base `Entidad` para el caso de creación de jugador, conservando las comprobaciones existentes en `Jugador` y `JugadorService`. Commit local `60c8f88`.
- [x] Agregar prueba que comprueba el rechazo de nombres vacíos y nulos al construir directamente un `Jugador`. Commit local `60c8f88`.
- [ ] Evaluar si otras subclases futuras podrían recibir nombres externos y requerir validación específica en su servicio; no generalizar el cambio sin revisar invariantes.

### 1.6. Diagramas y licencia

- [x] Actualizar el diagrama general con `Vaca` y `AldeanoArmero` y sus relaciones de herencia. Commit `7d54bbd`.
- [x] Reflejar `Explosivo`, `Atacante`, `Creeper`, `TNT` y las relaciones relevantes en los diagramas.
- [x] Actualizar el texto sobre `emitirSonido()` y sus sobreescrituras.
- [x] Verificar el archivo `LICENSE`: contiene **Apache License, Version 2.0**.
- [x] Mencionar la licencia Apache 2.0 en el README.
- [ ] Ajustar la nota antigua del README que todavía dice que verificar el contenido de `LICENSE` está pendiente.
- [ ] Incorporar al README el nuevo endpoint de acercamiento y las correcciones recientes.

---

## 2. Pruebas unitarias de `ExplosionService`

### 2.1. Pruebas iniciales

- [x] Crear `src/test/java/py/edu/uc/lp3/service/ExplosionServiceTest.java`.
- [x] Probar explosión válida y daño al objetivo.
- [x] Probar objetivo fuera del radio de explosión.
- [x] Rechazar UUID nulos.
- [x] Rechazar un Zombie como origen.
- [x] Rechazar un mob inexistente.
- [x] Rechazar un Creeper como su propio objetivo.
- [x] Verificar **6 pruebas aprobadas** en la primera etapa. Commit `99acb46`.

### 2.2. Pruebas adicionales

- [x] Rechazar Creeper muerto.
- [x] Rechazar Creeper que ya está detonando.
- [x] Rechazar objetivo muerto.
- [x] Rechazar Creeper que ya explotó.
- [x] Priorizar la verificación de `yaExploto()` para conservar el mensaje `El Creeper ya exploto`.
- [x] Verificar que las operaciones rechazadas no llamen a `save()` en los repositorios simulados.
- [x] Confirmar **10 pruebas aprobadas** de `ExplosionServiceTest` (20/11/2026).

**Límite:** las pruebas anteriores usan Mockito; no sustituyen pruebas de integración con H2. Los tests HTTP posteriores sí ejercitan el contexto de Spring y el endpoint indicado.

---

## 3. Refactorización de las reglas de explosión

### 3.1. Cálculo del daño — completado

- [x] Trasladar el cálculo desde `Creeper.calcularDanoExplosion()` a `ExplosionService`.
- [x] Eliminar el cálculo duplicado en `Creeper`.
- [x] Conservar fuerza del Creeper **17** y radio máximo **4 bloques**.
- [x] Mantener daño cero fuera del radio y decrecimiento proporcional dentro.
- [x] Ejecutar pruebas tras el cambio. Commit `286ac22`.

Fórmula en el servicio:

```java
double radio = 4.0;
double fuerzaAtaque = creeper.getFuerzaAtaque();
double dano = distancia > radio
        ? 0
        : Math.max(0, fuerzaAtaque * (1 - distancia / radio));
```

### 3.2. Validaciones de explosión — implementadas en el servicio

- [x] Validar UUID y existencia de origen y objetivo.
- [x] Rechazar origen distinto de Creeper o que coincide con el objetivo.
- [x] Validar explícitamente Creeper ya explotado, mobs muertos y Creeper detonando.
- [x] Mantener el mensaje específico de explosión repetida.
- [x] Verificar las validaciones mediante pruebas de `ExplosionService`.
- [x] Reordenar **solo la indentación** del bloque de validaciones y el cálculo; comprobado mediante `git diff -w --ignore-blank-lines --exit-code`. Cambio guardado en el commit local `4a9b7c2` (`style: ordenar validaciones de ExplosionService`).

### 3.3. Comportamiento de `Creeper` que debe conservarse

- `ExplosionService` llama a `comenzarDetonacion()` y `explotar()`.
- `Creeper` implementa `Explosivo` mediante `puedeExplotar()` y `explotar()`.
- `Creeper.tick()` llama a `explotar()` al cumplirse el tiempo de detonación.
- [x] Agregar la prueba `creeperExplotaDespuesDeTresTicks()`: explota al tercer tick, queda muerto y no repite explosión. Commit local `7b979cd`.
- [x] Confirmar **34 pruebas completas aprobadas** tras agregar la prueba anterior.
- [ ] Revisar definitivamente con la observación del profesor qué reglas deben residir en el servicio y qué operaciones de estado/polimorfismo deben permanecer en la entidad.
- [ ] Resolver sin romper la interfaz `Explosivo` ni la detonación mediante `tick()` la separación de responsabilidades pendiente.
- [ ] Repetir las pruebas tras cualquier modificación adicional de `Creeper` o `ExplosionService`.

**No repetir el traslado del cálculo de daño: ya está terminado.**

### 3.4. Verificaciones históricas

- [x] Confirmar **17/17** pruebas aprobadas tras la primera refactorización (20/11/2026).
- [x] Confirmar **26/26** pruebas aprobadas en la revisión posterior documentada en el README (20/11/2026).
- [x] Confirmar **33/33** tras `ExplosivoControllerTest`.
- [x] Confirmar **34/34** tras la prueba de tres ticks.
- [x] Confirmar **36/36** tras las pruebas de acercamiento, según la ejecución local informada por el estudiante.

---

## 4. Incidente de H2 durante las pruebas

### 4.1. Incidente histórico

Una ejecución de `./mvnw clean test` falló con `Unable to determine Dialect without JDBC metadata`: 13 pruebas ejecutadas, 7 errores, `BUILD FAILURE`.

### 4.2. Diagnóstico y solución comprobada

La aplicación Spring Boot y la suite Maven estaban utilizando simultáneamente la base persistente `jdbc:h2:file:./data/minecraft`. Se consideró probable un conflicto de acceso al archivo.

- [x] Detener Spring Boot con `Ctrl + C`.
- [x] Repetir `./mvnw clean test` y obtener `BUILD SUCCESS`.
- [x] Comprobar que no hacía falta modificar propiedades, borrar `data/`, configurar manualmente el dialecto ni revertir código.

**Recordatorio:** detener Spring Boot antes de ejecutar pruebas que acceden a la misma base H2 persistente.

---

## 5. Código revisado y cambios recientes

### 5.1. Archivos revisados

`Entidad.java`, `SerVivo.java`, `MobHostil.java`, `Creeper.java`, `Zombie.java`, `Jugador.java`, `Vector3D.java`, `Atacante.java`, `Explosivo.java`, `TNT.java`, `SimulacionExplosion.java`, `MobHostilService.java`, `ExplosionService.java`, `JugadorService.java`, `IndexController.java`, `ExplosivoController.java`, `PolimorfismoController.java`, `AcercamientoController.java` y las pruebas correspondientes.

### 5.2. Cambios completados durante esta sesión

- [x] Eliminar búsqueda duplicada de `MobHostil` y mantenerla en `MobHostilService`.
- [x] Crear `JugadorService` y delegar desde `IndexController`.
- [x] Agregar `JugadorServiceTest` (creación válida, datos inválidos y constructor directo).
- [x] Eliminar la validación duplicada de nombre en `Entidad` para el caso actual, conservando la de `Jugador`.
- [x] Agregar `SerVivoTest` (salud máxima y regeneración inválida).
- [x] Pasar validación de `ExplosivoController` al servicio y probar HTTP 409.
- [x] Añadir prueba de detonación tras tres ticks.
- [x] Implementar demostración HTTP de `acercarse()` con límites 2 y 4.
- [x] Añadir dos pruebas de `AcercamientoControllerTest`.

---

## 6. Observaciones todavía pendientes

### 6.1. Validaciones y excepciones del dominio

- [x] Inspeccionar `Entidad`, `SerVivo`, `MobHostil`, `Creeper` y sus servicios relacionados.
- [x] Identificar y eliminar la lógica duplicada de búsqueda de objetivos.
- [x] Trasladar al servicio la validación del nombre del jugador al crear por HTTP.
- [x] Añadir tests para proteger la salud máxima y la regeneración.
- [ ] Revisar y trasladar, donde corresponda, las validaciones y excepciones que **todavía** permanecen en `Entidad`, `SerVivo`, `MobHostil`, `Creeper` y `Zombie`.
- [ ] Revisar `recibirDano()`, `curar()`, `cambiarSalud()`, `aplicarRegeneracion()` y sus invariantes antes de eliminar controles de entidades.
- [ ] Evaluar el impacto sobre las demás clases que heredan de `Entidad` o `SerVivo` (incluidos `Jugador` y `Ghast`).
- [ ] Evitar que una refactorización permita estados inválidos o rompa JPA, herencia o polimorfismo.
- [ ] Confirmar que los errores HTTP sigan siendo informativos y correspondan a sus códigos.

**Estado:** parcialmente resuelto; no marcar la corrección arquitectónica completa como cerrada todavía.

### 6.2. Reglas y estado de detonación

- [x] Trasladar el cálculo de daño a `ExplosionService`.
- [x] Validar los rechazos principales en el servicio.
- [x] Proteger `tick()` con prueba de tres ticks.
- [ ] Resolver la distribución definitiva de responsabilidades de `explotar()`, `puedeExplotar()` y `tick()` conforme al PDF.

### 6.3. `Explosivo` y polimorfismo

- [x] Confirmar que el profesor pidió dos implementaciones.
- [x] Implementar `TNT` además de `Creeper`.
- [x] Mostrar ambas mediante `List<Explosivo>` y `GET /api/minecraft/polimorfismo/explosivos`.
- [x] Delegar validación de `ExplosivoController` al servicio.
- [x] Probar que el Zombie produce HTTP 409.

**Estado de esta observación:** implementada y probada.

### 6.4. Sobrecargas observables por HTTP

- [x] Verificar constructores sobrecargados de Creeper y Zombie.
- [x] Implementar `AcercamientoController` y comprobar ambas variantes de `acercarse()` con 2 y 4 bloques.
- [x] Incorporar dos tests del nuevo endpoint.
- [x] Confirmar la suite de 36 pruebas con `BUILD SUCCESS`.
- [ ] Verificar explícitamente la observabilidad por HTTP de las dos firmas de Zombie, si se desea demostrar también esa sobrecarga.

### 6.5. Diagramas y licencia

- [x] Actualizar `Vaca`, `AldeanoArmero`, interfaces y relaciones de herencia.
- [x] Actualizar documentación de `emitirSonido()`.
- [x] Verificar Apache License 2.0 y su mención en el README.
- [ ] Eliminar del README la antigua nota de licencia «pendiente de verificar» y actualizarlo para los cambios de esta sesión.

### 6.6. Verificación y documentación finales

- [ ] Resolver los pendientes arquitectónicos antes de declarar cerrada toda la corrección de POO-08.
- [ ] Ejecutar nuevamente `./mvnw clean test` **después** de los cambios finales.
- [ ] Revisar HTTP 400, 404 y 409; Swagger y los endpoints de simulación.
- [ ] Ejecutar `git diff --check`, `git status` y confirmar los commits.
- [ ] Actualizar README, `BITACORA.md`, esta lista y las especificaciones de entrega.
- [ ] Subir la rama cuando el trabajo esté probado y se decida publicarlo.

---

## 7. Historial de Git

**Repositorio:** `NicoRodriguez85677/NRodriguez-taller-git-2026`.

### 7.1. Correcciones iniciales

| Commit | Descripción |
|---|---|
| `62c8b25` | Reorganización de dominio a `domain` |
| `f4e0777` | Controllers delegan operaciones a servicios |
| `5a93ac01` | Inicio del seguimiento |
| `99acb46` | Pruebas iniciales de `ExplosionService` |
| `2f27249` | Actualización del seguimiento |
| `286ac22` | Traslado del cálculo y validaciones de explosión al servicio |
| `220e046` | Delegación de simulación de Ghast y pruebas de búsqueda |
| `c1b271b` | TNT y polimorfismo con `Explosivo` |
| `7d54bbd` | Diagramas y documentación de POO-06 y POO-08 |

### 7.2. Rama POO-09 y correcciones locales

| Commit | Descripción |
|---|---|
| `b44e50c` | OpenAPI inicial en la rama `feat/poo09-openapi-2026` (ya publicado en GitHub) |
| `efb6f50` | Delegar creación de jugador al servicio y eliminar búsqueda duplicada |
| `e213d17` | Pruebas iniciales de `JugadorService` |
| `60c8f88` | Validación del nombre en servicio y prueba del constructor directo |
| `a7e7d0b` | Pruebas de regeneración y salud máxima |
| `1230a98` | Delegar comprobación de `Explosivo` al servicio y prueba HTTP 409 |
| `7b979cd` | Prueba de detonación después de tres ticks |
| `4a9b7c2` | Limpieza de formato de `ExplosionService` (`style: ordenar validaciones de ExplosionService`) |
| `7eff864` | Endpoint y pruebas de acercamiento (`feat: demostrar sobrecarga de acercarse mediante HTTP`) |

**Estado reportado:** rama local `feat/poo09-openapi-2026`, con commits pendientes de `git push`; los commits posteriores a `b44e50c` aún no aparecen en GitHub. El último `git status` confirmado antes de los dos commits más recientes estaba limpio. Los hashes de los ocho commits locales recientes fueron confirmados mediante `git log -8 --oneline`; `HEAD` apunta a `7eff864`. Revisar `git status` después de actualizar este documento.

**No hacer `git push` hasta decidir que la rama está lista para compartir con los compañeros.**

---

## 8. Estado actual del proyecto

### 8.1. Pruebas

**Última ejecución completa informada por el estudiante:**

```powershell
.\mvnw.cmd clean test
```

- **36 pruebas aprobadas**.
- **0 fallos y 0 errores**.
- **BUILD SUCCESS**.
- Última ampliación: 2 pruebas de `AcercamientoControllerTest`.
- Pruebas específicas realizadas: `JugadorServiceTest`, `SerVivoTest`, `ExplosivoControllerTest`, `ExplosivoTest` y `AcercamientoControllerTest`.

### 8.2. HTTP comprobado

- `GET /api/minecraft/polimorfismo/tipos-ataque` — polimorfismo de `Atacante`.
- `GET /api/minecraft/polimorfismo/explosivos` — polimorfismo de Creeper y TNT.
- `GET /api/minecraft/creeper/acercamiento?distanciaMaxima=2` — una variante se acerca y la limitada no.
- `GET /api/minecraft/creeper/acercamiento?distanciaMaxima=4` — ambas se acercan.
- `GET /api/minecraft/explosivos/{id}` con Zombie — HTTP 409 por prueba de controlador.

### 8.3. POO-09: trabajo adelantado

- [x] Crear rama `feat/poo09-openapi-2026`.
- [x] Preparar dependencia `springdoc-openapi-starter-webmvc-ui` y configuración `OpenApiConfig` en GitHub (`b44e50c`).
- [x] Incluir anotaciones iniciales de OpenAPI para CRUD y simulación de explosiones.
- [ ] Revisar OpenAPI real `http://localhost:8080/v3/api-docs` y `http://localhost:8080/swagger-ui/index.html`, incluida la descripción de las reglas y los errores.
- [ ] Completar README, bitácora y colección Insomnia de POO-09.
- [ ] Configurar e implementar clientes HTTP de A2 y A3 **a partir de sus OpenAPI reales**.
- [ ] Implementar el escenario Minecraft propio que consulta ambas APIs, guarda en H2 solo cuando ambas aceptan y atribuye el rechazo sin persistir.
- [ ] Probar camino feliz, rechazo A2 y rechazo A3, y documentar las tres URLs del grupo.

**No afirmar que el escenario de APIs cruzadas ya funciona: la integración real sigue pendiente.**

---

## 9. Próximo trabajo

### Antes de reunirse con el grupo

1. Resolver las responsabilidades y validaciones de dominio que siguen pendientes conforme a la corrección del profesor.
2. Completar la revisión de `Creeper.explotar()`, `puedeExplotar()` y `tick()` sin romper el comportamiento probado.
3. Verificar que OpenAPI y Swagger describan las rutas y reglas efectivas.
4. Actualizar README, bitácora, seguimiento de correcciones y documentación de POO-08.
5. Preparar la arquitectura de clientes y persistencia condicional, sin inventar rutas o cuerpos de A2 y A3.
6. Ejecutar pruebas finales y revisar Git.

### Con el grupo

1. Confirmar nombres, repositorios, puertos y OpenAPI de A2 y A3.
2. Leer/importar sus especificaciones y decidir qué regla consulta cada cliente.
3. Conectar las llamadas reales.
4. Probar y registrar éxito, rechazo de A2 y rechazo de A3.
5. Preparar colección Insomnia y entrega individual con enlace exacto al commit.

---

## 10. Reglas para continuar el trabajo

- No repetir correcciones ya verificadas.
- No solicitar archivos completos previamente revisados salvo necesidad concreta.
- Antes de programar, comprobar si la funcionalidad ya existe.
- Hacer cambios pequeños y ejecutar las pruebas correspondientes.
- Mantener las invariantes de dominio, compatibilidad JPA y polimorfismo.
- No eliminar validaciones de entidades antes de comprobar qué servicio las sustituye y qué consumidores dependen de ellas.
- Detener Spring Boot antes de ejecutar la suite Maven con H2 persistente.
- No confundir cambios locales con cambios publicados en GitHub.
- Registrar hashes de commits confirmados; no inventar hashes de commits que todavía no se hayan consultado.
- No dar por cerrada la corrección de POO-08 mientras queden observaciones sin verificar.

---

## 11. Punto exacto para retomar

**Trabajo recientemente terminado:** prueba y commit `7eff864` de la demostración HTTP de `acercarse()`, suite Maven con 36 pruebas aprobadas y hashes locales confirmados hasta `HEAD`.

**Próximo paso recomendado:** guardar esta versión del seguimiento, ejecutar `git diff --check` y `git status --short`, y crear un commit exclusivo de documentación si solo está modificado `CORRECCIONES-POO06-POO08.md`. Después actualizar el README de POO-08/POO-09 y continuar con las observaciones arquitectónicas.

**Última suite reportada:** **36/36**, `BUILD SUCCESS`. **No repetir pruebas solo por editar documentación**, salvo que se modifique código.
