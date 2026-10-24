# backend_express

API REST para reseñas de películas construida con **Express**, **Sequelize** y **PostgreSQL**. Expone usuarios y películas en modo lectura y un CRUD completo de reseñas, cada una asociada a un usuario y a una película.

## Stack

| Tecnología | Uso |
|---|---|
| Node.js + Express 5 | Servidor HTTP y definición de rutas |
| Sequelize 6 + pg | ORM y driver de PostgreSQL |
| PostgreSQL 18 | Base de datos relacional |
| morgan | Log de peticiones HTTP |
| dotenv | Variables de entorno |
| nodemon | Recarga automática en desarrollo |

## Requisitos

- Node.js >= 18
- PostgreSQL en `localhost:5432`
- Postman (pruebas de la API)
- DBeaver o pgAdmin (administración de la base de datos)

## Instalación

```bash
git clone <url-del-repositorio>
cd backend_express
npm install
```

## Variables de entorno

Copiar `.env.example` a `.env` y completar los valores:

| Variable | Descripción | Ejemplo |
|---|---|---|
| `NODE_ENV` | Entorno de ejecución | `development` |
| `PORT` | Puerto del servidor | `3000` |
| `DB_NAME` | Nombre de la base de datos | `backend_express` |
| `DB_USER` | Usuario de PostgreSQL | `postgres` |
| `DB_PASSWORD` | Contraseña de PostgreSQL | `tu_password` |
| `DB_HOST` | Host de la base de datos | `localhost` |
| `DB_PORT` | Puerto de PostgreSQL | `5432` |

`.env` está en `.gitignore` y nunca debe subirse al repositorio.

## Base de datos

```sql
CREATE DATABASE backend_express;
```

Por ahora el arranque solo verifica la conexión (`sequelize.authenticate()`). La creación de tablas se activa con `sequelize.sync()` en la fase de modelos.

Para reiniciar el esquema desde cero:

```sql
DROP DATABASE IF EXISTS backend_express;
CREATE DATABASE backend_express;
```

## Scripts

| Comando | Descripción |
|---|---|
| `npm run dev` | Inicia el servidor con nodemon |
| `npm start` | Inicia el servidor con node |

## Estructura

```text
src/
├── index.js              Punto de entrada: conexión a la BD y listen
├── app.js                Instancia de Express, middlewares y rutas
├── config/env.js         Carga y validación de variables de entorno
├── database/database.js  Instancia de Sequelize
├── models/               Modelos y relaciones
├── controllers/          Lógica de cada recurso
├── routes/               Definición de endpoints
└── middlewares/          Validaciones y manejo de errores
```

## Endpoints

Formato de error en todas las respuestas: `{ "error": "mensaje" }`.

| Método | Ruta | Descripción | Estado |
|---|---|---|---|
| GET | `/` | Estado del servicio | Disponible |
| GET | `/users` | Lista de usuarios | Pendiente |
| GET | `/users/:id` | Usuario por id | Pendiente |
| GET | `/users/:id/reviews` | Reseñas de un usuario | Pendiente |
| GET | `/articles` | Lista de películas | Pendiente |
| GET | `/articles/:id` | Detalle de una película | Pendiente |
| GET | `/articles/:id/reviews` | Reseñas de una película | Pendiente |
| POST | `/reviews` | Crea una reseña | Pendiente |
| GET | `/reviews/:id` | Detalle de una reseña | Pendiente |
| PUT | `/reviews/:id` | Actualiza una reseña | Pendiente |
| DELETE | `/reviews/:id` | Elimina una reseña | Pendiente |

## Postman

En `postman/` están la colección y el environment:

1. Postman → **Import** → seleccionar los dos archivos.
2. Elegir el environment **backend_express - local**.
3. Ajustar `userId`, `articleId` y `reviewId` según los datos creados.

`baseUrl` apunta a `http://localhost:3000`.

## Estado del proyecto

Andamiaje listo y verificado (conexión a PostgreSQL + servidor en el puerto 3000). Pendiente: modelos, relaciones, datos iniciales, controladores y rutas.
