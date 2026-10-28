# Movie Reviews

Aplicación de reseñas de películas compuesta por una API REST en **Express + Sequelize + PostgreSQL** y un cliente Android en **Kotlin + Jetpack Compose**. El cliente conserva Retrofit y agrega Firestore + Firebase Auth, seleccionables al compilar.

## Estructura

| Ruta | Contenido |
|---|---|
| `backend/` | API REST: usuarios, películas y CRUD de reseñas |
| `android/` | Cliente Android (Compose, Hilt, Retrofit y Firestore) |
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
3. Elegir el backend:
   - Firestore: guardar `google-services.json` y compilar con `-Pbackend=firestore`.
   - Express: levantar el backend, ejecutar `adb reverse tcp:3000 tcp:3000` y compilar con `-Pbackend=express`.
4. Ejecutar la app en un emulador o dispositivo.

Guía completa: [android/README.md](android/README.md).

Evidencias y mapeo de rúbrica: [docs/entrega/README.md](docs/entrega/README.md).

## Estado

- **Backend:** completo (Semana 9).
- **Android Semana 10:** home, detalle, comentarios, perfiles y CRUD con Express/Retrofit.
- **Android Semana 11:** autenticación, Firestore, selector de backend, edición de perfil y artículo principal implementados.

## Ramas

- `feat/dual-backend`: entrega actual con Retrofit y Firestore.
- `app-firebase`: variante solo Firestore.
- `feat/android-base-express`: variante solo Express/Retrofit.
