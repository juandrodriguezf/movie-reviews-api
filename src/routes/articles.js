import { Router } from 'express';
import { getArticles, getArticleById } from '../controllers/articles.controller.js';

const router = Router();

router.get('/articles', getArticles);
router.get('/articles/:id', getArticleById);

export default router;
