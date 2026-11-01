import { Article } from '../models/article.js';

export const getArticles = async (req, res) => {
  try {
    const articles = await Article.findAll();
    res.json(articles);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Error al obtener los articulos' });
  }
};

export const getArticleById = async (req, res) => {
  try {
    const { id } = req.params;
    const article = await Article.findByPk(id);

    if (!article) {
      return res.status(404).json({ error: 'Articulo no encontrado' });
    }

    res.json(article);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Error al obtener el articulo' });
  }
};
