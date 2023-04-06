import { AdminSignatureDTO } from './adminSignatureDTO';
import { AuthorDTO } from './authorDTO';

export interface BookDTO {
  bookId?: number;
  title: string;
  borrowedDate?: Date;
  returnDate?: Date;
  status?: string;
  pages?: number;
  genre: string;
  authorsDTOS: AuthorDTO[];
  adminSignatureDTO: AdminSignatureDTO[];
}
