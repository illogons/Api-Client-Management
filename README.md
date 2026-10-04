# Client Management API

REST API desarrollada con **Java 21 y Spring Boot** para la gestión de clientes y de los diferentes recursos y servicios asociados a ellos.

El proyecto está basado en una arquitectura por capas y utiliza **Spring Data JPA/Hibernate** para la persistencia de datos, DTOs para la comunicación con la API, Assemblers para transformar objetos y Spring Security para la protección de los endpoints.

## Importante
Proyecto desarrollado durante mi periodo de prácticas profesionales, dentro de un entorno empresarial.

El repositorio presenta un número reducido de commits debido a que el desarrollo original se realizó principalmente dentro de la infraestructura y flujo de trabajo interno de la empresa. Posteriormente, el proyecto fue preparado y organizado en este repositorio con fines de portfolio y demostración técnica.

---

## Índice

* [Características](#-características)
* [Tecnologías](#-tecnologías)
* [Arquitectura](#-arquitectura)
* [Estructura del proyecto](#-estructura-del-proyecto)
* [Recursos gestionados](#-recursos-gestionados)
* [Endpoints](#-endpoints)
* [DTOs](#-dtos)
* [Assemblers](#-assemblers)
* [Persistencia](#-persistencia)
* [Búsquedas y filtros](#-búsquedas-y-filtros)
* [Seguridad](#-seguridad)
* [Validaciones y excepciones](#-validaciones-y-excepciones)
* [Auditoría](#-auditoría)
* [Swagger / OpenAPI](#-swagger--openapi)
* [Instalación](#-instalación)
* [Ejecución](#-ejecución)
* [Base de datos](#-base-de-datos)
* [Próximas mejoras](#-próximas-mejoras)

---

## Características

La API permite gestionar diferentes elementos relacionados con clientes:

* Clientes
* Recursos
* Recursos asociados a clientes
* VPN
* VPN asociadas a clientes
* Conexiones
* Tipos de conexión
* Bases de datos
* Sistemas operativos
* Partners
* Responsables
* Usuarios
* Actualizaciones
* Versiones
* Historial de versiones

Además, el proyecto incorpora:

* Operaciones CRUD.
* DTOs de entrada y salida.
* Assemblers para transformar DTOs y modelos.
* Persistencia mediante JPA/Hibernate.
* Paginación.
* Búsqueda mediante filtros dinámicos.
* Validación de datos.
* Manejo global de excepciones.
* Control de concurrencia mediante versionado.
* Spring Security.
* Procesamiento de JWT.
* BCrypt para el tratamiento de contraseñas.
* Documentación mediante Swagger/OpenAPI.
* Auditoría mediante AOP.
* Configuración para H2 y MySQL.
* Lombok.
* Spring Boot DevTools.

---

## 🛠 Tecnologías

| Tecnología        | Uso                                    |
| ----------------- | -------------------------------------- |
| Java 21           | Lenguaje principal                     |
| Spring Boot 3.4.2 | Framework principal                    |
| Spring Web        | Creación de la API REST                |
| Spring Data JPA   | Acceso a datos                         |
| Hibernate         | ORM                                    |
| Spring Security   | Seguridad                              |
| JWT               | Autenticación mediante tokens          |
| H2                | Base de datos local                    |
| MySQL             | Base de datos para otros entornos      |
| Lombok            | Reducción de código repetitivo         |
| SpringDoc OpenAPI | Documentación Swagger                  |
| AOP               | Auditoría y aspectos                   |
| Maven             | Gestión de dependencias y construcción |

---

# Arquitectura

El proyecto utiliza una arquitectura organizada por capas:

```text
                ┌─────────────────────┐
                │      Controller     │
                └──────────┬──────────┘
                           │
                           ▼
                ┌─────────────────────┐
                │       Service       │
                └──────────┬──────────┘
                           │
                           ▼
                ┌─────────────────────┐
                │     Repository      │
                └──────────┬──────────┘
                           │
                           ▼
                ┌─────────────────────┐
                │   Base de datos     │
                └─────────────────────┘
```

Los **DTOs** y **Assemblers** se utilizan para separar los objetos que recibe/devuelve la API de los modelos utilizados internamente.

### Flujo de una petición

Por ejemplo, al crear un cliente:

```text
HTTP Request
     │
     ▼
ClientController
     │
     ▼
ClientService
     │
     ▼
ClientAssembler
     │
     ▼
ClientModel
     │
     ▼
ClientRepository
     │
     ▼
Database
```

Posteriormente, el resultado realiza el camino inverso para generar el `ClientResponseDto`.

---

# Estructura del proyecto

```text
src/
└── main/
    ├── java/
    │   └── com.gonzalovega.clientmanagement/
    │
    │       ├── assemblers/
    │       ├── aspect/
    │       ├── config/
    │       ├── controllers/
    │       ├── dto/
    │       ├── exceptions/
    │       ├── models/
    │       ├── repository/
    │       ├── security/
    │       ├── services/
    │       └── specifications/
    │
    └── resources/
        ├── application.properties
        ├── application-local.properties
        └── messages.properties
```

### `controllers/`

Contiene los controladores REST encargados de recibir las peticiones HTTP.

### `services/`

Contiene la lógica de negocio de la aplicación.

### `repository/`

Contiene los repositorios de Spring Data JPA utilizados para acceder a la base de datos.

### `models/`

Contiene las entidades JPA que representan las tablas de la base de datos.

### `dto/`

Contiene los objetos utilizados para recibir información desde el cliente y devolver respuestas.

### `assemblers/`

Se encargan de convertir entre DTOs y modelos.

### `specifications/`

Contiene las especificaciones utilizadas para construir búsquedas y filtros dinámicos.

### `security/`

Contiene la lógica relacionada con JWT y seguridad.

### `config/`

Contiene las diferentes configuraciones de Spring Boot, seguridad, Jackson, OpenAPI, JPA, etc.

### `exceptions/`

Contiene las clases relacionadas con el manejo de errores y excepciones.

### `aspect/`

Contiene aspectos utilizados para funcionalidades transversales como la auditoría.

---

# Recursos gestionados

La API contiene diferentes recursos relacionados entre sí.

### Clientes

Representan los clientes principales de la aplicación.

Endpoint base:

```text
/v1/clients
```

Permite realizar operaciones como:

```text
GET     /v1/clients
GET     /v1/clients/{id}
POST    /v1/clients
PUT     /v1/clients/{id}
DELETE  /v1/clients/{id}
POST    /v1/clients/search
```

También existe un endpoint para consultar el historial de versiones de un cliente.

---

### Usuarios

Endpoint:

```text
/v1/Users
```

Operaciones principales:

```text
GET     /v1/Users/all
GET     /v1/Users/active
GET     /v1/Users/{id}
POST    /v1/Users
PUT     /v1/Users/{id}
DELETE  /v1/Users/{id}
POST    /v1/Users/search
```

La eliminación de usuarios se realiza de forma lógica: el registro no se elimina físicamente, sino que se marca como inactivo.

---

### Recursos

```text
/v1/resources
```

Permite gestionar los diferentes recursos asociados al sistema.

---

### Recursos de clientes

```text
/v1/client-resources
```

Permite relacionar recursos con clientes.

---

### VPN

```text
/v1/vpn
```

Gestiona la información relacionada con conexiones VPN.

---

### VPN de clientes

```text
/v1/client-vpn
```

Gestiona la relación entre clientes y configuraciones VPN.

---

### Conexiones

```text
/v1/connections
```

Gestiona las diferentes conexiones existentes.

---

### Tipos de conexión

```text
/v1/type-connections
```

Permite gestionar los tipos de conexión disponibles.

---

### Bases de datos

```text
/v1/databases
```

Gestiona información relacionada con bases de datos.

---

### Sistemas operativos

```text
/v1/operative-systems
```

Gestiona los sistemas operativos asociados a los clientes o recursos.

---

### Partners

```text
/v1/partners
```

Gestiona los partners relacionados con los clientes.

---

### Responsables

```text
/v1/Responsible
```

Gestiona los responsables asociados a los diferentes elementos del sistema.

---

### Actualizaciones

```text
/v1/Update
```

Gestiona las actualizaciones registradas.

---

### Versiones

```text
/v1/versions
```

Gestiona las versiones de los elementos del sistema.

---

# DTOs

La API utiliza **Data Transfer Objects (DTOs)** para evitar exponer directamente las entidades JPA.

Por ejemplo:

```text
ClientRequestDto
```

representa los datos que la API espera recibir cuando se crea o modifica un cliente.

Mientras que:

```text
ClientResponseDto
```

representa los datos que devuelve la API.

La estructura general es:

```text
Request
   │
   ▼
RequestDto
   │
   ▼
Assembler
   │
   ▼
Model
```

Y para la respuesta:

```text
Model
   │
   ▼
Assembler
   │
   ▼
ResponseDto
   │
   ▼
Response
```

---

# Assemblers

Los Assemblers son una parte importante de la arquitectura.

Su función principal es transformar objetos entre diferentes capas.

Por ejemplo:

```java
ClientRequestDto
        ↓
ClientAssembler
        ↓
ClientModel
```

Y:

```java
ClientModel
        ↓
ClientAssembler
        ↓
ClientResponseDto
```

Esto permite mantener separadas las entidades de base de datos de los objetos que utiliza la API.

---

# Persistencia

La aplicación utiliza:

* Spring Data JPA
* Hibernate
* H2 para desarrollo local
* MySQL como alternativa

Los repositorios utilizan las interfaces de Spring Data para realizar operaciones sobre las entidades.

Ejemplo conceptual:

```text
ClientController
       ↓
ClientService
       ↓
ClientRepository
       ↓
ClientModel
       ↓
Database
```

---

# Búsquedas y filtros

La API incorpora búsquedas mediante `POST /search`.

Estas búsquedas utilizan:

```text
Spring Data JPA Specifications
```

permitiendo construir filtros dinámicos sin tener que crear un método de repositorio diferente para cada combinación de filtros.

Además, las búsquedas permiten utilizar:

* Número de página.
* Tamaño de página.
* Diferentes criterios de filtrado.

Las respuestas paginadas utilizan la clase:

```text
PageResponse
```

---

# Seguridad

El proyecto utiliza **Spring Security**.

La configuración principal se encuentra en:

```text
config/security/SecurityConfig.java
```

La aplicación funciona de forma stateless y está preparada para trabajar con tokens JWT.

Los endpoints de documentación están permitidos sin autenticación:

```text
/swagger-ui/**
/v3/api-docs/**
```

El resto de endpoints requieren autenticación.

Las contraseñas utilizan:

```text
BCryptPasswordEncoder
```

para su almacenamiento seguro.

### JWT

El proyecto contiene:

```text
JwtService
JwtAuthenticationFilter
SecurityUtils
```

El filtro comprueba el encabezado:

```http
Authorization: Bearer <token>
```

y obtiene información del usuario a partir del JWT.

> La implementación actual contiene la infraestructura de validación de JWT, pero el proyecto todavía puede ampliarse incorporando un endpoint completo de login y generación de tokens.

---

# Manejo de errores

La API incorpora un manejador global:

```text
GlobalExceptionHandler
```

Su objetivo es centralizar el tratamiento de errores y devolver respuestas HTTP consistentes.

Los errores se representan mediante:

```text
ErrorResponse
```

Esto evita tener que implementar el mismo tratamiento de excepciones en cada controlador.

---

# Control de concurrencia

Algunos recursos utilizan un campo de versión para detectar modificaciones simultáneas.

El funcionamiento básico es:

```text
Cliente A obtiene versión 5
          │
          ▼
Cliente B obtiene versión 5

Cliente A modifica → versión 6

Cliente B intenta modificar usando versión 5
          │
          ▼
      Conflicto
```

De esta forma se evita sobrescribir accidentalmente modificaciones realizadas por otro usuario.

---

# Auditoría

La aplicación incluye un sistema de auditoría basado en **Spring AOP**.

La clase:

```text
AuditLogAspect
```

permite interceptar determinadas operaciones y registrar información sobre las acciones realizadas.

Esto permite añadir funcionalidades transversales sin tener que repetir código en todos los servicios.

---

# Swagger / OpenAPI

La API utiliza **SpringDoc OpenAPI** para generar automáticamente la documentación.

Una vez iniciada la aplicación:

```text
http://localhost:8081/swagger-ui/index.html
```

Desde Swagger se pueden consultar:

* Endpoints.
* Parámetros.
* Request bodies.
* Responses.
* Códigos HTTP.
* Modelos DTO.
* Operaciones disponibles.

---

# 💻 Instalación

## Requisitos

Antes de ejecutar el proyecto necesitas:

* Java 21
* Git
* Maven, opcionalmente, ya que el proyecto incluye Maven Wrapper.

Puedes comprobar Java con:

```bash
java -version
```

---

## Clonar el repositorio

```bash
git clone https://github.com/illogons/client-management-api.git
```

Entrar en el proyecto:

```bash
cd client-management-api
```

---

#  Ejecución

### Windows

```powershell
.\mvnw.cmd spring-boot:run
```

### Linux / macOS

```bash
./mvnw spring-boot:run
```

La aplicación se ejecuta en:

```text
http://localhost:8081
```

---

# Base de datos

Para facilitar la ejecución local, el proyecto utiliza H2.

Configuración:

```text
Database: H2
Mode: File
Location: ./data/client-management
Username: sa
Password: vacío
```

La configuración se encuentra en:

```text
src/main/resources/application-local.properties
```

Hibernate utiliza:

```properties
spring.jpa.hibernate.ddl-auto=update
```

por lo que las tablas se generan/actualizan automáticamente a partir de las entidades JPA.

---

# Documentación adicional

El proyecto incluye algunos recursos gráficos relacionados con el modelo de datos:

```text
docs/
├── DER.png
└── DomainModel.png
```

Estos diagramas ayudan a entender las relaciones existentes entre las diferentes entidades.

---

# Próximas mejoras

Algunas mejoras que se pueden incorporar al proyecto son:

* [ ] Implementar endpoint de login.
* [ ] Implementar generación y firma de JWT.
* [ ] Añadir tests unitarios.
* [ ] Añadir tests de integración.
* [ ] Mejorar la gestión de roles y permisos.
* [ ] Añadir Docker.
* [ ] Añadir `docker-compose` para levantar API + base de datos.
* [ ] Añadir configuración específica para producción.
* [ ] Añadir CI/CD con GitHub Actions.
* [ ] Mejorar la documentación de algunos endpoints.
* [ ] Añadir ejemplos de peticiones y respuestas.
* [ ] Añadir una interfaz frontend para consumir la API.

---

# Autor

**Gonzalo Vega**

Desarrollador de software junior especializado en backend y desarrollo de aplicaciones web.

Tecnologías de interés:

```text
Java
Spring Boot
Spring Data JPA
Hibernate
REST APIs
SQL
Git
GitHub
HTML
CSS
JavaScript
```

---

## Licencia

Este proyecto se publica con fines educativos y de portfolio.
