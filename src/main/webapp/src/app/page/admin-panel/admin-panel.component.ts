import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { firstValueFrom } from 'rxjs';
import { ApiService } from '../../api.service';
import { AuthService } from '../../core/auth/auth.service';
import { Book } from '../../shared/interface/book';
import { SignatureDTO } from '../../shared/interface/signatureDTO';

@UntilDestroy()
@Component({
  selector: 'app-admin-panel',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, RouterLinkActive, MatButtonModule, MatIconModule],
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.scss'],
})
export class AdminPanelComponent implements OnInit {
  book: Book[] = [];
  adminPanelDTOs: SignatureDTO[] = [];
  sortDirection: 'asc' | 'desc' = 'asc'; // default sort direction is ascending
  currentUser: string = '';
  search: string = '';
  bookId: string = '';
  signatureId: string = '';

  constructor(private api: ApiService, private authService: AuthService, private router: Router) {}

  async ngOnInit(): Promise<void> {
    // stream obserwujacy aktualnie zalogowanego użytkownika
    this.authService.user$.pipe(untilDestroyed(this)).subscribe((user) => {
      // if (!user) {
      //   console.log('anonymous');
      //   // gdy użytkownik niezalogowany
      //   this.getListBookForAdmin();
      //   return;
      // }
      console.log('zalogowany admin ' + user?.username);
      // zalogowany - user nie jest undefined
      this.getListBookForAdmin();
    });
  }

  private async getListBookForAdmin(): Promise<void> {
    this.adminPanelDTOs = await firstValueFrom(this.api.getSignaturesForAdminPanel());
    this.sortByTitle();
  }

  sortByStatus(): void {
    this.adminPanelDTOs.sort((a, b) => {
      const statusOrder = ['available', 'reserved', 'ready', 'borrowed'];
      const aIndex = statusOrder.indexOf(a.status);
      const bIndex = statusOrder.indexOf(b.status);

      if (aIndex > bIndex) {
        return this.sortDirection === 'asc' ? 1 : -1;
      } else if (aIndex < bIndex) {
        return this.sortDirection === 'asc' ? -1 : 1;
      } else {
        return 0;
      }
    });
  }

  sortByUser(): void {
    this.adminPanelDTOs.sort((a, b) => {
      const usernameA = a.username.toLowerCase();
      const usernameB = b.username.toLowerCase();
      if (usernameA < usernameB) {
        return this.sortDirection === 'asc' ? -1 : 1;
      } else if (usernameA > usernameB) {
        return this.sortDirection === 'asc' ? 1 : -1;
      } else {
        return 0;
      }
    });
  }

  sortByTitle(): void {
    this.adminPanelDTOs.sort((a, b) => {
      const titleA = a.title.toLowerCase();
      const titleB = b.title.toLowerCase();
      if (titleA < titleB) {
        return this.sortDirection === 'asc' ? -1 : 1;
      } else if (titleA > titleB) {
        return this.sortDirection === 'asc' ? 1 : -1;
      } else {
        return 0;
      }
    });
  }

  toggleSortDirectionByTitle(): void {
    this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    this.sortByTitle();
    // this.sortByUser();
  }

  toggleSortDirectionByStatus(): void {
    this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    this.sortByStatus();
    // this.sortByUser();
  }
  toggleSortDirectionByUsername(): void {
    this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    this.sortByUser();
  }

  onSearch() {
    this.getBooksSearch(this.search);
    console.log(this.search);
  }

  private async getBooksSearch(text: string): Promise<void> {
    this.adminPanelDTOs = await firstValueFrom(this.api.getBooksSearchForAdmin(text));
  }

  public async deleteOneSignatureAsAdmin(signatureId: number): Promise<void> {
    await firstValueFrom(this.api.deleteOneSignature(signatureId));
    this.getListBookForAdmin();
  }
}
