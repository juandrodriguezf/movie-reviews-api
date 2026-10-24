import { Review } from '../models/review.js';
import { User } from '../models/user.js';
import { Article } from '../models/article.js';

export const createReview = async (req, res) => {
  try {
    const { userId, articleId } = req.body;

    if (!userId || !articleId) {
      return res.status(400).json({ error: 'userId y articleId son obligatorios' });
    }

    const user = await User.findByPk(userId);

    if (!user) {
      return res.status(404).json({ error: 'Usuario no encontrado' });
    }

    const article = await Article.findByPk(articleId);

    if (!article) {
      return res.status(404).json({ error: 'Articulo no encontrado' });
    }

    const review = await Review.create(req.body);
    res.status(201).json(review);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Error al crear la resena' });
  }
};

export const getReviewById = async (req, res) => {
  try {
    const { id } = req.params;
    const review = await Review.findByPk(id, {
      include: [
        {
          model: User,
          as: 'user',
          attributes: ['id', 'username', 'name', 'avatarUrl']
        },
        {
          model: Article,
          as: 'article',
          attributes: ['id', 'title', 'imageUrl']
        }
      ]
    });

    if (!review) {
      return res.status(404).json({ error: 'Resena no encontrada' });
    }

    res.json(review);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Error al obtener la resena' });
  }
};

export const updateReview = async (req, res) => {
  try {
    const { id } = req.params;
    const review = await Review.findByPk(id);

    if (!review) {
      return res.status(404).json({ error: 'Resena no encontrada' });
    }

    await review.update(req.body);
    res.json(review);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Error al actualizar la resena' });
  }
};

export const deleteReview = async (req, res) => {
  try {
    const { id } = req.params;
    const review = await Review.findByPk(id);

    if (!review) {
      return res.status(404).json({ error: 'Resena no encontrada' });
    }

    await review.destroy();
    res.sendStatus(204);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Error al eliminar la resena' });
  }
};

export const getArticleReviews = async (req, res) => {
  try {
    const { id } = req.params;
    const article = await Article.findByPk(id);

    if (!article) {
      return res.status(404).json({ error: 'Articulo no encontrado' });
    }

    const reviews = await Review.findAll({
      where: { articleId: id },
      include: [
        {
          model: User,
          as: 'user',
          attributes: ['id', 'username', 'name', 'avatarUrl']
        }
      ],
      order: [['createdAt', 'DESC']]
    });

    res.json(reviews);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Error al obtener las resenas del articulo' });
  }
};

export const getUserReviews = async (req, res) => {
  try {
    const { id } = req.params;
    const user = await User.findByPk(id);

    if (!user) {
      return res.status(404).json({ error: 'Usuario no encontrado' });
    }

    const reviews = await Review.findAll({
      where: { userId: id },
      include: [
        {
          model: Article,
          as: 'article',
          attributes: ['id', 'title', 'imageUrl']
        }
      ],
      order: [['createdAt', 'DESC']]
    });

    res.json(reviews);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Error al obtener las resenas del usuario' });
  }
};
