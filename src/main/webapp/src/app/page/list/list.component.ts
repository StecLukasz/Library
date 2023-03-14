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
      this.getBooksForUser(user);
    });
  }

  private async getBooks(): Promise<void> {
    this.books = await firstValueFrom(this.api.getBooks());
  }

  private async getBooksForUser(user: User): Promise<void> {
    this.books = await firstValueFrom(this.api.getBooksForUser(user.username));

    console.log(this.books);
  }

  onSubmit() {
    this.getSentencesSearch(this.search);
    console.log(this.search);
  }

  private getSentencesSearch(text: string) {
    this.api.getBooksSearch(text).subscribe((data) => {
      this.books = data;
    });
  }
}
