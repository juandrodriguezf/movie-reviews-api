import express from 'express';
import userRoutes from './routes/userRoots.js';
import articleRoutes from './routes/articles.js';

const app = express();

app.use(express.json());

app.use(userRoutes);
app.use(articleRoutes);

export default app;
