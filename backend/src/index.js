import app from './app.js';
import { sequelize } from './database/database.js';
import './models/user.js';
import './models/article.js';
import './models/review.js';
import { setupRelations } from './models/relations.js';
import { loadInitialUsers } from './database/initUsers.js';
import { loadInitialArticles } from './database/initArticles.js';
import { loadInitialReviews } from './database/initReviews.js';

async function init() {
  try {
    await sequelize.authenticate();
    console.log('Conexion establecida con exito a la base de datos.');

    setupRelations();
    await sequelize.sync({ force: true });

    await loadInitialUsers();
    await loadInitialArticles();
    await loadInitialReviews();

    app.listen(3000, () => {
      console.log('Servidor corriendo en http://localhost:3000');
    });
  } catch (error) {
    console.error('Error al conectar a la base de datos:', error);
  }
}

init();
