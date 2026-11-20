
# Seguimiento de correcciones POO-06 y POO-08

## Referencias

- Corrección POO-06 - Nicolás Rodríguez.pdf
- Corrección POO-08 - Nicolás Rodríguez.pdf

## Objetivo

Corregir las observaciones del profesor correspondientes a POO-06 y POO-08 antes de continuar con POO-09.

Trabajar paso a paso, verificar cada cambio y evitar repetir revisiones o modificaciones ya realizadas.

---

## 1. Correcciones realizadas y verificadas

### 1.1. Organización del dominio

- [x] Reorganizar las clases del dominio en `py.edu.uc.lp3.domain`.
  - Se trasladaron las clases al paquete correspondiente.
  - Se actualizaron las referencias e imports.
  - Las pruebas Maven aprobaron.
  - Commit: `62c8b25`.

### 1.2. Separación entre controllers y servicios

- [x] Delegar las operaciones CRUD de `MobHostilController` a `MobHostilService`.
  - Se eliminó el acceso directo al repositorio desde el controller.
  - Commit: `f4e0777`.

- [x] Reemplazar el acceso directo al repositorio en `ExplosivoController`.
  - El controller utiliza `MobHostilService` para obtener los mobs.
  - Commit: `f4e0777`.

- [x] Verificar que los controllers no accedan directamente a repositorios.

### 1.3. Paquetes de los controllers

- [x] Verificar `IndexController`.
  - Paquete: `py.edu.uc.lp3.rest.controller`.

- [x] Verificar `PolimorfismoController`.
  - Paquete: `py.edu.uc.lp3.rest.controller`.

### 1.4. Interfaz compartida y polimorfismo

- [x] Verificar el uso de una interfaz compartida.
  - Interfaz: `Atacante`.
  - `MobHostil` implementa `Atacante`.
  - `Creeper` y `Zombie` implementan indirectamente `Atacante` por herencia.

- [x] Verificar el polimorfismo mediante HTTP.
  - Controller: `PolimorfismoController`.
  - Utiliza `List<Atacante>`.
  - Invoca `getFuerzaAtaque()` y `tipoAtaque()` mediante la interfaz.
  - Endpoint: `GET /api/minecraft/polimorfismo/tipos-ataque`.
  - Prueba HTTP exitosa el 20/11/2026.
  - Creeper: fuerza 17.0, tipo Explosion.
  - Zombie: fuerza 3.0, tipo Golpe cuerpo a cuerpo.

- [ ] Confirmar si la observación del profesor requiere específicamente una segunda implementación de `Explosivo`.
  - Actualmente se demostró polimorfismo mediante `Atacante`.
  - No dar por resuelta automáticamente la observación específica sobre `Explosivo`.

### 1.5. Sobrecarga de constructores

- [x] Verificar la sobrecarga de constructores en `Zombie`.
  - `Zombie(Vector3D posicion)`.
  - `Zombie(Vector3D posicion, double velocidad)`.
  - El primero delega mediante `this(posicion, 1.0)`.
  - Se conserva el constructor protegido requerido por JPA.
  - No fue necesario modificar `Zombie.java`.

- [x] Verificar los constructores sobrecargados de `Creeper`.
  - Constructor con posición.
  - Constructor con posición y tiempo de detonación.

- [ ] Verificar que las sobrecargas sean observables mediante HTTP.
  - Revisar la demostración de los constructores de `Creeper`.
  - Revisar la sobrecarga de `acercarse()`.
  - Agregar o adaptar un endpoint solamente si es necesario.

### 1.6. Manejo de parámetros inválidos

- [x] Verificar HTTP 400 para nombres inválidos.
  - Controller: `IndexController`.
  - Endpoint: `GET /api/minecraft/jugador?nombre=%20&x=0&y=64&z=0`.
  - Resultado: HTTP 400.
  - Error: `ARGUMENTO_INVALIDO`.
  - Mensaje: `El nombre no puede estar vacio`.
  - Respuesta JSON comprobada en navegador el 20/11/2026.
  - No fue necesario modificar el controller ni el manejador de excepciones.

---

## 2. Pruebas unitarias de ExplosionService

### 2.1. Pruebas iniciales

- [x] Crear el paquete `py.edu.uc.lp3.service` para pruebas.

- [x] Crear `ExplosionServiceTest.java`.
  - Ruta: `src/test/java/py/edu/uc/lp3/service/ExplosionServiceTest.java`.

- [x] Implementar seis pruebas iniciales:
  1. Explosión exitosa con daño al objetivo.
  2. Objetivo fuera del radio de explosión.
  3. Rechazo de identificadores nulos.
  4. Rechazo de un Zombie como origen.
  5. Rechazo de un mob inexistente.
  6. Rechazo de un Creeper que intenta atacarse a sí mismo.

- [x] Ejecutar las seis pruebas.
  - Comando: `.\mvnw.cmd -Dtest=ExplosionServiceTest test`.
  - Resultado: 6 pruebas aprobadas.
  - Fallos: 0.
  - Errores: 0.
  - BUILD SUCCESS.

- [x] Guardar las pruebas en Git.
  - Commit: `99acb46`.
  - Mensaje: `test: agregar pruebas unitarias de ExplosionService`.

### 2.2. Pruebas adicionales

- [x] Agregar pruebas para condiciones de explosión.
  - Creeper muerto.
  - Creeper que ya está detonando.
  - Objetivo muerto.
  - Creeper que ya explotó.

- [x] Verificar el rechazo de operaciones inválidas.
  - No deben guardarse cambios cuando se rechaza la explosión.
  - Se comprueba el comportamiento mediante repositorios simulados.

- [x] Corregir el orden de validación para el Creeper que ya explotó.
  - Inicialmente una prueba falló porque devolvía:
    `Los mobs deben estar vivos`.
  - El mensaje esperado era:
    `El Creeper ya exploto`.
  - Se corrigió el orden de las condiciones.
  - La prueba volvió a aprobar.

- [x] Ejecutar las diez pruebas de `ExplosionService`.
  - Resultado: 10 pruebas aprobadas.
  - Fallos: 0.
  - Errores: 0.
  - BUILD SUCCESS.

**Importante:** estas pruebas utilizan Mockito para simular los repositorios. No sustituyen una prueba de integración real con H2.

---

## 3. Refactorización de las reglas de explosión

### 3.1. Cálculo del daño

- [x] Trasladar el cálculo del daño desde `Creeper` a `ExplosionService`.

Anteriormente se utilizaba:

`creeper.calcularDanoExplosion(objetivo)`

Ahora el cálculo se realiza directamente en el servicio:

```java
double radio = 4.0;
double fuerzaAtaque = creeper.getFuerzaAtaque();

double dano = distancia > radio
        ? 0
        : Math.max(0, fuerzaAtaque * (1 - distancia / radio));
```

- [x] Conservar el comportamiento original.
  - Fuerza de ataque del Creeper: 17.
  - Radio de explosión: 4 bloques.
  - El daño disminuye proporcionalmente con la distancia.
  - Fuera del radio, el daño es cero.

- [x] Eliminar el método duplicado `calcularDanoExplosion()` de `Creeper.java`.

- [x] Verificar que las pruebas sigan aprobando después del traslado.

### 3.2. Validaciones de explosión

- [x] Incorporar la consulta `yaExploto()` en `Creeper`.

- [x] Validar explícitamente en `ExplosionService` que el Creeper no haya explotado anteriormente.

- [x] Diferenciar las condiciones de rechazo.
  - Creeper muerto.
  - Creeper detonando.
  - Creeper que ya explotó.
  - Objetivo muerto.

- [x] Verificar las condiciones mediante pruebas unitarias.

- [x] Mantener el comportamiento existente de los métodos utilizados por el servicio.

### 3.3. Dependencias que deben conservarse

Los siguientes métodos y relaciones ya fueron identificados:

- `ExplosionService` utiliza `comenzarDetonacion()` y `explotar()`.
- `Creeper.tick()` llama internamente a `explotar()`.
- `ExplosivoController` utiliza `puedeExplotar()`.
- La interfaz `Explosivo` declara `explotar()` y `puedeExplotar()`.

No eliminar ni cambiar estos métodos sin considerar sus dependencias.

### 3.4. Verificación completa

- [x] Ejecutar `.\mvnw.cmd clean test`.
- [x] Confirmar 17 pruebas ejecutadas.
- [x] Confirmar 17 pruebas aprobadas.
- [x] Confirmar 0 fallos.
- [x] Confirmar 0 errores.
- [x] Confirmar BUILD SUCCESS.

Fecha: 20/11/2026.

### 3.5. Git

- [x] Verificar `git diff --check`.
- [x] Corregir una advertencia de indentación en `Creeper.java`.
- [x] Verificar nuevamente el formato.
- [x] Crear commit de la refactorización.

Commit: `286ac22`.

Mensaje:

`refactor: trasladar calculo y validaciones de explosion al servicio`

Archivos incluidos:

- `CORRECCIONES-POO06-POO08.md`
- `src/main/java/py/edu/uc/lp3/domain/Creeper.java`
- `src/main/java/py/edu/uc/lp3/service/ExplosionService.java`
- `src/test/java/py/edu/uc/lp3/service/ExplosionServiceTest.java`

- [x] Confirmar que el commit se realizó correctamente.
- [x] Confirmar que Git quedó sin cambios pendientes.

**Estado:** refactorización del cálculo y de las validaciones específicas de explosión completada y guardada en Git.

**Importante:** esto no significa que todas las observaciones de arquitectura de POO-08 estén resueltas. Todavía falta revisar otras responsabilidades del dominio.

---

## 4. Problema de H2 durante las pruebas

### 4.1. Error encontrado

- [x] Identificar un error de inicialización de Spring Boot y Hibernate.

Durante una ejecución de:

`.\mvnw.cmd clean test`

Se obtuvo:

- 13 pruebas ejecutadas.
- 0 fallos.
- 7 errores.
- BUILD FAILURE.

Mensaje principal:

`Unable to determine Dialect without JDBC metadata`

### 4.2. Diagnóstico

La aplicación Spring Boot estaba ejecutándose simultáneamente con las pruebas Maven.

Ambas utilizaban la base H2 persistente:

`jdbc:h2:file:./data/minecraft`

La causa probable fue un conflicto de acceso al archivo de la base de datos.

### 4.3. Solución comprobada

- [x] Detener Spring Boot mediante Ctrl + C.
- [x] Repetir `.\mvnw.cmd clean test`.
- [x] Confirmar BUILD SUCCESS.
- [x] Confirmar que las pruebas aprobaron sin errores.

Posteriormente se ejecutaron nuevamente las pruebas completas:

- 17 pruebas aprobadas.
- 0 fallos.
- 0 errores.
- BUILD SUCCESS.

No fue necesario:

- Modificar `application.properties`.
- Configurar manualmente el dialecto de Hibernate.
- Borrar la carpeta `data`.
- Revertir la refactorización.

**Recordatorio:** antes de ejecutar las pruebas completas, detener cualquier instancia de Spring Boot que utilice la misma base H2.

---

## 5. Código ya revisado

Los siguientes archivos fueron inspeccionados durante las correcciones:

- `MobHostilService.java`
- `ExplosionService.java`
- `Creeper.java`
- `Entidad.java`
- `Zombie.java`
- `Atacante.java`
- `Explosivo.java`
- `PolimorfismoController.java`
- `IndexController.java`
- `ExplosivoController.java`
- `SimulacionExplosion.java`

No volver a solicitar estos archivos completos salvo que sea necesario para una modificación concreta.

**Nota:** `Creeper.java` y `ExplosionService.java` ya fueron modificados, probados y guardados en el commit `286ac22`.

---

## 6. Observaciones pendientes de POO-06 y POO-08

### 6.1. Validaciones y excepciones del dominio

- [ ] Revisar las validaciones que permanecen dentro de las clases del dominio.
- [ ] Identificar qué reglas deben trasladarse a servicios.
- [ ] Revisar específicamente `SerVivo` y `MobHostil`.
- [ ] Revisar las responsabilidades restantes de `Entidad`.
- [ ] Mantener las responsabilidades necesarias para JPA.
- [ ] Evitar duplicar validaciones innecesariamente.
- [ ] Conservar el funcionamiento de la herencia y el polimorfismo.
- [ ] Verificar el comportamiento HTTP de los errores.
- [ ] Ejecutar las pruebas después de modificar código.

**Estado:** pendiente.

### 6.2. Reglas de explosión

- [x] Trasladar el cálculo del daño a `ExplosionService`.
- [x] Eliminar el cálculo duplicado de `Creeper`.
- [x] Incorporar la consulta `yaExploto()`.
- [x] Agregar validación explícita para un Creeper que ya explotó.
- [x] Verificar las condiciones principales de rechazo.
- [x] Ejecutar 10 pruebas unitarias de explosión.
- [x] Ejecutar 17 pruebas completas del proyecto.
- [x] Registrar los cambios en Git.
  - Commit: `286ac22`.

Todavía falta:

- [ ] Revisar la distribución de responsabilidades de `explotar()`.
- [ ] Revisar la distribución de responsabilidades de `puedeExplotar()`.
- [ ] Revisar el comportamiento de `tick()` en relación con las reglas de juego.
- [ ] Determinar si hacen falta cambios adicionales para satisfacer la observación del profesor.

**No repetir el traslado del cálculo de daño. Ya está terminado.**

### 6.3. Interfaz Explosivo

- [x] Verificar el contrato de `Explosivo`.
  - `boolean explotar()`.
  - `boolean puedeExplotar()`.

- [x] Verificar el uso de `Atacante` con Creeper y Zombie.

- [ ] Confirmar si la corrección original exige específicamente dos clases que implementen `Explosivo`.
- [ ] Si corresponde, implementar una segunda clase compatible.
- [ ] Verificar el uso de la interfaz sin depender de una clase concreta.
- [ ] Probar el comportamiento.

### 6.4. Sobrecargas observables mediante HTTP

- [x] Verificar constructores sobrecargados en Zombie.
- [x] Verificar constructores sobrecargados en Creeper.

Pendiente:

- [ ] Verificar la demostración HTTP de los constructores.
- [ ] Revisar la sobrecarga de `acercarse()` de Creeper.
- [ ] Agregar o adaptar un endpoint solamente si es necesario.
- [ ] Probar las respuestas HTTP.

### 6.5. Diagramas

- [ ] Actualizar los diagramas con `Vaca`.
- [ ] Incorporar `AldeanoArmero`.
- [ ] Verificar las relaciones de herencia.
- [ ] Verificar las interfaces.
- [ ] Revisar la documentación de `emitirSonido()`.

### 6.6. Licencia

- [ ] Verificar el archivo `LICENSE`.
- [ ] Documentar la licencia Apache 2.0 en `README.md`.

### 6.7. Documentación y pruebas finales

- [ ] Revisar la documentación completa del proyecto.
- [ ] Ejecutar nuevamente `.\mvnw.cmd clean test` al finalizar todas las correcciones.
- [ ] Verificar los endpoints correspondientes.
- [ ] Ejecutar `git diff --check`.
- [ ] Revisar `git status`.
- [ ] Registrar los cambios nuevos mediante commits.
- [ ] Confirmar que las observaciones del profesor estén atendidas.

---

## 7. Historial de Git

**Repositorio:** `NRodriguez-taller-git-2026`

**Rama:** `fix/correcciones-poo06-poo08`

### Commits confirmados

| Commit | Descripción |
|---|---|
| `62c8b25` | Reorganización de clases del dominio |
| `f4e0777` | Controllers delegan operaciones a servicios |
| `5a93ac01` | Creación del seguimiento de correcciones |
| `99acb46` | Pruebas unitarias iniciales de ExplosionService |
| `2f27249` | Actualización del seguimiento de correcciones |
| `286ac22` | Traslado del cálculo y validaciones de explosión al servicio |

**Último commit confirmado:** `286ac22`.

### Estado de Git

Después del commit `286ac22` se comprobó:

- `git status --short`: sin archivos pendientes.
- `git log -1 --oneline`: muestra `286ac22`.
- Rama correcta: `fix/correcciones-poo06-poo08`.

La edición actual de este documento generará un nuevo cambio pendiente de guardar en Git.

No hace falta crear un commit exclusivo para cada anotación. Puede incluirse en el siguiente commit de trabajo.

**No hacer push hasta completar las correcciones y verificaciones.**

---

## 8. Estado actual del proyecto

**Fecha de última verificación:** 20/11/2026.

### Último resultado Maven

Comando:

`.\mvnw.cmd clean test`

Resultado:

- 17 pruebas ejecutadas.
- 17 pruebas aprobadas.
- 0 fallos.
- 0 errores.
- BUILD SUCCESS.

### Pruebas de explosión

Comando:

`.\mvnw.cmd -Dtest=ExplosionServiceTest test`

Resultado:

- 10 pruebas ejecutadas.
- 10 pruebas aprobadas.
- 0 fallos.
- 0 errores.
- BUILD SUCCESS.

### Última corrección de código completada

Traslado del cálculo del daño y validaciones específicas de explosión a `ExplosionService`.

Commit: `286ac22`.

### Refactorización pendiente

Revisión de las validaciones y excepciones restantes del dominio, así como de las responsabilidades de `explotar()`, `puedeExplotar()` y `tick()`.

---

## 9. Próximo trabajo

### Paso inmediato

Revisar las responsabilidades de las clases `SerVivo` y `MobHostil`.

Objetivos:

1. Identificar las validaciones que todavía permanecen en el dominio.
2. Determinar cuáles corresponden a los servicios.
3. Evitar modificaciones innecesarias en código ya corregido.
4. Mantener la compatibilidad con JPA y la herencia.
5. Implementar cambios pequeños.
6. Ejecutar pruebas antes de guardar nuevos commits.

### Después

1. Completar la revisión de las reglas restantes de explosión.
2. Resolver la observación específica sobre la interfaz `Explosivo`, si sigue pendiente.
3. Comprobar las sobrecargas mediante HTTP.
4. Actualizar los diagramas.
5. Documentar la licencia.
6. Ejecutar las pruebas finales.
7. Realizar los commits correspondientes.
8. Continuar con POO-09.

---

## 10. Reglas para continuar el trabajo

- No repetir correcciones ya verificadas.
- No solicitar nuevamente archivos completos que ya fueron revisados, salvo necesidad concreta.
- Antes de agregar una funcionalidad, comprobar si ya existe.
- Trabajar con cambios pequeños.
- Ejecutar pruebas después de modificar código.
- No hacer commits de código sin comprobar su funcionamiento.
- Mantener actualizado este archivo de seguimiento.
- No confundir errores de H2 con errores de lógica sin revisar primero la causa.
- Detener Spring Boot antes de ejecutar pruebas que utilizan la misma base H2 persistente.
- No hacer push hasta finalizar las correcciones.
- Registrar los commits confirmados.
- No dar por resueltas observaciones del profesor sin comprobar sus requisitos concretos.

---

## 11. Punto exacto para retomar

**Último commit:** `286ac22`.

**Últimas pruebas completas:** 17/17 aprobadas.

**Pruebas de ExplosionService:** 10/10 aprobadas.

**Git:** limpio después del último commit, antes de editar este documento.

**Trabajo ya terminado:**
- Organización del dominio.
- Delegación de controllers a servicios.
- Verificación de paquetes.
- Verificación de constructores sobrecargados.
- Demostración HTTP de `Atacante`.
- Verificación de HTTP 400.
- Pruebas iniciales y adicionales de explosión.
- Traslado del cálculo del daño a `ExplosionService`.
- Validaciones específicas de explosión.
- Commit `286ac22`.

**Trabajo inmediato pendiente:**
- Revisar validaciones y excepciones de `SerVivo` y `MobHostil`.
- Determinar qué responsabilidades deben trasladarse a servicios.

**No volver a realizar el traslado del cálculo de daño ni repetir las pruebas anteriores sin una razón concreta.**
