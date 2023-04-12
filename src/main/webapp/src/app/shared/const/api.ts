import { apiBuilder } from './api-builder';

const apiBase = {
  app: {
    url: '/info',
  },
  books: {
    url: '/books',
    userBooks: '/books-for-user',
    reservedUserBooks: '/reserved-books-for-user',
    reservedAdminSignatures: '/reserved-signatures-for-admin',
    reserve: '/reserve',
    cancel_reserved: '/cancelReserved',
    cancel_signature_reservation: '/cancelSignatureReservation',
    ready_signature_reservation: '/readySignatureReservation',
    borrow_signature: '/borrowSignature',
    searchWithGenreList: '/search-with-genre-list?',
  },
};

const authBase = {
  login: '/soft_oauth/login',
  logout: '/api/logout',
  me: '/api/me',
  users: '/api/users',
};

export const api = apiBuilder(apiBase, '/api') as typeof apiBase;
export const auth = apiBuilder(authBase, '') as typeof authBase;
