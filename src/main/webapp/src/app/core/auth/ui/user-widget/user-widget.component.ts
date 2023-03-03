import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { TranslateModule } from '@ngx-translate/core';
import { AuthService } from '../../auth.service';
import { User } from '../../model/user';

@UntilDestroy()
@Component({
  selector: 'app-user-widget',
  standalone: true,
  imports: [CommonModule, TranslateModule, MatTooltipModule, MatButtonModule],
  templateUrl: './user-widget.component.html',
  styleUrls: ['./user-widget.component.scss'],
})
export class UserWidgetComponent implements OnInit {
  user?: User;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.user$.pipe(untilDestroyed(this)).subscribe((user) => (this.user = user));
  }

  login(): void {
    this.authService.login();
  }

  logout(): void {
    this.authService.logout();
  }
}
