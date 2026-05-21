# 📱 Nodo Cívico

Aplicación móvil Android para la gestión de reportes ciudadanos, con roles de usuario y administrador, seguimiento de casos y panel de control estadístico.

---

## 🚀 Descripción

**Nodo Cívico** permite a los ciudadanos registrar reportes sobre su entorno (seguridad, alumbrado, basura, vías, etc.), hacer seguimiento al estado de los mismos y visualizar información comunitaria.

La aplicación incluye un sistema de autenticación básico, manejo de sesiones locales y separación de roles:

- 👤 **Usuario:** crea y consulta sus propios reportes.
- 🛠️ **Administrador:** visualiza todos los reportes y estadísticas globales.

---

## 🎯 Funcionalidades principales

- Registro e inicio de sesión de usuarios.
- Sesiones locales con `SharedPreferences`.
- Creación, edición y eliminación de reportes.
- Clasificación por categoría, prioridad y estado.
- Filtrado de reportes (Mis reportes / Comunidad).
- Panel de usuario con estadísticas personales.
- Panel de administrador con estadísticas globales.
- Sistema de roles (USER / ADMIN).
- Persistencia local con Room Database.
- Navegación entre pantallas con Navigation Component.
- Interfaz moderna con Material Design.

---

## 🗄️ Base de datos (Room)

### Tabla `users`
- id  
- name  
- email  
- password  
- role  

### Tabla `reports`
- id  
- title  
- description  
- category  
- priority  
- status  
- location  
- date  
- userId  

---

## 🔐 Sistema de autenticación

- Login con email y contraseña.  
- Validación local contra Room.  
- Persistencia de sesión con `SharedPreferences`.  
- Control de acceso por rol.  

---

## 🧑‍💼 Roles del sistema

### 👤 USER
- Crear reportes.  
- Ver solo sus propios reportes.  
- Ver estadísticas personales.  

### 🛠️ ADMIN
- Ver todos los reportes.  
- Eliminar reportes.  
- Acceder a estadísticas globales.  
- Visualizar usuarios activos.  

---

## 📊 Dashboards

### Usuario
- Total de reportes creados.  
- Reportes resueltos personales.  

### Administrador
- Total de reportes globales.  
- Total de usuarios.  
- Reportes por estado (Pendiente, En proceso, Resuelto).  

---

## 🧩 Tecnologías usadas

- Kotlin  
- Android Studio  
- Room Database  
- Navigation Component  
- Fragments  
- RecyclerView  
- Material Design Components  
- Coroutines  
- SharedPreferences  

---

## 🧪 Estado del proyecto

✔ Funcional en modo local (offline)  
✔ Sistema de roles implementado  
✔ CRUD de reportes completo  
✔ UI moderna en Material Design  
⚠ Puede mejorarse con API REST y login real  

---

## 📌 Posibles mejoras futuras

- Backend con Spring Boot o Firebase  
- Notificaciones push  
- Geolocalización de reportes  
- Mapa de reportes  
- Chat o comentarios por reporte  
- Seguridad avanzada (JWT / OAuth)  

---

## 👨‍💻 Autor

Desarrollado como proyecto educativo para práctica de:

- Arquitectura Android  
- Persistencia local  
- Navegación entre fragments  
- Gestión de estados y roles  

---

## 📂 Estructura adicional

```text
data/session/
 └── SessionManager.kt
