# Backend - Movie Reviews API

API REST para reseñas de películas construida con **Express**, **Sequelize** y **PostgreSQL**. Expone usuarios y películas en modo lectura y un CRUD completo de reseñas, cada una asociada a un usuario y a una película.

Forma parte del monorepo [Movie Reviews](../README.md). Ejecutar los comandos desde `backend/`.

## Estructura

| Ruta | Contenido |
|---|---|
| `src/` | Código del backend |
| `../postman/` | Colección y environment de Postman |
| `../android/` | Cliente Android (Compose) |

## Stack

| Tecnología | Uso |
|---|---|
| Node.js + Express | Servidor HTTP y definición de rutas |
| Sequelize + pg | ORM y driver de PostgreSQL |
| PostgreSQL | Base de datos relacional |
| dotenv | Lectura de la contraseña de la base de datos |
| nodemon | Recarga automática en desarrollo |

## Requisitos

- Node.js >= 18
- PostgreSQL en `localhost:5432`
- Postman (pruebas de la API)
- DBeaver o pgAdmin (administración de la base de datos)

## Instalación

```bash
git clone <url-del-repositorio>
cd <repositorio>/backend
npm install
```

## Variable de entorno

Copiar `.env.example` a `.env` dentro de `backend/` y completar la contraseña:

| Variable | Descripción | Ejemplo |
|---|---|---|
| `DB_PASSWORD` | Contraseña del usuario `postgres` | `tu_password` |

El nombre de la base de datos, el usuario, el host y el puerto están definidos en `src/database/database.js`. `backend/.env` está en `.gitignore` y nunca debe subirse al repositorio.

## Base de datos

```sql
CREATE DATABASE backend_express;
```

Al arrancar, `src/index.js` ejecuta `sequelize.sync({ force: true })`: **elimina y vuelve a crear las tablas en cada inicio** y luego carga los datos iniciales. Es el comportamiento de la clase, útil durante el desarrollo; no debe usarse en producción.

## Scripts

Ejecutar desde `backend/`.

| Comando | Descripción |
|---|---|
| `npm run def` | Inicia el servidor con nodemon en el puerto 3000 |

## Estructura del backend

```text
src/
├── index.js              Punto de entrada: conexión, sync, datos iniciales y listen
├── app.js                Instancia de Express, express.json y rutas
├── database/
│   ├── database.js       Instancia de Sequelize
│   ├── initUsers.js      Datos iniciales de usuarios
│   ├── initArticles.js   Datos iniciales de películas
│   └── initReviews.js    Datos iniciales de reseñas
├── models/
│   ├── user.js
│   ├── article.js
│   ├── review.js
│   └── relations.js
├── controllers/
│   ├── users.controller.js
│   ├── articles.controller.js
│   └── reviews.controller.js
└── routes/
    ├── userRoots.js
    ├── articles.js
    └── reviews.js
```

## Endpoints

Formato de error en todas las respuestas: `{ "error": "mensaje" }`.

`DELETE /reviews/:id` responde `200` con `{ "message": "Resena eliminada correctamente" }`, de modo que clientes como Retrofit puedan leer el cuerpo de la respuesta.

| Método | Ruta | Descripción | Estado |
|---|---|---|---|
| GET | `/users` | Lista de usuarios | Disponible |
| GET | `/users/:id` | Usuario por id | Disponible |
| GET | `/users/:id/reviews` | Reseñas de un usuario | Disponible |
| GET | `/articles` | Lista de películas | Disponible |
| GET | `/articles/:id` | Detalle de una película | Disponible |
| GET | `/articles/:id/reviews` | Reseñas de una película | Disponible |
| POST | `/reviews` | Crea una reseña | Disponible |
| GET | `/reviews/:id` | Detalle de una reseña | Disponible |
| PUT | `/reviews/:id` | Actualiza una reseña | Disponible |
| DELETE | `/reviews/:id` | Elimina una reseña y responde `200` con un mensaje | Disponible |

## Postman

En `../postman/` (raíz del repositorio) están la colección y el environment:

1. Postman → **Import** → seleccionar los dos archivos.
2. Elegir el environment **movie-reviews-api - local**.
3. Ajustar `userId`, `articleId` y `reviewId` según los datos creados.

`baseUrl` apunta a `http://localhost:3000`.

## Estado del proyecto

API completa: lecturas de usuarios y películas, CRUD de reseñas y consultas de reseñas por usuario y por película.
