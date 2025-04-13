# maven-backend-template

Este arquetipo sirve como plantilla para generar proyectos backend mediante Maven.

---

# Dependencias

---

# Crear proyecto

## Parámetros obligatorios

## Parámetros opcionales

---

# Estructura

---

# TO DO

<!--
Checklists
- [ ] Pending
- [x] Done
-->

- Gestionar exception TypeMismatchException en CommonControllerAdvice (y buscar otras posibles
  excepciones propias de Spring bastante comunes que necesiten ser manejadas). Echar un ojo a
  org.springframework.http.web.bind.ServletRequestBindingException y sus subclases.
- Configurar un githook para solo aceptar mensajes de commit válidos (Conventional Commits, Husky)
- Cambiar los log.info() por log.debug(), log.error(), etc según el nivel de severidad necesario
  para su uso actual.
- Refactorizar los DTO por records de Java (cuidado con anotaciones de Lombok)
- Actualizar versión Java de 17 a 21
- Reformatear todas las plantillas para aplicar el CheckStyle de google (cuidado con las variables
  en los imports). Comprobar los tabulados con 2 espacios, linea en blanco entre la descripción y
  los parámetros en los javadoc, etc.
- Crear plantillas de github actions dentro del archetype y parametrizar versiones de las actions
- Crear un script que genere un banner que lea el nombre del proyecto
- Implementar un ValueObject para el UserID y reemplazar sus referencias en otros sitios (relaciones
  entre entidades, JWT, controladores, validaciones, etc)
- Implementar los tests básicos de usuario en una clase BasicUserTest. Luego el resto de tests
  cuando se añadan nuevas
  funcionalidades tras instanciar el archetype serán clases hijas de BasicUserTest.
- Configurar un Linter de java y adjuntarlo a un nuevo perfil de Maven. Ejecutarlo antes de compilar
  código. (CheckStyle, quizás la de Google sea la mejor)
- Configurar todo el aspecto del Logging (¿quizás automatizar exportación a un docker con
  elasticsearch o algo similar, en vez de a una carpeta /log?)
- Automatizar poder pasar más propiedades en el properties sin tener que modificar el array de
  propiedades obligatorias: "-D{nombreVariable}={valorVariable}"
- Crear script que genere las claves rsa públicas y privadas cuando se genere el proyecto
- Redactar todo este documento, commitear y pushear
- Comprobar que hay documentación escrita solo para las clases y métodos (ambos public).
- Configurar alguna libreria como OpenAPI o similar para automatizar la generación de documentación
  del proyecto.