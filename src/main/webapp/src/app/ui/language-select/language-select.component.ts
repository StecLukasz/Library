import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { TranslateModule } from '@ngx-translate/core';
import { BehaviorSubject } from 'rxjs';
import { LanguageSelectService } from './language-select.service';
import { Language } from './model/language';

@Component({
  standalone: true,
  imports: [CommonModule, MatTooltipModule, MatMenuModule, TranslateModule, MatButtonModule],
  selector: 'app-language-select',
  templateUrl: './language-select.component.html',
  styleUrls: ['./language-select.component.scss'],
})
export class LanguageSelectComponent {
  languages: Language[];
  currentLanguage$: BehaviorSubject<string>;

  constructor(private service: LanguageSelectService) {
    this.languages = service.languages;
    this.currentLanguage$ = this.service.getCurrentLangStream();
  }

  changeLang(lang: string): void {
    this.service.changeLang(lang);
  }
}
