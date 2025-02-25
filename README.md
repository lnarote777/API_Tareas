# API_Tareas

## Descripción, Tablas y Endpoints
Para almacenar los datos de las tareas y los usuarios se crearan en la base de datos de Mongodb una coleccion (tabla) que almacene los usuarios y las tareas.

### Documento Usuario
Guardará la información relacionada con un usuario que servirá a su vez para identificarse.

- Estructura

| Campo     | Tipo      | Descripción                                  |
|-----------|-----------|----------------------------------------------|
| username  | `String`   | Nick de usuario                              |
| _id       | `String`   | Email del usuario                            |
| password  | `String`   | Contraseña del usuario                       |
| roles     | `String`   | Rol del usuario en la aplicación (USER, ADMIN) |
| direccion | `Direccion` | Direcció del usuario                         |

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

| Campo       | Tipo      | Descripción                                 |
|-------------|-----------|---------------------------------------------|
| _id         | `Int`     | Identificador de la tarea                   |
| nombre      | `String`  | Nombre de la tarea                          |
| descripcion | `String`  | Descripción de la tarea                     |
| usuario     | `Usuario` | Usuario que escribió la tarea               |
| progreso    | `String`  | Progreso de la tarea (COMPLETADA/PENDIENTE) |

- Endpoints


| Método | Endpoint                      | Descripción                            |
|--------|-------------------------------|----------------------------------------|
| POST   | /tareas/crear                 | añade una tarea                        |
| GET    | /tareas/todas                 | Obtiene todas las tareas               |
| GET    | /tareas/mis-tareas/{username} | Obtiene todas las tareas de un usuario |
| UPDATE | /tareas/update                | Actualiza una tarea                    |
| DELETE | /tareas/delete/{id}           | Elimina una tarea                      |

## Lógca de Negocio
1. Gestión de usuarios
   - Los usuarios deben estar registrados y deben iniciar sesión para acceder a las tareas
2. Validacion de Rol
   - Usuario USER: Puede ver sus tareas, marcar como terminada una tarea propia, Eliminar una tarea propia y crear tareas.
   - Usuario ADMIN Puede ver todas las tareas de cualquier usuario, eliminar cualquier tarea de cualquier usuario, crear nuevas tareas a cualquier usuario y marcarcarlas como terminada a cualquiera.


## Excepciones

| Situación            | Código | Excepción            |
|----------------------|--------|----------------------|
| Datos no encontrados | 404    | `NotFoundException`  |
| Datos inválidos      | 400    | `BadRequestException` |
| Usuario existente    | 400    | `UserExistException` |
| Sin autorización     | 401    | `NotAuthorizedException` |

## Restricciones de seguridad
1. Autenticación obligatoria:
   Se requiere que todos los usuarios se autentiquen con un token único para acceder a los recursos.
2. Validación de datos:
   Se aplican reglas estrictas para verificar los datos enviados por los clientes, evitando valores inválidos o maliciosos.
3. Cifrado de contraseñas:
   Las contraseñas se almacenan cifradas.

# Pruebas Gestion de Usuario

- Login exitoso: Cuando el login sea exitoso pasará a una pantalla completamente en blanco. Para
este caso usare el usuario prueba1 que ya estaba registrado en la base de datos de Mongo.

![img.png](src%2Fmain%2Fresources%2Fcapturas%2FpruebasUsuarios%2Fimg.png)

![img_2.png](src%2Fmain%2Fresources%2Fcapturas%2FpruebasUsuarios%2Fimg_2.png)


- Casos en los que es erróneo el login:
    
  - Contraseña incorrecta:
  
![img_11.png](src%2Fmain%2Fresources%2Fcapturas%2FpruebasUsuarios%2Fimg_11.png)

  - Usuario no encontrado:
  
![img_12.png](src%2Fmain%2Fresources%2Fcapturas%2FpruebasUsuarios%2Fimg_12.png)

    > 📝 **Nota:** No se comprueba si los campos estan vacíos porque la interfaz está diseñada para que no se pueda pulsar el botón de iniciar sesión hasta que se rellenen los 2 campos.
  
- Campos vacíos: Botón desabilitado.
  
![img_13.png](src%2Fmain%2Fresources%2Fcapturas%2FpruebasUsuarios%2Fimg_13.png)

- Registro exitoso: Si el registro ha sido exitos pasará a verse la pantalla de login para que el usuario inicie sesión.

![img_1.png](src%2Fmain%2Fresources%2Fcapturas%2FpruebasUsuarios%2Fimg_1.png)

![img_3.png](src%2Fmain%2Fresources%2Fcapturas%2FpruebasUsuarios%2Fimg_3.png)

![img_4.png](src%2Fmain%2Fresources%2Fcapturas%2FpruebasUsuarios%2Fimg_4.png)

- Casos en los que el registro da error:
  - Campo vacío: Se deja cualquier campo vacío. Se muestra el mensaje de error.
  
  ![img_5.png](src%2Fmain%2Fresources%2Fcapturas%2FpruebasUsuarios%2Fimg_5.png)


  - Email mal escrito: Se introduce un formato de email inválido y se muestra un mensaje de error.
  
 ![img_6.png](src%2Fmain%2Fresources%2Fcapturas%2FpruebasUsuarios%2Fimg_6.png)

  - Usuario existente: El email introducido ya existe en la base de datos por lo que el usuario ya existe.
  
  ![img_7.png](src%2Fmain%2Fresources%2Fcapturas%2FpruebasUsuarios%2Fimg_7.png)

  - Contraseñas no coinciden: La contraseña y la confirmación de ésta no coinciden.
  
  ![img_8.png](src%2Fmain%2Fresources%2Fcapturas%2FpruebasUsuarios%2Fimg_8.png)

  - Provincia no existente:
  
![img_9.png](src%2Fmain%2Fresources%2Fcapturas%2FpruebasUsuarios%2Fimg_9.png)

  - Municipio no existente en la provincia seleccionada:
  
![img_10.png](src%2Fmain%2Fresources%2Fcapturas%2FpruebasUsuarios%2Fimg_10.png)



# Pruebas Gestión Tareas

- Creación de tareas:

  - Caso exitoso: Para crear una tarea primero se ha hecho login con una cuenta existente y se ha pasado el token recibido para la autenticación. Hay dos maneras de que se de exitosa la creación de la tarea:
  
    - El usuario es ADMIN por lo que puede añadirle una tarea a cualquier usuario solo poniendo su nombre de usuario en el campo correspondiente: (Usuario usado: admin1 1234)
  
![img.png](src%2Fmain%2Fresources%2Fcapturas%2FpruebasTareas%2Fimg.png)


    - El usuario es USER y se está añadiendo la tarea así mismo: (Usuario usado: prueba1 1234)
    
































