import { apiBuilder } from './api-builder';

const apiBase = {
  app: {
    url: '/info',
  },
  books: {
    url: '/books',
    userBooks: '/borrowed-books',
    reservedUserBooks: '/reserved-books-for-user',
    borrowedDate: '/borrowed-date',
    adminPanelList: '/admin-panel',
    adminBorrowedPanelList: '/admin-borrowed-panel',
    addBook: '/add-book',
    editBook: '/edit-book',
    deleteBook: '/${bookId}',
    // borrowedAdminSignatures: '/borrowed-signatures-for-admin',
    returnBookWithStatusAvailable: '/return-book-with-status-available',
    reminderOfTheBookFromAdmin: '/return-book-with-status-available-remind',
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
