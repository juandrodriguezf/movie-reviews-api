import { User } from '../models/user.js';

const users = [
  {
    username: 'juan.rodriguez',
    email: 'juan.rodriguez@javeriana.edu.co',
    name: 'Juan Rodriguez',
    avatarUrl: 'https://i.pravatar.cc/150?img=11'
  },
  {
    username: 'camilo.cine',
    email: 'camilo.rojas@javeriana.edu.co',
    name: 'Camilo Rojas',
    avatarUrl: 'https://i.pravatar.cc/150?img=12'
  },
  {
    username: 'valeria_f',
    email: 'valeria.fernandez@javeriana.edu.co',
    name: 'Valeria Fernandez',
    avatarUrl: 'https://i.pravatar.cc/150?img=32'
  },
  {
    username: 'diego_rz',
    email: 'diego.ramirez@javeriana.edu.co',
    name: 'Diego Ramirez',
    avatarUrl: 'https://i.pravatar.cc/150?img=15'
  },
  {
    username: 'sara.mendez',
    email: 'sara.mendez@javeriana.edu.co',
    name: 'Sara Mendez',
    avatarUrl: 'https://i.pravatar.cc/150?img=45'
  }
];

export async function loadInitialUsers() {
  const count = await User.count();

  if (count > 0) {
    return;
  }

  await User.bulkCreate(users, { validate: true });
  console.log(`Usuarios iniciales cargados: ${users.length}`);
}
