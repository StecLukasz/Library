import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthComponent } from '../../core/auth/auth.component';
import { UserWidgetComponent } from '../../core/auth/ui/user-widget/user-widget.component';
import { LanguageSelectComponent } from '../language-select/language-select.component';

@Component({
  selector: 'app-top-bar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, LanguageSelectComponent, AuthComponent, UserWidgetComponent],
  templateUrl: './top-bar.component.html',
  styleUrls: ['./top-bar.component.scss'],
})
export class TopBarComponent {
  title = 'Books';
}
