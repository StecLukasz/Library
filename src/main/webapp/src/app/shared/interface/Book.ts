import { Author } from './Author';
import { Signature } from './Signature';

export interface Book {
  id: number;
  title: string;
  pages: number;
  genre: string;
  availableQuantity: number;
  authors: Author[];
  signatures: Signature[];
}
