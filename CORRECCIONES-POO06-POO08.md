# Seguimiento de correcciones POO-06 y POO-08

## Referencias

- Corrección POO-06 - Nicolás Rodríguez.pdf
- Corrección POO-08 - Nicolás Rodríguez.pdf

## Correcciones realizadas

- [x] Reorganizar las clases del dominio en `py.edu.uc.lp3.domain`.
  - Commit: `62c8b25`.

- [x] Delegar el CRUD de `MobHostilController` a `MobHostilService`.
  - Commit: `f4e0777`.

- [x] Reemplazar el acceso directo al repositorio en `ExplosivoController`.
  - Commit: `f4e0777`.

- [x] Verificar los paquetes de `IndexController` y `PolimorfismoController`.
  - Ambos pertenecen a `py.edu.uc.lp3.rest.controller`.

- [x] Ejecutar pruebas Maven.
  - Resultado: 7/7 pruebas aprobadas después de las refactorizaciones realizadas.

- [x] Verificar que los controllers no accedan directamente a repositorios.
  - Comprobado después de las modificaciones.

- [x] Verificar una interfaz compartida por dos clases concretas.
  - Interfaz: `Atacante`.
  - `Creeper` y `Zombie` implementan `Atacante` mediante la herencia de `MobHostil`.
  - `PolimorfismoController` utiliza `List<Atacante>`.
  - Endpoint: `GET /api/minecraft/polimorfismo/tipos-ataque`.
  - Prueba HTTP realizada exitosamente el 20/11/2026.
  - Respuesta comprobada:
    - Creeper: fuerza de ataque 17.0, tipo Explosion.
    - Zombie: fuerza de ataque 3.0, tipo Golpe cuerpo a cuerpo.
  - Nota: verificar en la defensa si el profesor exige específicamente dos implementaciones de `Explosivo`, ya que la demostración actual utiliza `Atacante`.

- [x] Incorporar sobrecarga de constructores en otra clase.
  - Verificado: `Zombie` ya tiene dos constructores públicos.
  - `Zombie(Vector3D)` delega mediante `this(posicion, 1.0)`.
  - `Zombie(Vector3D, double)` permite configurar la velocidad.
  - Se conserva el constructor protegido requerido por JPA.
  - No requiere modificaciones adicionales.

- [x] Verificar respuesta HTTP 400 para parámetros inválidos.
  - Endpoint: `GET /api/minecraft/jugador?nombre=%20&x=0&y=64&z=0`.
  - Resultado: HTTP 400.
  - Error: `ARGUMENTO_INVALIDO`.
  - Mensaje: `El nombre no puede estar vacio`.
  - Respuesta JSON comprobada mediante navegador el 20/11/2026.
  - No requiere modificaciones adicionales.

## Código revisado

Los siguientes archivos ya fueron inspeccionados:

- `MobHostilService.java`
- `ExplosionService.java`
- `Creeper.java`
- `Entidad.java`
- `Zombie.java`
- `Atacante.java`
- `PolimorfismoController.java`
- `IndexController.java`

No volver a inspeccionar estos archivos completos salvo que sea necesario para una modificación concreta.

## Observaciones pendientes

- [ ] Resolver las validaciones y excepciones del dominio según POO-08.
  - Identificar las reglas de negocio que deben trasladarse a servicios.
  - Mantener las responsabilidades necesarias para la persistencia JPA.
  - Evitar romper los endpoints y pruebas existentes.

- [ ] Revisar la ubicación de las reglas de explosión.
  - Analizar `Creeper.explotar()`.
  - Analizar `Creeper.calcularDanoExplosion()`.
  - Trasladar las reglas de negocio correspondientes a `ExplosionService`.
  - Conservar el funcionamiento de la simulación y la persistencia H2.

- [ ] Verificar que la sobrecarga de constructores sea observable mediante HTTP.
  - Comprobar si existe un endpoint que permita demostrar el uso de diferentes constructores.
  - Revisar también la sobrecarga de `acercarse()` de `Creeper`.

- [ ] Actualizar los diagramas.
  - Incorporar `Vaca`.
  - Incorporar `AldeanoArmero`.
  - Verificar las relaciones de herencia e interfaces.
  - Revisar la documentación del polimorfismo de `emitirSonido()`.

- [ ] Documentar la licencia Apache 2.0 en README.
  - Verificar el archivo `LICENSE`.
  - Agregar una referencia clara a la licencia en `README.md`.

- [ ] Verificar la documentación y las pruebas finales.
  - Ejecutar `.\mvnw.cmd clean test`.
  - Verificar los endpoints modificados.
  - Comprobar que no existan errores de compilación.
  - Revisar `git diff --check`.
  - Verificar el estado de Git antes de realizar el commit final.

## Estado de Git

**Repositorio:** `NRodriguez-taller-git-2026`

**Rama:** `fix/correcciones-poo06-poo08`

**Último commit confirmado:** `5a93ac01`

**Descripción:** `docs: registrar seguimiento de correcciones POO-06 y POO-08`

Commits anteriores:

- `62c8b25` - Reorganización del dominio.
- `f4e0777` - Refactorización de controllers para utilizar servicios.
- `5a93ac01` - Creación del archivo de seguimiento.

Los cambios posteriores realizados en este documento todavía deben confirmarse mediante un nuevo commit.

**Importante:** no hacer push hasta completar las correcciones y verificaciones.

## Próximo trabajo

### Prioridad 1: reglas de negocio y validaciones

1. Comprobar las pruebas existentes relacionadas con explosiones.
2. Definir qué reglas deben trasladarse desde el dominio a los servicios.
3. Refactorizar únicamente las clases necesarias.
4. Ejecutar las pruebas Maven.
5. Verificar los endpoints afectados.
6. Registrar los cambios mediante Git.

### Prioridad 2: demostraciones HTTP

Verificar que las sobrecargas requeridas sean observables mediante endpoints.

### Prioridad 3: documentación

Actualizar diagramas, README y licencia.

### Prioridad 4: verificación final

Ejecutar todas las pruebas, revisar las diferencias de Git y confirmar que las observaciones del profesor estén atendidas.

---

**Criterio de trabajo:** avanzar paso a paso, verificar antes de modificar, evitar repetir correcciones ya realizadas y realizar commits pequeños después de comprobar cada cambio.

