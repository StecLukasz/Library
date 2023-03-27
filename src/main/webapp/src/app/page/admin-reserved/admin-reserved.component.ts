import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { firstValueFrom } from 'rxjs';
import { ApiService } from '../../api.service';
import { AuthService } from '../../core/auth/auth.service';
import { AdminSignatureReservedDTO } from '../../shared/interface/AdminSignatureReservedDTO';

@UntilDestroy()
@Component({
  selector: 'app-admin-reserved',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-reserved.component.html',
  styleUrls: ['./admin-reserved.component.scss'],
})
export class AdminReservedComponent implements OnInit {
  AdminSignatureReservedDTOs: AdminSignatureReservedDTO[] = [];
  sortDirection: 'asc' | 'desc' = 'asc'; // default sort direction is ascending

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
      console.log('zalogowany user' + user.username);
      // zalogowany - user nie jest undefined
      this.getBooks();
    });
  }

  private async getBooks(): Promise<void> {
    this.AdminSignatureReservedDTOs = await firstValueFrom(this.api.getSignaturesReserved());
    this.sortByStatus(); // sort by status after fetching the data
  }

  sortByStatus(): void {
    this.AdminSignatureReservedDTOs.sort((a, b) => {
      if (a.status > b.status) {
        return this.sortDirection === 'asc' ? 1 : -1;
      } else if (a.status < b.status) {
        return this.sortDirection === 'asc' ? -1 : 1;
      } else {
        return 0;
      }
    });
  }

  toggleSortDirection(): void {
    this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    this.sortByStatus();
  }
}
