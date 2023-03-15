export interface BookDTO {
  bookId: number;
  title: string;
  pages: number;
  borrowedDate: Date;
  returnDate: Date;
  status: string;
  availableQuantity: number;
  author: string;
}
