import { Review } from '../models/review.js';
import { User } from '../models/user.js';
import { Article } from '../models/article.js';

export async function loadInitialReviews() {
  const count = await Review.count();

  if (count > 0) {
    return;
  }

  const users = await User.findAll({
    attributes: ['id'],
    order: [['id', 'ASC']]
  });
  const articles = await Article.findAll({
    attributes: ['id'],
    order: [['id', 'ASC']]
  });

  if (users.length === 0 || articles.length === 0) {
    return;
  }

  const reviews = [
    {
      userId: users[0].id,
      articleId: articles[0].id,
      rating: 5,
      comment: 'Una obra maestra de la animacion, cada plano vale la pena.'
    },
    {
      userId: users[1].id,
      articleId: articles[1].id,
      rating: 5,
      comment: 'El guion y las actuaciones siguen siendo impecables.'
    },
    {
      userId: users[2].id,
      articleId: articles[2].id,
      rating: 4,
      comment: 'La parte cientifica es discutible, pero la experiencia es enorme.'
    },
    {
      userId: users[0].id,
      articleId: articles[3].id,
      rating: 5,
      comment: 'La estructura narrativa no envejece ni un poco.'
    },
    {
      userId: users[3].id,
      articleId: articles[4].id,
      rating: 4,
      comment: 'Cruda y potente, con un ritmo que no da respiro.'
    },
    {
      userId: users[4].id,
      articleId: articles[5].id,
      rating: 5,
      comment: 'Fantasia y guerra mezcladas con una sensibilidad unica.'
    },
    {
      userId: users[2].id,
      articleId: articles[6].id,
      rating: 4,
      comment: 'Los personajes femeninos sostienen toda la pelicula.'
    },
    {
      userId: users[1].id,
      articleId: articles[7].id,
      rating: 3,
      comment: 'Divertida por momentos, aunque irregular como antologia.'
    }
  ];

  await Review.bulkCreate(reviews, { validate: true });
  console.log(`Resenas iniciales cargadas: ${reviews.length}`);
}
