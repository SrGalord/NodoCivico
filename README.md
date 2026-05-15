# Nodo Cívico

Aplicación móvil Android orientada a la gestión de reportes ciudadanos, seguimiento comunitario y acciones coordinadas de microgestión urbana.

## Descripción

Nodo Cívico permite a los usuarios registrar situaciones de su entorno, consultar reportes comunitarios y realizar seguimiento a cada caso mediante una interfaz moderna y organizada.

La aplicación está diseñada bajo una arquitectura limpia utilizando componentes modernos de Android y un enfoque offline-first.

---

# Objetivos del proyecto

- Registrar reportes ciudadanos.
- Consultar reportes comunitarios.
- Dar seguimiento a casos reportados.
- Gestionar recordatorios y alertas.
- Permitir funcionamiento offline.
- Sincronizar información con una API REST.

---

# Tecnologías utilizadas

- Kotlin
- Android Studio
- MVVM Architecture
- Navigation Component
- Fragments
- RecyclerView
- Room Database
- Retrofit
- Material Design

---

# Arquitectura

El proyecto está organizado utilizando arquitectura MVVM:

## Estructura principal

```text
data/
 ├── local/
 ├── remote/
 ├── repository/

ui/
 ├── fragments/
 ├── adapters/

viewmodel/

utils/
