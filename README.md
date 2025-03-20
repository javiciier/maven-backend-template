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

- Configurar claves GPG
- Dentro de las /application, crear los casos de uso como clases, y que estas llamen a las interfaces de los servicios.
- Pulir scripts para automatizar tareas de creación del proyecto, mover todo a carpeta /scripts (actualizar .gitignore)
- Crear un script que genere un banner que lea el nombre del proyecto
- Implementar los tests básicos de usuario en una clase BasicUserTest. Luego el resto de tests cuando se añadan nuevas
  funcionalidades tras instanciar el archetype serán clases hijas de BasicUserTest.
- Configurar un Linter de java y adjuntarlo a un nuevo perfil de Maven. Ejecutarlo antes de compilar código. (
  CheckStyle, quizás la de Google sea la mejor)
- Configurar todo el aspecto del Logging (¿quizás automatizar exportación a un docker con elasticsearch o algo similar,
  en vez de a una carpeta /log?)
- Automatizar poder pasar más propiedades en el properties sin tener que modificar el array de propiedades
  obligatorias: "-D{nombreVariable}={valorVariable}"
- Crear script que genere las claves rsa públicas y privadas cuando se genere el proyecto
- Redactar todo este documento, commitear y pushear 
- Crear las Run Configuration básicas para IntelliJ (estilo ejecutar tests de user, arrancar aplicación, arrancar
  contenedor docker, etc)
- Poder configurar qué backend se va a usar en función de la property que se base al crear el proyecto: si un
  repositorio JDBC o si un backend contra Google Firebase, Firestore, etc.
- 