import { Author } from './author';

export interface SearchDTO {
  title: string;
  genre: string;
  authors: Author[];
  availableQuantity: number;
  signatureQuantity: number;
  bookStatusForUser: string;
}
