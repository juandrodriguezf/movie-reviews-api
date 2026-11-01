import { User } from './user.js';
import { Article } from './article.js';
import { Review } from './review.js';

export function setupRelations() {
  User.hasMany(Review, {
    foreignKey: 'userId',
    as: 'reviews',
    onDelete: 'CASCADE',
    hooks: true
  });
  Review.belongsTo(User, {
    foreignKey: 'userId',
    as: 'user'
  });

  Article.hasMany(Review, {
    foreignKey: 'articleId',
    as: 'reviews',
    onDelete: 'CASCADE',
    hooks: true
  });
  Review.belongsTo(Article, {
    foreignKey: 'articleId',
    as: 'article'
  });
}
