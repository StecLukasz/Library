import { Borrowed } from './borrowed';

export interface Signature {
  id: number;
  bookId: number;
  bookSignature: string;
  borrowed: Borrowed[];
}
