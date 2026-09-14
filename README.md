# SpringEduManager

Proyecto desarrollado para el Módulo 6: Desarrollo de aplicaciones JEE con Spring Framework.

Sistema de gestión académica para un bootcamp de programación, con tres roles de usuario: administrador, docente y estudiante. Permite gestionar cursos, prácticas, evaluaciones y notas, con autenticación mediante RUT y permisos diferenciados según el rol.

## Tecnologías utilizadas

- Java 21
- Spring Boot 3.3.4
- Spring MVC
- Spring Data JPA
- Spring Security
- Thymeleaf
- H2 Database
- Maven
- JUnit 5 y Mockito
- JaCoCo (cobertura de tests)

## Requisitos previos

- JDK 21 instalado
- Maven (opcional si usas el IDE, obligatorio si usas la línea de comandos)
- Eclipse, Spring Tool Suite o Visual Studio Code
- Git

## Clonar el proyecto

```
git clone https://github.com/Jennifer-academico/SpringEduManager.git
cd SpringEduManager
```

## Importar en el IDE

### Eclipse o Spring Tool Suite

1. File > Import > Maven > Existing Maven Projects
2. Selecciona la carpeta SpringEduManager
3. Finish
4. Clic derecho sobre el proyecto > Maven > Update Project

### Visual Studio Code

1. File > Open Folder
2. Selecciona la carpeta completa del proyecto (no un archivo suelto)
3. Instala las extensiones de Java recomendadas si el editor las solicita

## Ejecutar la aplicación

### Desde el IDE

Ejecuta la clase principal como aplicación Java:

```
src/main/java/cl/miclase/springedumanager/SpringEduManagerApplication.java
```

Clic derecho sobre el archivo > Run As > Java Application

### Desde la línea de comandos

```
mvn spring-boot:run
```

La aplicación queda disponible en:

```
http://localhost:8081/springedumanager/
```

El puerto se puede cambiar en el archivo src/main/resources/application.yml, en la propiedad server.port.

## Base de datos

El proyecto usa H2 como base de datos embebida, guardada en un archivo local dentro de la carpeta data del proyecto. No requiere instalar ningún motor de base de datos externo.

Al arrancar la aplicación por primera vez, se cargan automáticamente datos de prueba (usuarios, cursos, prácticas, evaluaciones y notas) mediante la clase DemoDataInitializer. Esta carga solo ocurre si la base de datos está vacía.

Para revisar la base de datos directamente, se puede acceder a la consola de H2 en:

```
http://localhost:8081/springedumanager/h2-console
```

Datos de conexión:

- JDBC URL: jdbc:h2:file:./data/springedumanager
- Usuario: sa
- Contraseña: (en blanco)

Si necesitas reiniciar los datos desde cero, basta con detener la aplicación y borrar el archivo springedumanager.mv.db dentro de la carpeta data, y volver a ejecutar la aplicación.

## Usuarios de prueba

El sistema distingue tres roles: administrador, docente y estudiante. Algunos usuarios ya vienen con la cuenta activa, y otros deben activarla primero ingresando su RUT en la pantalla de activación.

Administrador (cuenta activa):

- RUT: 00000000-0
- Contraseña: admin123

Docente con cuenta activa:

- RUT: 11111111-1
- Contraseña: docente123

Docente sin activar (debe activar su cuenta primero):

- RUT: 22222222-2

Estudiantes con cuenta activa:

- RUT: 33333333-3, contraseña estudiante123
- RUT: 44444444-4, contraseña estudiante123

Estudiantes sin activar (deben activar su cuenta primero):

- RUT: 55555555-5
- RUT: 66666666-6
- RUT: 77777777-7

## Flujo de acceso al sistema

1. El administrador precarga personas en el sistema (nombre, email, RUT y rol), sin asignarles contraseña todavía.
2. La persona precargada ingresa a la pantalla de activación de cuenta, escribe su RUT y define una contraseña.
3. Una vez activada la cuenta, puede iniciar sesión con su RUT y la contraseña que definió.
4. Según su rol, accede a distintas funcionalidades:
   - Administrador: gestiona personas, crea cursos y asigna docentes.
   - Docente: crea prácticas y evaluaciones para los cursos que dicta, y registra las notas de sus estudiantes.
   - Estudiante: consulta los cursos en los que está inscrito, sus prácticas y sus propias notas.

## Rutas principales

Rutas web:

- / : página de inicio pública
- /login : inicio de sesión
- /activar-cuenta : activación de cuenta
- /inicio : menú de navegación tras iniciar sesión
- /cursos : gestión y consulta de cursos
- /practicas : gestión y consulta de prácticas
- /evaluaciones : planilla de evaluaciones y notas
- /personas : gestión de personas (solo administrador)

API REST (respuestas en JSON):

- GET /api/v1/cursos
- GET /api/v1/cursos/{id}
- POST /api/v1/cursos (solo administrador)
- DELETE /api/v1/cursos/{id} (solo administrador)
- GET /api/v1/personas (solo administrador)
- POST /api/v1/personas (solo administrador)
- DELETE /api/v1/personas/{id} (solo administrador)
- GET /api/v1/practicas
- POST /api/v1/practicas (administrador o docente)
- GET /api/v1/evaluaciones
- POST /api/v1/evaluaciones (administrador o docente)

## Ejecutar los tests

```
mvn test
```

Para generar el reporte de cobertura con JaCoCo:

```
mvn clean test jacoco:report
```

El reporte queda disponible en:

```
target/site/jacoco/index.html
```

El proyecto cuenta con 87 pruebas entre unitarias e integración, cubriendo aproximadamente el 90% del código: entidades, repositorios, servicios, controladores web, controladores REST y configuración de seguridad.

## Generar documentación JavaDoc

```
mvn javadoc:javadoc
```

El resultado queda disponible en:

```
target/site/apidocs/index.html
```

## Generar el archivo WAR

```
mvn clean package
```

El archivo queda en:

```
target/springedumanager.war
```

El proyecto usa Spring Boot 3 y Jakarta EE, por lo que si se despliega el WAR en un servidor externo, se requiere Tomcat 10.1 o superior. Para desarrollo se recomienda usar el servidor Tomcat embebido, ejecutando la aplicación directamente desde el IDE o con mvn spring-boot:run.

## Estructura del proyecto

```
src/main/java/cl/miclase/springedumanager/
  api/            controladores REST
  config/         inicialización de datos de prueba
  domain/         entidades JPA
  dto/            objetos de transferencia de datos y formularios
  repository/     repositorios Spring Data JPA
  security/       configuración de Spring Security
  service/        lógica de negocio
  web/            controladores MVC

src/main/resources/
  templates/      vistas Thymeleaf
  application.yml configuración de la aplicación

src/test/java/    pruebas unitarias e integración
```

## Notas adicionales

- Las notas se registran a través de una planilla por curso: las filas son los estudiantes y las columnas son las evaluaciones definidas para ese curso. Si un estudiante queda sin nota en una evaluación, se le asigna automáticamente la nota mínima (1.0).
- Un curso tiene un docente asignado (opcional) y una lista de estudiantes inscritos. Tanto docentes como estudiantes solo ven la información asociada a los cursos en los que participan; el administrador ve todo el sistema sin restricciones.
