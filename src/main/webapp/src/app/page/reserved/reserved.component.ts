import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { firstValueFrom } from 'rxjs';
import { ApiService } from '../../api.service';
import { AuthService } from '../../core/auth/auth.service';
import { Book } from '../../shared/interface/book';

@UntilDestroy()
@Component({
  selector: 'app-reserved',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reserved.component.html',
  styleUrls: ['./reserved.component.scss'],
})
export class ReservedComponent implements OnInit {
  books: Book[] = [];
  currentUser: string = '';
  isButtonDisabled = false;

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
      this.currentUser = user.username;

      console.log('zalogowany user' + user.username);
      // zalogowany - user nie jest undefined
      this.getReservedBooksForUser(this.currentUser);
    });
  }

  private async getBooks(): Promise<void> {
    this.books = await firstValueFrom(this.api.getBooks());
  }

  private async getReservedBooksForUser(user: string): Promise<void> {
    this.books = await firstValueFrom(this.api.getReservedBooksForUser(this.currentUser));
  }

  postCancelReservedBookByUser(book: Book) {
    if (this.isButtonDisabled === false) {
      this.isButtonDisabled = true;

      this.api.postCancelReservedBookByUser(this.currentUser, book.title).subscribe(
        (data) => {},
        (error) => console.log(error)
      );

      setTimeout(() => {
        this.isButtonDisabled = false;
      }, 350);

      this.getReservedBooksForUser(this.currentUser);
      console.log(this.getReservedBooksForUser(this.currentUser));
    }
  }
}
