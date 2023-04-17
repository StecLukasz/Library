import { AdminSignatureDTO } from './adminSignatureDTO';
import { AuthorDTO } from './authorDTO';

export interface BookDTO {
  id?: number;
  bookId?: number;
  title: string;
  borrowedDate?: Date;
  returnDate?: Date;
  status?: string;
  pages?: number;
  genre: string;
  authorDTO: AuthorDTO[];
  adminSignatureDTO: AdminSignatureDTO[];
}
