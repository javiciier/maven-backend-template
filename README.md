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

- Configurar un githook para solo aceptar mensajes de commit válidos (Conventional Commits, Husky)
- Crear plantillas de github actions dentro del archetype y parametrizar versiones de las actions
- Crear un script que genere un banner que lea el nombre del proyecto
- Implementar un ValueObject para el UserID y reemplazar sus referencias en otros sitios (relaciones
  entre entidades, JWT, controladores, validaciones, etc)
- Configurar un Linter de java y adjuntarlo a un nuevo perfil de Maven. Ejecutarlo antes de compilar
  código. (CheckStyle, quizás la de Google sea la mejor)
- Configurar todo el aspecto del Logging (¿quizás automatizar exportación a un docker con
  elasticsearch o algo similar, en vez de a una carpeta /log?)
- Automatizar poder pasar más propiedades en el properties sin tener que modificar el array de
  propiedades obligatorias: "-D{nombreVariable}={valorVariable}"
- Crear script que genere las claves rsa públicas y privadas cuando se genere el proyecto
- Redactar todo este documento, commitear y pushear
- Configurar alguna libreria como OpenAPI o similar para automatizar la generación de documentación
  del proyecto.