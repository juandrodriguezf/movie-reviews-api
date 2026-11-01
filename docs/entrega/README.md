# Entrega Semana 11 - Movie Reviews

Aplicación Android en Kotlin + Jetpack Compose que conserva la API REST de
`backend/` (Semana 10) y agrega Firebase Auth + Firestore (Semana 11). El
backend se elige al compilar: `-Pbackend=firestore` o `-Pbackend=express`.

## Cómo ejecutar

### Modo Firestore

1. Guardar el `google-services.json` real en `android/app/google-services.json`.
2. En Firebase Console, habilitar Authentication (Email/Password) y Firestore.
3. Compilar:

   ```powershell
   cd android
   .\gradlew.bat :app:assembleDebug -Pbackend=firestore
   ```

4. Instalar y abrir. La app arranca en Login.
5. Si Firestore está vacío, crear los datos iniciales manualmente:
   - Registrar una cuenta desde la app; se crea `users/{uid}`.
   - Agregar los artículos en la colección `articles` desde Firebase Console.
   - Publicar reseñas desde la app.
   - Para probar perfiles de otros usuarios, crear documentos `users/{id}` y
     reseñas que los referencien, o registrar otra cuenta y publicar desde ella.
   - Para el artículo principal, abrir **Editar perfil** y seleccionar un artículo.

La base de ejemplo de esta entrega ya contiene 8 artículos, 4 usuarios demo y
8 reseñas de ejemplo, además de las que se creen desde la app, por lo que se
puede usar directamente para la demostración.

### Modo Express

1. Levantar PostgreSQL y el backend:

   ```powershell
   cd backend
   npm install
   npm run def
   ```

2. Crear el puente:

   ```powershell
   adb reverse tcp:3000 tcp:3000
   ```

3. Compilar:

   ```powershell
   cd android
   .\gradlew.bat :app:assembleDebug -Pbackend=express
   ```

4. Instalar y abrir. La app arranca en Home sin login; el usuario actual es
   `id = 1`.

## Funcionalidades Semana 11

| Funcionalidad | Estado | Implementación | Evidencia |
|---|---|---|---|
| Registrar usuario guardando su información en Firestore | OK | `RegisterViewModel`, `AuthRepository` y `FirestoreUserDataSource.createUserProfile`. Crea `users/{uid}`. | [10-registro](../img/10-registro.png) |
| Ver mi perfil | OK | `profile/me`; `ProfileViewModel` usa el UID de Auth o `id = 1` en Express. | [12-mi-perfil](../img/12-mi-perfil.png) |
| Modificar mi perfil | OK | `EditProfileScreen` en `profile/edit`: nombre, username, bio, avatar y artículo principal. Actualiza `users/{uid}` y las copias de `user` en `reviews`. | [13-editar-perfil](../img/13-editar-perfil.png), [14-articulo-principal](../img/14-articulo-principal.png) |
| Ver el perfil de cualquier otro usuario | OK | Ruta `profile/{userId}`, abierta desde el autor de una reseña. | [15-perfil-otro](../img/15-perfil-otro.png) |
| Leer su artículo principal | OK | `ProfileViewModel` carga `mainArticleId` y `ProfileScreen` muestra la tarjeta “Artículo principal” en el perfil propio y ajeno. | [12-mi-perfil](../img/12-mi-perfil.png), [15-perfil-otro](../img/15-perfil-otro.png) |
| Ver todos los artículos | OK | `HomeScreen` y `GET /articles` o colección `articles`. | [11-home-firestore](../img/11-home-firestore.png), [18-home-firestore-scroll](../img/18-home-firestore-scroll.png) |
| Ver las reseñas de un artículo | OK | `ArticleDetailScreen` y `GET /articles/:id/reviews` o `whereEqualTo("articleId", id)`. | [16-detalle](../img/16-detalle.png) |
| Ver el detalle de un artículo | OK | `ArticleDetailScreen` con imagen, ficha y puntaje. | [16-detalle](../img/16-detalle.png) |
| Escribir una reseña | OK | `ReviewFormScreen`; crea en Firestore o con `POST /reviews`. | [17-escribir-resena](../img/17-escribir-resena.png) |

Evidencias de autenticación: [09-login](../img/09-login.png) y
[10-registro](../img/10-registro.png).

## Autenticación con Firebase

- Registro con email/contraseña en `AuthRemoteDataSource`.
- El UID de Auth es el ID del documento `users/{uid}`.
- Antes de crear la cuenta se valida que el `username` no exista.
- Si falla la creación del perfil, se cierra la sesión recién creada.
- `SessionGate` muestra Login/Register hasta que `FirebaseAuth` tenga usuario.
- Cerrar sesión está disponible en el perfil propio, solo en modo Firestore.

## Modelo Firestore

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

Se desnormalizan `user` y `article` dentro de cada reseña para mostrarla sin
lecturas adicionales. El mapeo se hace de forma manual desde `DocumentSnapshot`
en `FirestoreMappers.kt`, usando `doc.id` y convirtiendo `Timestamp`.

Reglas recomendadas para desarrollo:

```js
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read: if true;
      allow write: if request.auth != null;
    }

    match /{document=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

La lectura pública de `users` permite validar el `username` antes de crear la
cuenta en Firebase Auth. Las escrituras siguen requiriendo sesión.

## Doble backend

- `BuildConfig.BACKEND` se genera desde `-Pbackend=express|firestore`.
- `BackendConfig` decide qué implementación de `RemoteDataSource` se inyecta.
- `AppModule` usa `Provider` para no inicializar Firestore en modo Express ni
  Retrofit en modo Firestore.
- `CurrentUserProvider` devuelve `"1"` en Express y el UID de Auth en Firestore.
- `SessionGate` se omite en Express.
- `google-services` solo se aplica en modo Firestore.
- Los IDs de la UI son `String`. `NumericStringTypeAdapter` convierte los IDs
  numéricos de Express; `RetrofitReviewRequestDto` los vuelve a convertir a
  `Int` al crear o actualizar una reseña.

## Estructura

```text
screens/
  auth/
  home/
  article/
  profile/
  review/
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
docs/entrega/
```

## Semana 10 - Modo Express

El modo Express conserva la entrega anterior:

| Criterio | Puntos | Evidencia | Implementación |
|---|---:|---|---|
| Home con artículos | 1.0 | [01-home](../img/01-home.png) | `HomeScreen`, `HomeViewModel` y `GET /articles`. |
| Detalle del artículo | 0.5 | [02-detalle](../img/02-detalle.png) | `ArticleDetailScreen` y `GET /articles/:id`. |
| Ver comentarios | 0.5 | [02-detalle](../img/02-detalle.png) | `GET /articles/:id/reviews`. |
| Mi perfil | 0.5 | [05-mi-perfil](../img/05-mi-perfil.png) | `profile/me` y `GET /users/1/reviews`. |
| Perfil de otro usuario | 1.0 | [04-perfil-otro](../img/04-perfil-otro.png) | `profile/{userId}`, `GET /users/:id` y `GET /users/:id/reviews`. |
| CRUD de reseñas | 1.5 | [08-crear](../img/08-crear.png), [06-editar](../img/06-editar.png), [07-eliminar](../img/07-eliminar.png) | `ReviewFormScreen`, `POST /reviews`, `PUT /reviews/:id` y `DELETE /reviews/:id`. |

### Rutas

```text
home
article/{articleId}
profile/me
profile/edit
profile/{userId}
review/form?articleId={articleId}&reviewId={reviewId}
```

`reviewId = ""` indica que se está creando una reseña; un ID con valor indica
que se está editando.

### Endpoints consumidos en Express

| Método | Endpoint | Uso |
|---|---|---|
| GET | `/articles` | Home |
| GET | `/articles/:id` | Detalle |
| GET | `/articles/:id/reviews` | Comentarios |
| GET | `/users/:id` | Perfil |
| GET | `/users/:id/reviews` | Reseñas del perfil |
| GET | `/reviews/:id` | Precargar el formulario de edición |
| POST | `/reviews` | Crear reseña |
| PUT | `/reviews/:id` | Editar reseña |
| DELETE | `/reviews/:id` | Eliminar reseña |

## Evidencias

- Semana 10: capturas en `docs/img/` para Home, detalle, perfiles y CRUD.
- Semana 11: [Login](../img/09-login.png), [registro](../img/10-registro.png),
  [Home Firestore](../img/11-home-firestore.png),
  [Home Firestore (scroll)](../img/18-home-firestore-scroll.png),
  [mi perfil](../img/12-mi-perfil.png), [editar perfil](../img/13-editar-perfil.png),
  [artículo principal](../img/14-articulo-principal.png),
  [perfil de otro usuario](../img/15-perfil-otro.png),
  [detalle](../img/16-detalle.png) y
  [escribir reseña](../img/17-escribir-resena.png).

## Notas

- El mismo `applicationId` se usa en ambos modos; para cambiar de backend hay
  que recompilar e instalar. No se pueden tener las dos variantes instaladas a
  la vez.
- `google-services.json` está ignorado por Git. Solo es necesario en Firestore.
- La app no incluye un seeder; los datos iniciales se cargan manualmente o ya
  están en la base de ejemplo.
- Los usuarios nuevos de Firebase Auth empiezan sin reseñas. La base de ejemplo
  ya contiene 8 artículos, 4 usuarios demo y 8 reseñas de ejemplo, además de
  las que se creen desde la app.
- La edición de perfil solo está disponible en modo Firestore; en Express el
  usuario actual es fijo (`id = 1`).
