# Movie Reviews - Android

Cliente Android en Kotlin + Jetpack Compose que consume la API REST de reseñas de películas.

## Requisitos

- Android Studio (SDK 36 o superior)
- Emulador Android
- Backend corriendo en `http://localhost:3000`

## Ejecutar el backend

Desde la raíz del repositorio:

```bash
cd backend
npm install
npm run def
```

La guía completa del backend está en [`../backend/README.md`](../backend/README.md).

## Abrir la app

1. Abrir la carpeta `android/` en Android Studio (no la raíz del repositorio).
2. En **Settings → Build, Execution, Deployment → Build Tools → Gradle**, usar como Gradle JDK el `jbr-21`/`jbr-25` de Android Studio o un JDK 17/21.
3. Esperar el **Gradle Sync**. `local.properties` se genera automáticamente y no se versiona.
4. Ejecutar la app en un emulador.

## Conexión con el backend

Antes de abrir la app, crear el puente entre el equipo y el emulador:

```bash
adb reverse tcp:3000 tcp:3000
```

- URL base: `http://127.0.0.1:3000/`.
- El tráfico HTTP sin cifrar está permitido únicamente hacia `127.0.0.1` y `10.0.2.2` mediante `res/xml/network_security_config.xml`.
- `adb reverse` permite usar la misma URL en emulador y dispositivo físico, sin depender de la IP del equipo anfitrión.
- El usuario autenticado se fija en `id = 1`; no se usa Firebase ni se requiere `google-services.json`.

## Compilar por consola

Desde `android/`:

```bash
./gradlew assembleDebug
```

En Windows:

```powershell
.\gradlew.bat assembleDebug
```

El APK queda en `app/build/outputs/apk/debug/app-debug.apk`.
