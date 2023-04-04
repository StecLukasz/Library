import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { api } from './shared/const/api';
import { AdminSignatureDTO } from './shared/interface/adminSignatureDTO';
import { AppInfo } from './shared/interface/app-info';
import { Book } from './shared/interface/book';
import { BookDTO } from './shared/interface/bookDTO';

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

  getBorrowedBooksForUser(login: string): Observable<Book[]> {
    return this.http.get<Book[]>(api.books.userBooks + `?login=${login}`);
  }

  getBorrowedDate(login: string): Observable<BookDTO[]> {
    return this.http.get<BookDTO[]>(api.books.borrowedDate + `/?login=${login}`);
  }

  getReservedBooksForUser(login: string): Observable<Book[]> {
    return this.http.get<Book[]>(api.books.reservedUserBooks + `?login=${login}`);
  }

  getBooksSearch(text: string): Observable<Book[]> {
    return this.http.get<Book[]>(api.books.url + `/search?title=${text}&genre=${text}&authorLastName=${text}`);
  }

  reserveBookByUser(login: string, title: string): Observable<Object> {
    const data = { login, title };
    console.log(data);

    return this.http.post(api.books.url + `/reserve`, data);
  }

  getSignaturesForAdminPanel(): Observable<AdminSignatureDTO[]> {
    return this.http.get<AdminSignatureDTO[]>(api.books.adminPanelList);
  }

  getBooksSearchForAdmin(text: string): Observable<AdminSignatureDTO[]> {
    return this.http.get<AdminSignatureDTO[]>(api.books.url + `/search?title=${text}`);
  }
  addBookAdmin(book: Book): Observable<Object> {
    const data = { book };
    console.log(data);
    return this.http.post(api.books.addBook, data);
  }
}
