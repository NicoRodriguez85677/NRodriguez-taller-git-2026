# Bitácora de uso de IA

## Asistente utilizado

- Marca: OpenAI ChatGPT
- Modelo: GPT-5.6 Sol

## Resumen de uso

Se utilizó ChatGPT como apoyo durante el desarrollo y revisión de la actividad.

Los principales usos fueron:

- Revisión de la estructura existente del proyecto Spring Boot.
- Revisión de la jerarquía de clases del dominio Minecraft.
- Explicación de los conceptos de sobrecarga y sobreescritura.
- Propuesta de un constructor sobrecargado para la clase `Creeper`.
- Propuesta de una sobrecarga del método `acercarse`.
- Reorganización de los controladores REST según el template de paquetes.
- Verificación del funcionamiento del servicio con `./mvnw spring-boot:run`.
- Apoyo para actualizar el README y documentar los cambios realizados.

## Resumen de prompts

Entre las consultas realizadas se incluyeron:

- Cómo adaptar el proyecto existente a la nueva consigna.
- Cómo implementar sobrecarga de constructores y métodos.
- Cómo identificar la sobreescritura ya presente en las clases.
- Cómo reorganizar los controllers dentro de `rest/controller`.
- Cómo ejecutar y probar el proyecto con Maven y Spring Boot.
- Cómo documentar los cambios realizados en el README.
---

## POO-08 — CRUD REST, H2 e Insomnia

**Fecha:** 08/11/2026

**Asistente de IA:** ChatGPT (OpenAI).

**Modelo de lenguaje utilizado:** GPT-6.

**Modalidad:** asistencia durante el desarrollo individual mediante explicaciones, generación de código, revisión de errores y documentación.

### Resumen de prompts

- Cómo adaptar el modelado Minecraft existente para persistencia con Spring Data JPA y H2.
- Cómo configurar las dependencias Maven y las propiedades de H2.
- Cómo implementar el mapeo de herencia entre `MobHostil`, `Creeper` y `Zombie`.
- Cómo crear un repositorio JPA y un CRUD REST completo.
- Cómo desarrollar excepciones propias del dominio con respuestas HTTP 404 y 409.
- Cómo utilizar la interface `Explosivo` en el dominio y exponerla mediante HTTP.
- Cómo implementar dos reglas de negocio y guardar una simulación de explosiones en H2.
- Cómo preparar e importar una colección de peticiones en Insomnia.
- Cómo revisar los errores de las pruebas Maven y solucionar el conflicto de acceso a la base H2.
- Cómo actualizar el README y preparar la entrega final.

### Resultados verificados

- Spring Boot inicia correctamente en el puerto 8080.
- Hibernate crea las tablas `mob_hostil` y `simulacion_explosion`.
- El CRUD permite crear, consultar, actualizar y eliminar mobs.
- La simulación calcula 8,5 puntos de daño para una distancia de dos bloques.
- El Zombie pasa de 20 a 11,5 puntos de salud.
- El historial de la simulación permanece guardado en H2.
- Un UUID inexistente devuelve HTTP 404.
- Una explosión ilegal devuelve HTTP 409.
- El endpoint de la interface `Explosivo` devuelve el estado esperado.
- La colección se importó en Insomnia y se comprobó una petición POST con HTTP 201.
- Maven finalizó con `BUILD SUCCESS`: cinco pruebas ejecutadas, ninguna falla y ningún error.

### Participación del estudiante

El estudiante realizó las modificaciones en su repositorio, ejecutó los comandos, verificó los resultados en PowerShell e Insomnia y revisó el funcionamiento del servicio.

El asistente proporcionó orientación, ejemplos de implementación y apoyo para detectar y solucionar errores.

El desarrollo se realizó sobre el modelado Minecraft existente, conservando los ejercicios anteriores.