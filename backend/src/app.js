import express from 'express';
import userRoutes from './routes/userRoots.js';
import articleRoutes from './routes/articles.js';
import reviewRoutes from './routes/reviews.js';

const app = express();

app.use(express.json());

app.use(userRoutes);
app.use(articleRoutes);
app.use(reviewRoutes);

export default app;
