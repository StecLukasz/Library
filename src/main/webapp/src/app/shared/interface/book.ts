import { Author } from './author';
import { Signature } from './signature';

export interface Book {
  id: number;
  title: string;
  pages: number;
  genre: string;
  availableQuantity: number;
  bookStatusForUser: string;
  authors: Author[];
  signatures: Signature[];
}
