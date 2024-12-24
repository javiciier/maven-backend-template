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

- Cambiar los comentarios que separan secciones dentro del códdigo por comentarios /* region */
- Mover toda la parte de autenticación a un nuevo paquete base 'auth'
- Pulir scripts para automatizar tareas de creación del proyecto, mover todo a carpeta /scripts (actualizar .gitignore)
- Crear un script que genere un banner que lea el nombre del proyecto
- Configurar un Linter de java y adjuntarlo a un nuevo perfil de Maven. Ejecutarlo antes de compilar código.
- Configurar todo el aspecto del Logging (¿quizás automatizar exportación a un docker con elasticsearch o algo similar,
  en vez de a una carpeta /log?)
- Automatizar poder pasar más propiedades en el properties sin tener que modificar el array de propiedades
  obligatorias: "-D{nombreVariable}={valorVariable}"
- Crear script que genere las claves rsa públicas y privadas cuando se genere el proyeto
- Redactar todo este documento, commitear y pushear 