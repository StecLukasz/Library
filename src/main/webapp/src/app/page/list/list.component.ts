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
    this.isButtonDisabled = false;
  }

  private async getBooks(): Promise<void> {
    this.books = await firstValueFrom(this.api.getBooks());
  }

  // private async getBooksForUser(user: User): Promise<void> {
  //   this.books = await firstValueFrom(this.api.getBooksForUser(user.username));
  //   console.log(this.books);
  // }

  postReservedBookByUser(searchBook: SearchDTO) {
    this.postReservation(searchBook);
    this.onSearch();
  }

  postReservation(searchBook: SearchDTO) {
    if (this.isButtonDisabled === false) {
      this.isButtonDisabled = true;

      this.api.postReserveBookByUser(this.currentUserLogin, searchBook.title).subscribe(
        (data) => {
          if (data === 1) {
            setTimeout(() => {
              this.isButtonDisabled = false;
            }, 450);
          }
        },
        (error) => console.log(error)
      );
    }
    this.onSearch();
  }

  onSearch() {
    this.getBooksSearch(this.search);
    console.log(this.search);
  }

  private async getBooksSearch(text: string): Promise<void> {
    this.searchBooks = await firstValueFrom(this.api.getBooksSearch(text, this.currentUserLogin));
  }

  // getSignatureQuantity(searchBook: SearchDTO): number {
  //   return book.signatures.length;
  // }

  private async bookReservedById(id: number, login: string): Promise<void> {
    this.books = await firstValueFrom(this.api.getReservedBookByIdAndLogin(id, login));
  }
}
