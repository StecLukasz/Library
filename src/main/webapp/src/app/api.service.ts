import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { api } from './shared/const/api';
import { AppInfo } from './shared/interface/app-info';
import { Book } from './shared/interface/book';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  appInfo?: AppInfo;

  constructor(private http: HttpClient) {}

  getAppInfo(): Observable<AppInfo> {
    return this.http.get<AppInfo>(api.app.url).pipe(
      tap((data) => {
        this.appInfo = data;
      })
    );
  }

  getBooks(): Observable<Book[]> {
    return this.http.get<Book[]>(api.books.url);
  }

  getBooksForUser(login: string): Observable<Book[]> {
    const params = {
      login,
    };

    return this.http.get<Book[]>(api.books.userBooks, { params });
  }

  getReservedBooksForUser(login: string): Observable<Book[]> {
    return this.http.get<Book[]>(api.books.reservedUserBooks + `?login=${login}`);
  }

  getBooksSearch(text: string): Observable<Book[]> {
    return this.http.get<Book[]>(
      api.books.url + `/search?title=${text}&genre=${text}&authorLastName=${text}&authorFirstName=${text}`
    );
  }

  postReserveBookByUser(login: string, title: string): Observable<Object> {
    const data = { login, title };
    console.log(data);

    return this.http.post(api.books.url + `/reserve`, data);
  }
  //TODO URL ENUMS
  postCancelReservedBookByUser(login: string, title: string): Observable<Object> {
    const data = { login, title };
    console.log(data);

    return this.http.post(api.books.url + `/cancelReserved`, data);
  }
}
