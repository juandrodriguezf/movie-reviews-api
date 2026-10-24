import { Article } from '../models/article.js';

const articles = [
  {
    title: 'El viaje de Chihiro',
    director: 'Hayao Miyazaki',
    genre: 'Animacion',
    releaseYear: 2001,
    synopsis:
      'Una nina entra a un mundo de espiritus y debe trabajar en unos banos publicos para salvar a sus padres.',
    duration: 125,
    score: 9.3,
    imageUrl: 'https://picsum.photos/seed/chihiro/500/750'
  },
  {
    title: 'El padrino',
    director: 'Francis Ford Coppola',
    genre: 'Drama',
    releaseYear: 1972,
    synopsis:
      'El patriarca de una familia de la mafia intenta transferir el control de su imperio a su hijo menor.',
    duration: 175,
    score: 9.2,
    imageUrl: 'https://picsum.photos/seed/padrino/500/750'
  },
  {
    title: 'Interestelar',
    director: 'Christopher Nolan',
    genre: 'Ciencia ficcion',
    releaseYear: 2014,
    synopsis:
      'Un grupo de exploradores viaja a traves de un agujero de gusano para encontrar un nuevo hogar para la humanidad.',
    duration: 169,
    score: 8.7,
    imageUrl: 'https://picsum.photos/seed/interestelar/500/750'
  },
  {
    title: 'Pulp Fiction',
    director: 'Quentin Tarantino',
    genre: 'Crimen',
    releaseYear: 1994,
    synopsis:
      'Historias entrelazadas de dos sicarios, un boxeador y la esposa de un jefe criminal.',
    duration: 154,
    score: 8.9,
    imageUrl: 'https://picsum.photos/seed/pulpfiction/500/750'
  },
  {
    title: 'Ciudad de Dios',
    director: 'Fernando Meirelles',
    genre: 'Drama',
    releaseYear: 2002,
    synopsis:
      'La vida de dos jovenes en una favela de Rio de Janeiro toma caminos opuestos entre el crimen y la fotografia.',
    duration: 130,
    score: 8.6,
    imageUrl: 'https://picsum.photos/seed/ciudaddedios/500/750'
  },
  {
    title: 'El laberinto del fauno',
    director: 'Guillermo del Toro',
    genre: 'Fantasia',
    releaseYear: 2006,
    synopsis:
      'Una nina descubre un laberinto magico en la Espana franquista y recibe tres pruebas de un fauno.',
    duration: 118,
    score: 8.2,
    imageUrl: 'https://picsum.photos/seed/fauno/500/750'
  },
  {
    title: 'Volver',
    director: 'Pedro Almodovar',
    genre: 'Drama',
    releaseYear: 2006,
    synopsis:
      'Tres generaciones de mujeres de La Mancha enfrentan la muerte, los secretos y la supervivencia.',
    duration: 121,
    score: 7.6,
    imageUrl: 'https://picsum.photos/seed/volver/500/750'
  },
  {
    title: 'Relatos salvajes',
    director: 'Damian Szifron',
    genre: 'Comedia negra',
    releaseYear: 2014,
    synopsis:
      'Seis historias sobre la venganza y la desesperacion de personajes al borde del colapso.',
    duration: 122,
    score: 8.1,
    imageUrl: 'https://picsum.photos/seed/relatos/500/750'
  }
];

export async function loadInitialArticles() {
  const count = await Article.count();

  if (count > 0) {
    return;
  }

  await Article.bulkCreate(articles, { validate: true });
  console.log(`Articulos iniciales cargados: ${articles.length}`);
}
