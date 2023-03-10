import { Author } from './author';
import { Signature } from './signature';

export interface Book {
  id: number;
  title: string;
  pages: number;
  genre: string;
  authors: Author[];
  signatures: Signature[];
}
