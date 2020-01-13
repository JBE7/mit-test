# Prueba práctica

## Como levantar el proyecto
### dependencias

* Java 8+

Para correr los test ejecutar `./gradlew test` ( o `gradlew test` desde windows)

Para lvantar el servidor correr `./gradlew bootRun` ( o `gradlew bootRun` desde windows)

## Descripción del proyecto

Es un inicio de ticketera que actualmente cuenta con dos servicios:

* `GET /ticket` para obtener los tickets creados
* `POST /ticket` para crear un ticket nuevo

## Objetivos

Crear los siguientes servicios:

* `GET /ticket/{id}` para obtener un ticket por id
* `PUT /ticket/{id}` para cambiar el estado de un ticket
* `DELETE /ticket/{id}` para eliminar un ticket con la condición de que el estado sea DONE
* `GET /ticket/notFinished` para obtener todos los tickets no terminados

Puntos extra

* Crear test para cada uno de los casos anteriores
* Crear test que validen casos "no felices" o que generen un error controlado





