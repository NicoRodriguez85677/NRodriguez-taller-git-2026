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
  - Se trasladaron las clases del dominio al paquete correspondiente.
  - Se actualizaron las referencias e imports.
  - Las pruebas Maven aprobaron.
  - Commit: `62c8b25`.

### 1.2. Separación entre controllers y servicios

- [x] Delegar las operaciones CRUD de `MobHostilController` a `MobHostilService`.
  - Se eliminó el acceso directo al repositorio desde el controller.
  - El controller utiliza el servicio para las operaciones CRUD.
  - Commit: `f4e0777`.

- [x] Reemplazar el acceso directo al repositorio en `ExplosivoController`.
  - El controller utiliza `MobHostilService` para obtener los mobs.
  - Commit: `f4e0777`.

- [x] Verificar que los controllers no accedan directamente a repositorios.
  - Comprobado después de las modificaciones.

### 1.3. Paquetes de los controllers

- [x] Verificar `IndexController`.
  - Paquete correcto: `py.edu.uc.lp3.rest.controller`.

- [x] Verificar `PolimorfismoController`.
  - Paquete correcto: `py.edu.uc.lp3.rest.controller`.

### 1.4. Interfaz compartida y polimorfismo

- [x] Verificar una interfaz compartida por dos clases concretas.
  - Interfaz: `Atacante`.
  - `MobHostil` implementa `Atacante`.
  - `Creeper` y `Zombie` implementan indirectamente `Atacante` por herencia.

- [x] Verificar el uso polimórfico mediante HTTP.
  - Controller: `PolimorfismoController`.
  - Utiliza `List<Atacante>`.
  - Invoca `getFuerzaAtaque()` y `tipoAtaque()` mediante la interfaz.
  - Endpoint: `GET /api/minecraft/polimorfismo/tipos-ataque`.
  - Prueba HTTP realizada exitosamente el 20/11/2026.
  - Resultado Creeper: fuerza 17.0, tipo Explosion.
  - Resultado Zombie: fuerza 3.0, tipo Golpe cuerpo a cuerpo.

**Observación:** la demostración actual utiliza `Atacante`. Todavía conviene confirmar si el profesor exige específicamente una segunda implementación de `Explosivo`, porque esa fue la interfaz señalada en la corrección original.

### 1.5. Sobrecarga de constructores

- [x] Verificar la sobrecarga de constructores en `Zombie`.
  - Constructor público: `Zombie(Vector3D posicion)`.
  - Constructor público: `Zombie(Vector3D posicion, double velocidad)`.
  - El primero delega mediante `this(posicion, 1.0)`.
  - Se conserva el constructor protegido requerido por JPA.
  - No fue necesario modificar `Zombie.java`.

- [x] Verificar que `Creeper` también posee constructores sobrecargados.
  - Constructor con posición.
  - Constructor con posición y tiempo de detonación.

### 1.6. Manejo de parámetros inválidos

- [x] Verificar respuesta HTTP 400 para nombres inválidos.
  - Controller: `IndexController`.
  - Endpoint: `GET /api/minecraft/jugador?nombre=%20&x=0&y=64&z=0`.
  - Resultado: HTTP 400.
  - Error: `ARGUMENTO_INVALIDO`.
  - Mensaje: `El nombre no puede estar vacio`.
  - Respuesta JSON comprobada en navegador el 20/11/2026.
  - No fue necesario modificar el controller ni el manejador de excepciones.

---

## 2. Pruebas unitarias de ExplosionService

- [x] Crear el paquete de pruebas `py.edu.uc.lp3.service`.

- [x] Crear `ExplosionServiceTest.java`.
  - Ruta: `src/test/java/py/edu/uc/lp3/service/ExplosionServiceTest.java`.

- [x] Implementar seis pruebas unitarias:
  1. Explosión exitosa con daño al objetivo.
  2. Objetivo fuera del radio de explosión.
  3. Rechazo de identificadores nulos.
  4. Rechazo de un Zombie como origen.
  5. Rechazo de un mob inexistente.
  6. Rechazo de un Creeper que intenta atacarse a sí mismo.

- [x] Ejecutar las seis pruebas antes de modificar las reglas de explosión.
  - Comando: `.\mvnw.cmd -Dtest=ExplosionServiceTest test`.
  - Resultado: 6 pruebas aprobadas.
  - Fallos: 0.
  - Errores: 0.
  - BUILD SUCCESS.

- [x] Guardar las pruebas en Git.
  - Commit: `99acb46`.
  - Mensaje: `test: agregar pruebas unitarias de ExplosionService`.

**Importante:** estas pruebas utilizan Mockito para simular los repositorios. No sustituyen las pruebas de integración con H2.

---

## 3. Refactorización de las reglas de explosión

### 3.1. Cálculo del daño

- [x] Trasladar el cálculo del daño desde `Creeper` a `ExplosionService`.

Antes, `ExplosionService` utilizaba:

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

- [x] Ejecutar las seis pruebas de `ExplosionService` después de trasladar el cálculo.
  - Resultado informado: sin errores.

- [x] Ejecutar todas las pruebas del proyecto después de eliminar el método.
  - Comando: `.\mvnw.cmd clean test`.
  - Resultado final: 13 pruebas aprobadas.
  - Fallos: 0.
  - Errores: 0.
  - BUILD SUCCESS.
  - Fecha: 20/11/2026.

**Estado:** traslado del cálculo de daño completado y verificado.

**Git:** los cambios de `ExplosionService.java` y `Creeper.java` todavía no tienen commit.

### 3.2. Condiciones de explosión

- [ ] Revisar las condiciones de `Creeper.explotar()`.
- [ ] Revisar las condiciones de `Creeper.puedeExplotar()`.
- [ ] Determinar qué decisiones deben trasladarse a `ExplosionService`.
- [ ] Conservar el contrato de la interfaz `Explosivo`.
- [ ] Preservar el comportamiento de `Creeper.tick()`.
- [ ] Verificar que los endpoints existentes sigan funcionando.
- [ ] Ejecutar nuevamente todas las pruebas.
- [ ] Realizar un commit cuando la refactorización esté completa.

**Dependencias ya identificadas:**

- `ExplosionService` utiliza `comenzarDetonacion()` y `explotar()`.
- `Creeper.tick()` llama internamente a `explotar()`.
- `ExplosivoController` utiliza `puedeExplotar()`.
- La interfaz `Explosivo` declara `explotar()` y `puedeExplotar()`.

No eliminar ni cambiar estos métodos sin considerar sus dependencias.

---

## 4. Problema de H2 durante las pruebas

- [x] Identificar un error de inicialización de Spring Boot y Hibernate.

Durante una ejecución de `.\mvnw.cmd clean test` se obtuvo:

- 13 pruebas ejecutadas.
- 0 fallos.
- 7 errores.
- BUILD FAILURE.

El mensaje principal fue:

`Unable to determine Dialect without JDBC metadata`

### Diagnóstico

La aplicación Spring Boot estaba ejecutándose simultáneamente con las pruebas Maven.

Ambas utilizaban la base H2 persistente:

`jdbc:h2:file:./data/minecraft`

La causa probable fue un conflicto de acceso al archivo de la base de datos.

### Solución comprobada

- [x] Detener Spring Boot mediante Ctrl + C.
- [x] Repetir `.\mvnw.cmd clean test`.
- [x] Confirmar BUILD SUCCESS.
- [x] Confirmar 13 pruebas aprobadas, sin fallos ni errores.

No fue necesario:

- Modificar `application.properties`.
- Configurar manualmente el dialecto de Hibernate.
- Borrar la carpeta `data`.
- Revertir los cambios de la refactorización.

**Recordatorio:** antes de ejecutar las pruebas completas, detener cualquier instancia de Spring Boot que esté utilizando la misma base H2.

---

## 5. Código revisado

Los siguientes archivos ya fueron inspeccionados:

- `MobHostilService.java`
- `ExplosionService.java`
- `Creeper.java`
- `Entidad.java`
- `Zombie.java`
- `Atacante.java`
- `Explosivo.java` (contrato identificado)
- `PolimorfismoController.java`
- `IndexController.java`
- `ExplosivoController.java`
- `SimulacionExplosion.java`

No volver a solicitar estos archivos completos salvo que sea necesario para una modificación concreta.

---

## 6. Observaciones pendientes de POO-06 y POO-08

### 6.1. Validaciones y excepciones del dominio

- [ ] Resolver las observaciones sobre validaciones y excepciones dentro del dominio.
- [ ] Identificar qué reglas deben trasladarse a servicios.
- [ ] Mantener las responsabilidades necesarias para la persistencia JPA.
- [ ] Evitar duplicar validaciones innecesariamente.
- [ ] Verificar el comportamiento HTTP de los errores.
- [ ] Ejecutar las pruebas después de cada modificación.

### 6.2. Reglas de explosión

- [x] Trasladar el cálculo del daño a `ExplosionService`.
- [x] Eliminar el cálculo duplicado de `Creeper`.
- [x] Verificar las 13 pruebas.
- [ ] Completar la revisión de las condiciones de explosión.
- [ ] Preservar el funcionamiento de `tick()`.
- [ ] Registrar la refactorización mediante Git.

### 6.3. Sobrecargas observables mediante HTTP

- [ ] Verificar que la sobrecarga de constructores sea observable mediante HTTP.
- [ ] Revisar la demostración de los constructores de `Creeper`.
- [ ] Revisar la sobrecarga de `acercarse()` de `Creeper`.
- [ ] Agregar o adaptar un endpoint únicamente si es necesario.

### 6.4. Diagramas

- [ ] Actualizar los diagramas con `Vaca`.
- [ ] Incorporar `AldeanoArmero`.
- [ ] Verificar las relaciones de herencia.
- [ ] Verificar las interfaces.
- [ ] Revisar la documentación de `emitirSonido()`.

### 6.5. Licencia

- [ ] Verificar el archivo `LICENSE`.
- [ ] Documentar la licencia Apache 2.0 en `README.md`.

### 6.6. Documentación y pruebas finales

- [ ] Revisar la documentación del proyecto.
- [ ] Ejecutar `.\mvnw.cmd clean test`.
- [ ] Verificar los endpoints modificados.
- [ ] Ejecutar `git diff --check`.
- [ ] Revisar `git status`.
- [ ] Realizar los commits pendientes.
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
| `99acb46` | Pruebas unitarias de ExplosionService |
| `2f27249` | Actualización del seguimiento de correcciones |

**Último commit confirmado:** `2f27249`.

### Cambios posteriores sin commit

- Modificación de `ExplosionService.java` para calcular el daño.
- Eliminación de `calcularDanoExplosion()` de `Creeper.java`.
- Actualización del presente documento, una vez guardada.

El estado exacto de Git debe verificarse nuevamente antes del siguiente commit.

**No hacer push hasta completar las correcciones y verificaciones.**

---

## 8. Estado actual del proyecto

**Fecha de última verificación:** 20/11/2026.

**Último resultado Maven:**

- 13 pruebas ejecutadas.
- 13 pruebas aprobadas.
- 0 fallos.
- 0 errores.
- BUILD SUCCESS.

**Última corrección de código completada:** traslado del cálculo del daño de explosión desde `Creeper` hacia `ExplosionService`.

**Refactorización todavía en curso:** condiciones de explosión y distribución de responsabilidades entre dominio y servicios.

---

## 9. Próximo trabajo

### Paso inmediato

Continuar con la revisión de las condiciones de explosión de `Creeper.explotar()` y `Creeper.puedeExplotar()`.

Ya se conocen sus dependencias, por lo que no es necesario repetir las búsquedas realizadas.

### Después

1. Completar las reglas de explosión en servicios.
2. Verificar las validaciones y excepciones del dominio.
3. Comprobar las sobrecargas mediante HTTP.
4. Actualizar los diagramas.
5. Documentar la licencia.
6. Ejecutar las pruebas finales.
7. Realizar los commits pendientes.
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
- No hacer push hasta finalizar las correcciones.

**Punto exacto para retomar:** revisar las condiciones de explosión, conservando `Explosivo` y el comportamiento de `Creeper.tick()`. El cálculo del daño ya fue trasladado y las 13 pruebas aprobaron.