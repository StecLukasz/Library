import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { ApiService } from '../../api.service';
import { AuthComponent } from '../../core/auth/auth.component';
import { AuthService } from '../../core/auth/auth.service';
import { User } from '../../core/auth/model/user';
import { UserWidgetComponent } from '../../core/auth/ui/user-widget/user-widget.component';
import { LanguageSelectComponent } from '../language-select/language-select.component';

@UntilDestroy()
@Component({
  selector: 'app-top-bar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, LanguageSelectComponent, AuthComponent, UserWidgetComponent],
  templateUrl: './top-bar.component.html',
  styleUrls: ['./top-bar.component.scss'],
})
export class TopBarComponent {
  title = 'Books';
  currentUserLogin: string = '';
  user?: User;

  constructor(private api: ApiService, private authService: AuthService) {}

  async ngOnInit(): Promise<void> {
    // stream obserwujacy aktualnie zalogowanego użytkownika
    this.authService.user$.pipe(untilDestroyed(this)).subscribe((user) => {
      if (!user) {
        console.log('anonymous' + user);
        // gdy użytkownik niezalogowany
        //this.getBooks();
        return;
      }
      this.user = user;
      console.log(this.user);
      this.currentUserLogin = user.username;

      console.log('zalogowany user' + user.username);
      // zalogowany - user nie jest undefined
      //this.getReservedBooksForUser(this.currentUser);
    });
  }

  runScheduler() {
    this.api.scheduler().subscribe(
      (data) => {},
      (error) => console.log(error)
    );
    console.log('schedulerRun');
  }
}
