# API_Tareas

## Descripción, Tablas y Endpoints
Para almacenar los datos de las tareas y los usuarios se crearan en la base de datos de Mongodb una coleccion (tabla) que almacene los usuarios y las tareas.

### Documento Usuario
Guardará la información relacionada con un usuario que servirá a su vez para identificarse.

- Estructura

| Campo     | Tipo      | Descripción                                    |
|-----------|-----------|------------------------------------------------|
| username  | `String`   | Nick de usuario                                |
| email     | `String`   | Email del usuario                              |
| password  | `String`   | Contraseña del usuario                         |
| roles     | `String`   | Rol del usuario en la aplicación (USER, ADMIN) |
| direccion | `Direccion` | Direcció del usuario                           |

- Endpoint

| Método | Endpoint                    | Descripción                        |
|--------|-----------------------------|------------------------------------|
| POST   | /usuarios/login             | Identifica al usuario              |
| POST   | /usuarios/register          | Registra un nuevo usuario          |
| GET    | /usuarios/{username}        | Obtiene un usuario por su username |
| UPDATE | /usuarios/update            | Actualiza un usuario               |
| DELETE | /usuarios/delete/{username} | Elimina un usuario                 |


### Documneto Tarea
Representa una tarea

| Campo       | Tipo      | Descripción                                |
|-------------|-----------|--------------------------------------------|
| _id         | `String`  | Identificador de la tarea                  |
| nombre      | `String`  | Nombre de la tarea                         |
| descripcion | `String`  | Descripción de la tarea                    |
| usuario     | `Usuario` | Usuario que escribió la tarea              |
| progreso    | `String`  | Progreso de la tarea (TERMINADA/PENDIENTE) |

- Endpoints


| Método | Endpoint                  | Descripción                            |
|--------|---------------------------|----------------------------------------|
| POST   | /tareas/                  | añade una tarea                        |
| GET    | /tareas/                  | Obtiene todas las tareas               |
| GET    | /tareas/{id}              | Obtiene un una tarea po id             |
| GET    | /tareas/{username}        | Obtiene todas las tareas de un usuario |
| UPDATE | /tareas/update            | Actualiza una tarea                    |
| DELETE | /tareas/delete/{id}       | Elimina una tarea                      |
| DELETE | /tareas/delete/{username} | Elimina una tarea                      |

## Lógca de Negocio
1. Gestión de usuarios
   - Los usuarios deben estar registrados y deben iniciar sesión para acceder a las tareas
2. Validacion de Rol
   - Usuario USER: Puede ver sus tareas, marcar como terminada una tarea propia, Eliminar una tarea propia y crear tareas.
   - Usuario ADMIN Puede ver todas las tareas de cualquier usuario, eliminar cualquier tarea de cualquier usuario, crear nuevas tareas a cualquier usuario y marcarcarlas como terminada a cualquiera.


## Excepciones

| Situación                   | Código | Excepción                |
|-----------------------------|--------|--------------------------|
| Usuario no encontrado       | 404    | `UserNotFoundException`  |
| Tarea no encontrada         | 404    | `GastoNotFoundException` |
| Datos inválidos             | 400    | `BadRequestException`    |
| Usuario existente           | 400    | `UserExistException`     |
| Sin autorización            | 401    | `NotAuthorizedException` |

## Restricciones de seguridad
1. Autenticación obligatoria:
   Se requiere que todos los usuarios se autentiquen con un token único para acceder a los recursos.
2. Validación de datos:
   Se aplican reglas estrictas para verificar los datos enviados por los clientes, evitando valores inválidos o maliciosos.
3. Cifrado de contraseñas:
   Las contraseñas se almacenan cifradas.
4. Roles de usuario:
   Los usuarios con rol ADMIN tienen permisos adicionales, como gestionar tipos de gastos.
