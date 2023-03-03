import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { BehaviorSubject } from 'rxjs';
import { LS_LANGUAGE } from '../../shared/const/constant';
import { Language } from './model/language';

@Injectable({
  providedIn: 'root',
})
export class LanguageSelectService {
  private browserLang = 'en';
  private currentLang$ = new BehaviorSubject<string>(this.browserLang);

  private _languages: Language[] = [
    { code: 'en', label: 'english' },
    { code: 'pl', label: 'polish' },
  ];

  constructor(public translate: TranslateService) {
    translate.addLangs(this._languages.map((item) => item.code));
    this.setLanguagePreference();
  }

  get languages(): Language[] {
    return this._languages;
  }

  getCurrentLangStream(): BehaviorSubject<string> {
    return this.currentLang$;
  }

  changeLang(lang: string): void {
    this.translate.use(lang);
    localStorage.setItem(LS_LANGUAGE, lang);
    this.currentLang$.next(lang);
  }

  private setLanguagePreference(): void {
    const prefersLang = this.translate.getBrowserLang() ?? 'en';
    const isLocalStorageSet = localStorage.getItem(LS_LANGUAGE) as string;

    let language: string;

    if (isLocalStorageSet === null) {
      language = prefersLang;
    } else {
      language = isLocalStorageSet;
    }

    this.translate.setDefaultLang(language);
    this.translate.use(language);
    this.currentLang$.next(language);
  }
}
