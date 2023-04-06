import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { firstValueFrom } from 'rxjs';
import { ApiService } from '../../api.service';
import { AuthService } from '../../core/auth/auth.service';
import { Book } from '../../shared/interface/book';
import { ReservedSignaturesForUserDTO } from '../../shared/interface/reservedSignaturesForUserDTO';

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
  reservedBooks: ReservedSignaturesForUserDTO[] = [];
  currentUser: string = '';
  isButtonDisabled = false;

  constructor(private api: ApiService, private authService: AuthService, private router: Router) {}

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
      this.getReservedBooksForUser();
    });
  }

  private async getBooks(): Promise<void> {
    this.books = await firstValueFrom(this.api.getBooks());
  }

  private async getReservedBooksForUser(): Promise<void> {
    this.reservedBooks = await firstValueFrom(this.api.getReservedBooksForUser(this.currentUser));
  }

  postCancelReservedBookByUser(reservedBook: ReservedSignaturesForUserDTO) {
    if (this.isButtonDisabled === false) {
      this.isButtonDisabled = true;

      this.api.postCancelReservedBookByUser(this.currentUser, reservedBook.title).subscribe(
        (data) => {},
        (error) => console.log(error)
      );

      setTimeout(() => {
        this.isButtonDisabled = false;
        this.getReservedBooksForUser();
      }, 300);
    }
  }
}
