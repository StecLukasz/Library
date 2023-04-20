import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { firstValueFrom } from 'rxjs';
import { ApiService } from '../../api.service';
import { AuthService } from '../../core/auth/auth.service';
import { SignatureDTO } from '../../shared/interface/signatureDTO';

@UntilDestroy()
@Component({
  selector: 'app-admin-borrowed-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-borrowed-list.component.html',
  styleUrls: ['./admin-borrowed-list.component.scss'],
})
export class AdminBorrowedListComponent implements OnInit {
  adminPanelDTOs: SignatureDTO[] = [];
  sortDirection: 'asc' | 'desc' = 'asc'; // default sort direction is ascending
  username: string = '';
  isButtonDisabled = false;

  constructor(private api: ApiService, private authService: AuthService) {}

  async ngOnInit(): Promise<void> {
    // stream obserwujacy aktualnie zalogowanego użytkownika
    this.authService.user$.pipe(untilDestroyed(this)).subscribe((user) => {
      // if (!user) {
      //   console.log('anonymous');
      //   // gdy użytkownik niezalogowany
      //   this.getBooks();
      //   return;
      // }

      // zalogowany - user nie jest undefined
      // this.username = user.username;
      this.getBorrowedBooksForAdmin();
    });
  }

  private async getBorrowedBooksForAdmin(): Promise<void> {
    this.adminPanelDTOs = await firstValueFrom(this.api.getSignaturesBorrowedForAdmin());
    console.log(this.adminPanelDTOs);
  }

  postBookFromBorrowedToAvailable(DTO: SignatureDTO) {
    this.api.returnOfTheBook(DTO.username, DTO.id).subscribe(
      (data) => {},
      (error) => console.log(error)
    );
    setTimeout(() => {
      this.getBorrowedBooksForAdmin();
    }, 100);
  }

  sendMailWithReminder(DTO: SignatureDTO) {
    this.api.reminderOfTheBook(DTO.username, DTO.id).subscribe(
      () => {},
      (error) => console.log(error)
    );
  }

  toggleSortDirectionByUsername(): void {
    this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    this.sortByUser();
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
}
