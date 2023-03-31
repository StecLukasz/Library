import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { firstValueFrom } from 'rxjs';
import { ApiService } from '../../api.service';
import { AuthService } from '../../core/auth/auth.service';
import { User } from '../../core/auth/model/user';
import { Book } from '../../shared/interface/book';

@UntilDestroy()
@Component({
  selector: 'app-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss'],
})
export class ListComponent implements OnInit {
  books: Book[] = [];
  search: string = '';
  currentUserLogin: string = '';
  user?: User;
  isBookServedByUser: boolean = false;

  constructor(private api: ApiService, private authService: AuthService) {}

  async ngOnInit(): Promise<void> {
    // stream obserwujacy aktualnie zalogowanego użytkownika
    this.authService.user$.pipe(untilDestroyed(this)).subscribe((user) => {
      if (!user) {
        console.log('anonymous');
        // gdy użytkownik niezalogowany
        //this.getBooks();
        this.onSearch();
        return;
      }
      this.user = user;
      this.currentUserLogin = user.username;

      console.log('zalogowany user' + user.username);
      // zalogowany - user nie jest undefined
      // this.getBooksForUser(user);
      //this.getBooks();
      this.onSearch();
    });
  }

  private async getBooks(): Promise<void> {
    this.books = await firstValueFrom(this.api.getBooks());
  }

  // private async getBooksForUser(user: User): Promise<void> {
  //   this.books = await firstValueFrom(this.api.getBooksForUser(user.username));
  //   console.log(this.books);
  // }

  postReservedBookByUser(book: Book) {
    this.api.postReserveBookByUser(this.currentUserLogin, book.title).subscribe(
      (data) => {},
      (error) => console.log(error)
    );
    // this.getBooks();
    // console.log(this.getBooks());
    this.onSearch();
    console.log(this.onSearch());
  }

  onSearch() {
    this.getBooksSearch(this.search);
    console.log(this.search);
  }

  private async getBooksSearch(text: string): Promise<void> {
    this.books = await firstValueFrom(this.api.getBooksSearch(text, this.currentUserLogin));
  }

  getSignatureQuantity(book: Book): number {
    return book.signatures.length;
  }

  isBookServed(bookId: number): boolean {
    console.log('isbookreserved ' + bookId);
    return true;
  }

  private async bookReservedById(id: number, login: string): Promise<void> {
    this.books = await firstValueFrom(this.api.getReservedBookByIdAndLogin(id, login));
  }
}
