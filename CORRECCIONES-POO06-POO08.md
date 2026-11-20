

\# Seguimiento de correcciones POO-06 y POO-08



\## Referencias

\- Corrección POO-06 - Nicolás Rodríguez.pdf

\- Corrección POO-08 - Nicolás Rodríguez.pdf



\## Correcciones realizadas



\- \[x] Reorganizar las clases del dominio en `py.edu.uc.lp3.domain`.

&#x20; - Commit: `62c8b25`

\- \[x] Delegar el CRUD de `MobHostilController` a `MobHostilService`.

&#x20; - Commit: `f4e0777`

\- \[x] Reemplazar el acceso directo al repositorio en `ExplosivoController`.

&#x20; - Commit: `f4e0777`

\- \[x] Verificar paquetes de `IndexController` y `PolimorfismoController`.

\- \[x] Ejecutar pruebas Maven: 7/7 aprobadas.

\- \[x] Verificar que los controllers no referencien repositorios directamente.



\## Código revisado, sin modificaciones adicionales



\- `MobHostilService.java`

\- `ExplosionService.java`

\- `Creeper.java`

\- `Entidad.java`



\## Observaciones pendientes



\- \[ ] Resolver las validaciones y excepciones del dominio según POO-08.

\- \[ ] Revisar la ubicación de las reglas de explosión.

\- \[ ] Implementar una segunda clase que comparta una interfaz.

\- \[ ] Incorporar sobrecarga de constructores en otra clase.

\- \[ ] Verificar que la sobrecarga sea observable mediante HTTP.

\- \[ ] Verificar respuesta HTTP 400 para parámetros inválidos.

\- \[ ] Actualizar diagramas con Vaca y AldeanoArmero.

\- \[ ] Documentar la licencia Apache 2.0 en README.

\- \[ ] Verificar la documentación y las pruebas finales.



\## Estado de Git



Rama: `fix/correcciones-poo06-poo08`



Último commit conocido: `f4e0777`



No hacer push hasta completar las correcciones y verificaciones.



\## Próximo trabajo



Revisar las validaciones del dominio y definir una refactorización conjunta, evitando modificar nuevamente código ya corregido.



