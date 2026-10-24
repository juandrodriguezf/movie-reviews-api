import 'dotenv/config';
import { Sequelize } from 'sequelize';

export const sequelize = new Sequelize('backend_express', 'postgres', process.env.DB_PASSWORD, {
  host: 'localhost',
  port: 5432,
  dialect: 'postgres'
});
