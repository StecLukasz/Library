import { Author } from './author';

export interface ReservedSignaturesForUserDTO {
  title: string;
  genre: string;
  authors: Author[];
}
