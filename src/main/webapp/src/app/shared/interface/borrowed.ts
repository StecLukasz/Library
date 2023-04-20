export interface Borrowed {
  id: number;
  username: string;
  signatureId: string;
  borrowedDate?: Date;
  overdueDate?: Date;
  returnDate?: Date;
  statusDate?: Date;
  status: string;
}
