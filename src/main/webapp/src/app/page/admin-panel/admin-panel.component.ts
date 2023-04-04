import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { firstValueFrom } from 'rxjs';
import { ApiService } from '../../api.service';
import { AuthService } from '../../core/auth/auth.service';
import { AdminSignatureDTO } from '../../shared/interface/adminSignatureDTO';

@UntilDestroy()
@Component({
  selector: 'app-admin-panel',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, RouterLinkActive],
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.scss'],
})
export class AdminPanelComponent implements OnInit {
  adminPanelDTOs: AdminSignatureDTO[] = [];
  sortDirection: 'asc' | 'desc' = 'asc'; // default sort direction is ascending
  currentUser: string = '';
  search: string = '';

  constructor(private api: ApiService, private authService: AuthService) {}

  async ngOnInit(): Promise<void> {
    // stream obserwujacy aktualnie zalogowanego użytkownika
    this.authService.user$.pipe(untilDestroyed(this)).subscribe((user) => {
      // if (!user) {
      //   console.log('anonymous');
      //   // gdy użytkownik niezalogowany
      //   this.getListBookForAdmin();
      //   return;
      // }
      console.log('zalogowany admin' + user?.username);
      // zalogowany - user nie jest undefined
      this.getListBookForAdmin();
    });
  }

  private async getListBookForAdmin(): Promise<void> {
    this.adminPanelDTOs = await firstValueFrom(this.api.getSignaturesForAdminPanel());
    this.sortByStatus(); // sort by status after fetching the data
  }

  sortByStatus(): void {
    this.adminPanelDTOs.sort((a, b) => {
      if (a.status > b.status) {
        return this.sortDirection === 'asc' ? 1 : -1;
      } else if (a.status < b.status) {
        return this.sortDirection === 'asc' ? -1 : 1;
      } else {
        return 0;
      }
    });
  }
  sortByUser(): void {
    this.adminPanelDTOs.sort((a, b) => {
      if (a.username > b.username) {
        return this.sortDirection === 'asc' ? 1 : -1;
      } else if (a.username < b.username) {
        return this.sortDirection === 'asc' ? -1 : 1;
      } else {
        return 0;
      }
    });
  }

  toggleSortDirection(): void {
    this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    this.sortByStatus();
    this.sortByUser();
  }
  onSearch() {
    this.getBooksSearch(this.search);
  }

  private async getBooksSearch(text: string): Promise<void> {
    this.adminPanelDTOs = await firstValueFrom(this.api.getBooksSearchForAdmin(text));
  }

  async editBook(book: AdminSignatureDTO): Promise<void> {
    // TODO: implementacja metody edytującej książkę
  }

  async deleteBook(book: AdminSignatureDTO): Promise<void> {
    // TODO: implementacja metody usuwającej książkę
  }

  // addBook() {
  //   this.api.addBookAdmin(this.book).subscribe(
  //     (book) => {
  //       console.log('Książka dodana', book);
  //     },
  //     (error) => {
  //       console.log('Błąd podczas dodawania książki', error);
  //     }
  //   );
  // }
}
