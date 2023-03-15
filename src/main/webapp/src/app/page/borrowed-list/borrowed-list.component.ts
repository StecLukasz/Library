import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { firstValueFrom } from 'rxjs';
import { ApiService } from '../../api.service';
import { AuthService } from '../../core/auth/auth.service';
import { User } from '../../core/auth/model/user';
import { Book } from '../../shared/interface/book';
import { BookDTO } from '../../shared/interface/bookDTO';

@UntilDestroy()
@Component({
  selector: 'app-borrowed-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './borrowed-list.component.html',
  styleUrls: ['./borrowed-list.component.scss'],
})
export class BorrowedListComponent implements OnInit {
  borrowedBook: BookDTO[] = [];
  books: Book[] = [];
  username: string = '';
  displayedColumns: string[] = ['title', 'author', 'borrowedDate'];

  constructor(private api: ApiService, private authService: AuthService) {}

  async ngOnInit(): Promise<void> {
    // stream obserwujacy aktualnie zalogowanego użytkownika
    this.authService.user$.pipe(untilDestroyed(this)).subscribe((user) => {
      if (!user) {
        console.log('anonymous');
        // gdy użytkownik niezalogowany
        this.getBooks();
        return;
      }
      console.log(user.username);
      // zalogowany - user nie jest undefined
      this.username = user.username;
      this.getBooksForUser(user);
    });
  }

  private async getBooks(): Promise<void> {
    this.books = await firstValueFrom(this.api.getBooks());
  }

  private async getBooksForUser(user: User): Promise<void> {
    this.borrowedBook = await firstValueFrom(this.api.getBorrowedBooksForUser(user.username));
    console.log(this.books);
  }

  // private loadBorrowedList() {
  //   this.borrowedService.getBorrowedList().subscribe((borrowedList) => {
  //     this.borrowedList = borrowedList.map((borrowed) => ({
  //       ...borrowed,
  //       dueDate: this.getDueDate(new Date(borrowed.borrowedDate)),
  //     }));
  //   });
  // }
  private getDueDate(borrowedDate: Date): Date {
    //dodaj 3 meisiące do daty wypożyczenia
    const dueDate = new Date(borrowedDate.getTime());
    dueDate.setMonth(dueDate.getMonth() + 3);
    return dueDate;
  }
}
