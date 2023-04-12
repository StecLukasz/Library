import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { firstValueFrom } from 'rxjs';
import { ApiService } from '../../api.service';
import { AuthService } from '../../core/auth/auth.service';
import { User } from '../../core/auth/model/user';
import { Book } from '../../shared/interface/book';
import { SearchDTO } from '../../shared/interface/searchDTO';

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
  searchBooks: SearchDTO[] = [];
  search: string = '';
  currentUserLogin: string = '';
  user?: User;
  isButtonDisabled = false;
  genre: string = '';

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

      this.onSearch();
    });
    this.isButtonDisabled = false;
  }

  filterBooksByGenre(genre: string): void {
    this.genre = genre;
    setTimeout(() => {
      this.getBooksSearch(this.search, this.genre);
    }, 50);
    console.log(this.searchBooks);
  }

  private async getBooks(): Promise<void> {
    this.books = await firstValueFrom(this.api.getBooks());
  }

  postReservedBookByUser(searchBook: SearchDTO) {
    this.postReservation(searchBook);
  }

  postReservation(searchBook: SearchDTO) {
    if (this.isButtonDisabled === false) {
      this.isButtonDisabled = true;

      this.api.postReserveBookByUser(this.currentUserLogin, searchBook.title).subscribe(
        (data) => {
          if (data === 1) {
            setTimeout(() => {
              this.isButtonDisabled = false;
              this.getBooksSearch(this.search, this.genre);
            }, 350);
          }
        },
        (error) => console.log(error)
      );
    }
  }

  onSearch() {
    this.getBooksSearch(this.search, this.genre);
    if (this.search === '') this.genre = '';
  }

  private async getBooksSearch(text: string, genre: string): Promise<void> {
    this.searchBooks = await firstValueFrom(this.api.getBooksSearch(text, this.currentUserLogin, genre));
  }
}
