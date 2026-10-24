import { env } from './config/env.js';
import { sequelize } from './database/database.js';
import './models/user.js';
import './models/article.js';
import './models/review.js';
import { setupRelations } from './models/relations.js';
import { loadInitialUsers } from './database/initUsers.js';
import { loadInitialArticles } from './database/initArticles.js';
import { loadInitialReviews } from './database/initReviews.js';
import app from './app.js';

try {
  await sequelize.authenticate();
  console.log('Conexion establecida con exito a la base de datos.');

  setupRelations();
  await sequelize.sync();

  await loadInitialUsers();
  await loadInitialArticles();
  await loadInitialReviews();

  app.listen(env.port, () => {
    console.log(`Servidor corriendo en http://localhost:${env.port}`);
  });
} catch (error) {
  console.error('Error al iniciar el servidor:', error.message);
  process.exit(1);
}
