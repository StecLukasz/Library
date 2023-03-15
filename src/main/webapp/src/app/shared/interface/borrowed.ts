export interface Borrowed {
  id: number;
  username: string;
  signatureId: string;
  borrowedDate?: Date;
  overdueDate?: Date;
  returnDate?: Date;
  status: string;
}
