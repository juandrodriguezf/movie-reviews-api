# Movie Reviews - Android

Cliente Android en Kotlin + Jetpack Compose con dos backends intercambiables:

- **Firestore + Firebase Auth** (Semana 11).
- **API REST Express + Retrofit** (Semana 10).

El backend se elige al compilar con la propiedad `backend`. El valor por
defecto es `firestore`.

## Requisitos

- Android Studio con SDK 36 o superior.
- Gradle JDK 17 o 21.
- Para el modo Firestore: `android/app/google-services.json`.
- Para el modo Express: Node.js, PostgreSQL y el backend de `backend/` activo.

## Modos de backend

### Firestore

1. Descargar el `google-services.json` real desde Firebase Console y guardarlo
   en `android/app/google-services.json`. La plantilla
   `google-services.example.json` no sirve para ejecutar la app.
2. En Firebase Console, habilitar **Authentication > Email/Password** y
   **Firestore Database**.
3. Compilar:

   ```powershell
   cd android
   .\gradlew.bat :app:assembleDebug -Pbackend=firestore
   ```

4. Instalar y abrir. La app arranca en Login.
5. Si las colecciones estan vacias, el boton **Cargar datos de ejemplo** de Home
   crea 8 articulos, 4 usuarios demo y 8 resenas. Solo aparece en builds debug.

En este modo el usuario actual es el UID de Firebase Auth y su perfil se crea
en `users/{uid}` durante el registro.

### Express

1. Levantar PostgreSQL y el backend:

   ```powershell
   cd backend
   npm install
   npm run def
   ```

2. Crear el puente entre el equipo y el dispositivo/emulador:

   ```powershell
   adb reverse tcp:3000 tcp:3000
   ```

3. Compilar:

   ```powershell
   cd android
   .\gradlew.bat :app:assembleDebug -Pbackend=express
   ```

4. Instalar y abrir. La app arranca directamente en Home, sin login.
   El usuario actual es `id = 1` (`utils/Constants.kt`).

Si se reinicia `adb`, hay que repetir `adb reverse`.

## Variables y seleccion de backend

- `BuildConfig.BACKEND` se genera desde `-Pbackend=express|firestore`.
- `utils/BackendConfig.kt` expone `isExpress` e `isFirestore`.
- `injection/AppModule.kt` elige la implementacion de datos con `Provider`,
  de modo que el backend no usado no se inicializa.
- `utils/CurrentUserProvider.kt` devuelve:
  - `"1"` en Express.
  - El UID de Firebase Auth en Firestore.
- `SessionGate` solo muestra Login/Register en Firestore.
- El cierre de sesion y el seed de datos solo estan disponibles en Firestore.

## Arquitectura

```text
screens/
  auth/          Login, Register, SessionGate
  home/          Home
  article/       Detalle y resenas
  profile/       Mi perfil y perfiles ajenos
  review/        Crear y editar resena
data/datasource/
  implementation/Firestore*.kt
  implementation/Retrofit*.kt
  services/MovieReviewsService.kt
data/dto/
data/model/
data/repository/
injection/
  AppModule.kt
  FirebaseHiltModule.kt
navigation/
utils/
  BackendConfig.kt
  CurrentUserProvider.kt
  FirestoreSeeder.kt
  Result.kt
```

- **UI:** Compose + Navigation + ViewModel + `UiState`.
- **DI:** Hilt con KSP.
- **Firestore:** mapeo manual de `DocumentSnapshot` a DTOs, usando `doc.id`.
- **Retrofit:** Gson convierte los IDs numericos de Express a `String`
  mediante `NumericStringTypeAdapter`; las peticiones de resena usan
  `RetrofitReviewRequestDto` con IDs `Int`.
- **Modelos:** los DTOs se mapean a `*Info` antes de llegar a la UI.
- **Errores:** `Result` y `apiCall` en `utils/Result.kt`.

## Colecciones Firestore

```text
users/{uid}
  id, username, email, name, avatarUrl, bio, mainArticleId, createdAt, updatedAt

articles/{articleId}
  id, title, director, genre, releaseYear, synopsis, duration, score, imageUrl

reviews/{reviewId}
  id, rating, comment, userId, articleId, createdAt, updatedAt
  user: { id, name, username, avatarUrl }
  article: { id, title, imageUrl }
```

`user` y `article` dentro de `reviews` son copias desnormalizadas para evitar
lecturas adicionales al mostrar las resenas.

Reglas recomendadas para desarrollo:

```js
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

## Comandos utiles

```powershell
# Firestore (default)
.\gradlew.bat :app:assembleDebug -Pbackend=firestore

# Express
.\gradlew.bat :app:assembleDebug -Pbackend=express

# Instalar el APK generado
adb install -r app\build\outputs\apk\debug\app-debug.apk

# Ver errores fatales
adb logcat -s AndroidRuntime:E
```

## Notas

- El APK usa el mismo `applicationId` en ambos modos. Para cambiar de backend
  hay que recompilar e instalar; no se pueden tener las dos variantes
  instaladas al mismo tiempo.
- `google-services.json` esta ignorado por Git. Solo es obligatorio en el modo
  Firestore.
- Los usuarios creados con Firebase Auth empiezan sin resenas. Las resenas del
  seed pertenecen a los usuarios `seed-*`.
- La edicion de perfil esta en `profile/edit`: nombre, username, bio, avatar y
  articulo principal. Solo aparece en modo Firestore.
- El articulo principal (`mainArticleId`) se muestra como tarjeta en el perfil
  propio y en el de otros usuarios.
