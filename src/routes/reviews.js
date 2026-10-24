import { Router } from 'express';
import {
  createReview,
  getReviewById,
  updateReview,
  deleteReview,
  getArticleReviews,
  getUserReviews
} from '../controllers/reviews.controller.js';

const router = Router();

router.post('/reviews', createReview);
router.get('/reviews/:id', getReviewById);
router.put('/reviews/:id', updateReview);
router.delete('/reviews/:id', deleteReview);

router.get('/articles/:id/reviews', getArticleReviews);
router.get('/users/:id/reviews', getUserReviews);

export default router;
