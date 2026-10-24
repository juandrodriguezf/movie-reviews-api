import { env } from './config/env.js';
import { sequelize } from './database/database.js';
import app from './app.js';

try {
  await sequelize.authenticate();
  console.log('Conexion establecida con exito a la base de datos.');

  app.listen(env.port, () => {
    console.log(`Servidor corriendo en http://localhost:${env.port}`);
  });
} catch (error) {
  console.error('Error al conectar a la base de datos:', error.message);
  process.exit(1);
}
