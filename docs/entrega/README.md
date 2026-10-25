# Entrega Semana 10 - Movie Reviews

Aplicación Android en Kotlin + Jetpack Compose que consume la API REST de
`backend/` para listar películas, consultar su detalle y administrar reseñas.
El usuario autenticado está fijo en `id = 1` (`utils/Constants.kt`); no se usa
Firebase Auth.

## Cómo ejecutar

1. Levantar PostgreSQL y el backend:

   ```powershell
   cd backend
   npm install
   npm run def
   ```

2. Crear el puente entre el equipo y el emulador:

   ```powershell
   adb reverse tcp:3000 tcp:3000
   ```

3. Abrir `android/` en Android Studio, ejecutar el sync y correr la app.

   La URL base es `http://127.0.0.1:3000/`. Si se reinicia `adb`, hay que
   repetir el comando de `adb reverse`.

4. Compilar por consola:

   ```powershell
   cd android
   .\gradlew.bat assembleDebug
   ```

   APK: `android/app/build/outputs/apk/debug/app-debug.apk`.

## Mapeo de la rúbrica

| Criterio | Puntos | Evidencia | Implementación |
|---|---:|---|---|
| Home con artículos | 1.0 | [01-home](../img/01-home.png) | `HomeScreen`, `HomeViewModel` y `GET /articles`. Grilla de dos columnas con portada, puntaje, título, director y año. |
| Detalle del artículo | 0.5 | [02-detalle](../img/02-detalle.png) | `ArticleDetailScreen` y `ArticleDetailViewModel` con `GET /articles/:id`: imagen, título, director, género, duración, sinopsis y puntaje. |
| Ver comentarios | 0.5 | [02-detalle](../img/02-detalle.png) | Reseñas del detalle con `GET /articles/:id/reviews`: autor, avatar, calificación en estrellas y comentario. |
| Mi perfil | 0.5 | [05-mi-perfil](../img/05-mi-perfil.png) | `ProfileScreen` en la ruta `profile/me`: usuario actual, correo y solo sus reseñas (`GET /users/1/reviews`). |
| Perfil de otro usuario | 1.0 | [04-perfil-otro](../img/04-perfil-otro.png) | Ruta `profile/{userId}`. Se abre al tocar el autor de una reseña; muestra sus datos y sus reseñas (`GET /users/:id` y `GET /users/:id/reviews`). |
| CRUD de reseñas | 1.5 | [08-crear](../img/08-crear.png), [06-editar](../img/06-editar.png), [07-eliminar](../img/07-eliminar.png) | Formulario compartido para crear y editar (`ReviewFormScreen`, `ReviewFormViewModel`), usando `POST /reviews` y `PUT /reviews/:id`. Eliminación con confirmación desde detalle y perfil con `DELETE /reviews/:id`. Solo se permite editar o eliminar la reseña propia. |

## Flujo de la app

1. **Home:** lista todas las películas. El botón `Perfil` abre el perfil propio.
2. **Detalle:** muestra la ficha y las reseñas. Si el usuario actual no ha
   reseñado la película, aparece `Escribir reseña`. Si ya la reseñó, su tarjeta
   muestra `Editar` y `Eliminar`.
3. **Formulario:** selecciona de 1 a 5 estrellas, escribe un comentario de
   máximo 500 caracteres y guarda. Al volver, el detalle o el perfil se
   actualiza automáticamente.
4. **Perfil propio:** muestra solo las reseñas del usuario actual y permite
   editarlas o eliminarlas.
5. **Perfil ajeno:** se llega tocando el autor de una reseña. Muestra sus datos
   y sus reseñas, sin acciones de edición.

## Rutas

```text
home
article/{articleId}
profile/me
profile/{userId}
review/form?articleId={articleId}&reviewId={reviewId}
```

`reviewId = -1` significa que el formulario está creando una reseña; un valor
mayor que cero indica que está editando.

## Arquitectura

```text
screens/
data/dto/
data/model/
data/datasource/
data/datasource/services/
data/datasource/implementation/
data/repository/
injection/AppModule.kt
navigation/
utils/
```

- **UI:** Jetpack Compose + Navigation Compose + ViewModel + `UiState`.
- **DI:** Hilt con KSP.
- **Red:** Retrofit + Gson + OkHttp Logging.
- **Imágenes:** Coil.
- **Errores:** `Result` y `apiCall` en `utils/Result.kt`, sin exponer DTOs a la UI.
- **Modelos:** los DTOs se mapean a `*Info` en la capa de datos.

## Endpoints consumidos

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

## Pruebas realizadas

- `assembleDebug` finaliza correctamente.
- Crear reseña desde el detalle y ver el nuevo comentario al volver.
- Editar desde detalle y desde `Mi perfil`, verificando el cambio de estrellas.
- Eliminar desde detalle y desde `Mi perfil`, con diálogo de confirmación.
- El backend responde `201` al crear y `200` al actualizar/eliminar.
- Se restauró la base con `npm run def` después de las pruebas.
- Sin `FATAL EXCEPTION` en el logcat del emulador.

## Notas

- `adb reverse` se usa porque el emulador de este equipo no alcanza
  `10.0.2.2:3000` de forma estable; `127.0.0.1` funciona igual en emulador y
  dispositivo físico.
- El tráfico HTTP sin cifrar está limitado a `127.0.0.1` y `10.0.2.2` en
  `res/xml/network_security_config.xml`.
- Las capturas fueron tomadas en el emulador `Pixel_7` con el backend local.
