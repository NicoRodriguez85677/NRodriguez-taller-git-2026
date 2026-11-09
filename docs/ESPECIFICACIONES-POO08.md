# Especificaciones POO-08 — Minecraft

**Estudiante:** Nicolás Rodríguez
**Materia:** Lenguajes de Programación 3
**Dominio:** Minecraft
**Tecnologías:** Java 21, Spring Boot 4.0.8, Spring Data JPA, H2 e Insomnia.

## 1. Objetivo

Extender el modelado POO de Minecraft para ofrecer un servicio HTTP REST que permita gestionar mobs hostiles mediante operaciones CRUD y ejecutar simulaciones de explosiones cuyos resultados se guardan en una base de datos H2.

## 2. Modelo persistido

Se utiliza la familia `MobHostil`, con dos clases hijas: `Creeper` y `Zombie`.

La jerarquía está mapeada mediante JPA utilizando `SINGLE_TABLE`. Los atributos comunes se heredan de `Entidad` y `SerVivo`.

También se persiste `SimulacionExplosion` para almacenar el historial de las explosiones.

## 3. Endpoints HTTP

| Método | URL | Función |
|---|---|---|
| POST | `/api/minecraft/mobs/creepers` | Crear Creeper |
| POST | `/api/minecraft/mobs/zombies` | Crear Zombie |
| GET | `/api/minecraft/mobs` | Listar mobs |
| GET | `/api/minecraft/mobs/{id}` | Buscar mob |
| PUT | `/api/minecraft/mobs/{id}` | Actualizar posición y salud |
| DELETE | `/api/minecraft/mobs/{id}` | Eliminar mob |
| GET | `/api/minecraft/explosivos/{id}` | Consultar interface Explosivo |
| POST | `/api/minecraft/simulaciones/explosiones` | Simular explosión |
| GET | `/api/minecraft/simulaciones/explosiones` | Consultar historial |
| GET | `/api/minecraft/simulaciones/explosiones/creeper/{id}` | Consultar historial de un Creeper |

La URL base es `http://localhost:8080`.

## 4. Reglas de negocio

**Regla 1 — Detonación:** un Creeper debe estar vivo, haber iniciado su detonación y no haber explotado anteriormente.

**Regla 2 — Daño:** la explosión aplica un daño proporcional a la distancia dentro de un radio de cuatro bloques.

Se utiliza la fórmula:

`daño = 17 × (1 - distancia / 4)`

Fuera del radio, el daño es cero.

La simulación registra los UUID de los participantes, la distancia, el daño aplicado y la salud anterior y posterior del objetivo.

## 5. Invariantes

- La salud debe estar entre cero y la salud máxima.
- No se aceptan valores de salud negativos o no finitos.
- El tiempo inicial de detonación debe ser positivo.
- Un Creeper muerto no puede explotar.
- Un Creeper no puede explotar dos veces.
- El origen de una simulación debe ser un Creeper.
- El objetivo debe existir y estar vivo.
- El Creeper no puede seleccionarse como su propio objetivo.
- El daño no puede reducir la salud por debajo de cero.
- Una simulación inválida no debe guardar cambios.

## 6. Excepciones

La excepción base es `MinecraftException`.

Sus dos clases hijas son:

- `MobNoEncontradoException`: HTTP 404 cuando no existe el UUID solicitado.
- `AccionIlegalException`: HTTP 409 cuando una acción contradice el estado del juego.

Los argumentos inválidos devuelven HTTP 400 mediante el handler correspondiente.

## 7. Persistencia

La base H2 utiliza:

`jdbc:h2:file:./data/minecraft`

Los datos se conservan entre reinicios. Hibernate utiliza `ddl-auto=update` y genera las tablas a partir de las entidades JPA.

La carpeta `data/` está excluida del repositorio Git.

## 8. Cómo ejecutar y probar

Desde la raíz del repositorio:

```bash
./mvnw spring-boot:run
```

En PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Para las pruebas automatizadas:

```bash
./mvnw clean test
```

Se recomienda detener el servidor antes de ejecutar las pruebas, ya que ambos procesos pueden intentar acceder a la misma base H2.

### Prueba manual con Insomnia

1. Abrir Insomnia e importar `insomnia/POO08-Minecraft.postman_collection.json`.
2. Ejecutar `01 Crear Creeper` y `02 Crear Zombie`.
3. Copiar los UUID a las variables de la colección.
4. Listar, consultar y actualizar mobs.
5. Consultar el endpoint de la interface `Explosivo`.
6. Ejecutar una simulación.
7. Consultar su historial y la salud restante.
8. Repetir la explosión para verificar HTTP 409.
9. Consultar un UUID inexistente para verificar HTTP 404.
10. Crear un Creeper con tiempo de detonación negativo para verificar HTTP 400.
11. Eliminar los mobs utilizados en las pruebas.

## 9. Verificaciones realizadas

Se verificó el funcionamiento del CRUD, las reglas de explosión y la persistencia del historial.

Con una distancia de dos bloques, el daño fue de 8,5 puntos y la salud del Zombie disminuyó de 20 a 11,5.

También se comprobó el endpoint de la interface `Explosivo`, las respuestas HTTP 404 y 409, y la importación de la colección en Insomnia.

El comando Maven `clean test` terminó con cinco pruebas ejecutadas, cero fallas y cero errores.

## 10. Enlace al commit

**Pendiente:** agregar aquí la URL exacta del commit definitivo una vez publicado en GitHub.