# Movie Reviews

Aplicación de reseñas de películas compuesta por una API REST en **Express + Sequelize + PostgreSQL** y un cliente Android en **Kotlin + Jetpack Compose**.

## Estructura

| Ruta | Contenido |
|---|---|
| `backend/` | API REST: usuarios, películas y CRUD de reseñas |
| `android/` | Cliente Android (Compose, Retrofit y Hilt) |
| `postman/` | Colección y environment de Postman |
| `docs/entrega/` | Evidencias y mapeo de la rúbrica |

## Inicio rápido

### Backend

Requisitos: Node.js >= 18 y PostgreSQL en `localhost:5432`.

```bash
cd backend
npm install
cp .env.example .env
# Completar DB_PASSWORD en backend/.env
npm run def
```

En Windows, `Copy-Item .env.example .env` reemplaza a `cp`.

Guía completa: [backend/README.md](backend/README.md).

### Android

1. Abrir la carpeta `android/` en Android Studio.
2. Usar un Gradle JDK 17 o 21 y ejecutar el sync.
3. Crear el puente al backend: `adb reverse tcp:3000 tcp:3000`.
4. Ejecutar la app en un emulador con el backend activo.

Guía completa: [android/README.md](android/README.md).

Evidencias y mapeo de rúbrica: [docs/entrega/README.md](docs/entrega/README.md).

## Estado

- **Backend:** completo (Semana 9).
- **Android:** completo (Semana 10): home, detalle, comentarios, perfiles y CRUD de reseñas.
