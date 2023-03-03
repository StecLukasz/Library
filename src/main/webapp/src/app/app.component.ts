import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AuthService } from './core/auth/auth.service';
import { FooterComponent } from './ui/footer/footer.component';
import { TopBarComponent } from './ui/top-bar/top-bar.component';

@Component({
  standalone: true,
  imports: [CommonModule, TopBarComponent, FooterComponent, RouterOutlet],
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'Books';

  constructor(private authService: AuthService) {}

  async ngOnInit(): Promise<void> {
    this.authService.autoLogin().then();
  }
}
