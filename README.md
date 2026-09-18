# NRodriguez-taller-git-2026
Taller Git 2026 - Modelado POO Minecraft con Spring Boot

## Diagrama de clases del modelo Minecraft

Diagrama generado a partir del código real ubicado en `src/main/java/py/edu/uc/lp3/minecraft/`.

### Herencia

`Entidad` es la clase abstracta raíz del modelo. De ella derivan `SerVivo` (salud máxima, veneno y regeneración), `Animal` (edad y domesticación) y `Residente` (posibilidad de comerciar).

De `SerVivo` derivan `Jugador` y la clase abstracta `MobHostil`. Los mobs hostiles concretos son `Zombie`, `Esqueleto`, `Creeper`, `Enderman` y `Ghast` (contribución fusionada).

De `Animal` deriva `Cerdo`, y de `Residente` deriva `Aldeano`.

### Composición y asociaciones

- `Jugador` mantiene una relación de **composición** con `Inventario` (capacidad 36).
- `Inventario` agrega **0..*** objetos de tipo `Item`.
- `Entidad` usa `Vector3D` para su posición y `TipoEntidad` para su tipo.
- `Bloque` es una clase independiente que también usa `Vector3D`.
- Asociaciones de uso: `Jugador.atacar(Entidad)`, `Jugador.colocarBloque(Bloque)`, `MobHostil.atacar/buscarObjetivo(Entidad)`, `Enderman.tomarBloque(Bloque)`, `Animal.alimentar(Item)`, `Residente.interactuar(Jugador)`, `Aldeano.comerciar(Jugador, Item)`.

### Comportamiento polimórfico `emitirSonido()`

`emitirSonido()` se declara abstracta en `Entidad`, se implementa genéricamente en `SerVivo` y se **sobrescribe** de forma polimórfica en `Zombie` (ej. "Grrr..."), `Cerdo` ("Oinc oinc") y `Aldeano` ("Hmm... ¿En qué puedo ayudarte?").

### Diagrama Mermaid

```mermaid
classDiagram
    class TipoEntidad {
        <<enumeration>>
        JUGADOR
        ZOMBIE
        ESQUELETO
        CREEPER
        ENDERMAN
        GHAST
        CERDO
        ALDEANO
        OTRO
    }

    class Vector3D {
        -double x
        -double y
        -double z
        +getX() double
        +getY() double
        +getZ() double
        +distanciaA(otro Vector3D) double
    }

    class Entidad {
        <<abstract>>
        -UUID id
        -TipoEntidad tipo
        -String nombre
        -double salud
        -Vector3D posicion
        +getSalud() double
        +getPosicion() Vector3D
        +recibirDano(cantidad double) void
        +curar(cantidad double) void
        +estaViva() boolean
        +moverse(destino Vector3D) void
        +tick()*
        +emitirSonido()*
        #cambiarSalud(nuevaSalud double) void
    }

    class SerVivo {
        <<abstract>>
        -double saludMaxima
        -boolean envenenado
        -double regeneracion
        +getSaludMaxima() double
        +estaEnvenenado() boolean
        +envenenar() void
        +curarEnvenenamiento() void
        +aplicarRegeneracion(cantidadPorTick double) void
        +curar(cantidad double) void
        +tick() void
        +emitirSonido() String
    }

    class Jugador {
        -String nombreJugador
        -Inventario inventario
        -int nivelExperiencia
        -boolean modoCreativo
        +getNombreJugador() String
        +getNivelExperiencia() int
        +esModoCreativo() boolean
        +setModoCreativo(modoCreativo boolean) void
        +aumentarExperiencia(cantidad int) void
        +atacar(objetivo Entidad) void
        +recoger(item Item) boolean
        +colocarBloque(bloque Bloque) void
        +abrirInventario() Inventario
    }

    class MobHostil {
        <<abstract>>
        -double fuerzaAtaque
        -double alcanceVision
        +getFuerzaAtaque() double
        +getAlcanceVision() double
        +buscarObjetivo(entidades List~Entidad~) Entidad
        +atacar(objetivo Entidad) void
    }

    class Zombie {
        -double velocidad
        -boolean quemandose
        +getVelocidad() double
        +estaQuemandose() boolean
        +exponerAlSol(expuesto boolean) void
        +emitirSonido() String
        +tick() void
    }

    class Esqueleto {
        -boolean conArco
        +tieneArco() boolean
        +dispararFlecha(objetivo Entidad) boolean
    }

    class Creeper {
        -double tiempoDetonacion
        -boolean detonando
        -boolean explotado
        +getTiempoDetonacion() double
        +estaDetonando() boolean
        +comenzarDetonacion() void
        +acercarse(objetivo Entidad) void
        +explotar() boolean
        +tick() void
    }

    class Enderman {
        +teletransportarse(destino Vector3D) void
        +tomarBloque(bloque Bloque) Bloque
    }

    class Ghast {
        -int ticksEnfriamientoAtaque
        +getTicksEnfriamientoAtaque() int
        +puedeAtacar() boolean
        +atacar(objetivo Entidad) void
        +tick() void
    }

    class Animal {
        <<abstract>>
        -int edad
        -boolean domesticable
        +getEdad() int
        +esAdulto() boolean
        +esDomesticable() boolean
        +alimentar(alimento Item)*
        +tick() void
    }

    class Cerdo {
        -boolean montable
        +esMontable() boolean
        +emitirSonido() String
        +alimentar(alimento Item) boolean
    }

    class Residente {
        <<abstract>>
        -boolean puedeComerciar
        +puedeComerciar() boolean
        +interactuar(jugador Jugador)*
        +tick() void
    }

    class Aldeano {
        -String profesion
        -int nivelComercio
        +getProfesion() String
        +setProfesion(profesion String) void
        +getNivelComercio() int
        +comerciar(jugador Jugador, item Item) boolean
        +emitirSonido() String
        +interactuar(jugador Jugador) void
    }

    class Inventario {
        -int capacidadMaxima
        -List~Item~ items
        +agregar(item Item) boolean
        +quitar(item Item) boolean
        +estaLleno() boolean
        +getCapacidadMaxima() int
        +getItems() List~Item~
    }

    class Item {
        -UUID id
        -String nombre
        +getId() UUID
        +getNombre() String
    }

    class Bloque {
        -String material
        -Vector3D posicion
        +getMaterial() String
        +getPosicion() Vector3D
    }

    Entidad <|-- SerVivo : extends
    SerVivo <|-- Jugador : extends
    SerVivo <|-- MobHostil : extends
    MobHostil <|-- Zombie : extends
    MobHostil <|-- Esqueleto : extends
    MobHostil <|-- Creeper : extends
    MobHostil <|-- Enderman : extends
    MobHostil <|-- Ghast : extends
    Entidad <|-- Animal : extends
    Animal <|-- Cerdo : extends
    Entidad <|-- Residente : extends
    Residente <|-- Aldeano : extends

    Jugador *-- Inventario : composicion
    Inventario o-- Item : 0..*

    Entidad --> Vector3D : posicion
    Entidad --> TipoEntidad : tipo
    Bloque --> Vector3D : posicion
    Jugador --> Entidad : atacar
    Jugador --> Item : recoger
    Jugador --> Bloque : colocarBloque
    MobHostil --> Entidad : atacar / buscarObjetivo
    Esqueleto --> Entidad : dispararFlecha
    Creeper --> Entidad : acercarse
    Enderman --> Bloque : tomarBloque
    Enderman --> Vector3D : teletransportarse
    Animal --> Item : alimentar
    Residente --> Jugador : interactuar
    Aldeano --> Jugador : comerciar
    Aldeano --> Item : comerciar
```
