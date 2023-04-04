import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { api } from './shared/const/api';
import { AdminSignatureReservedDTO } from './shared/interface/AdminSignatureReservedDTO';
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

  getSignaturesReserved(): Observable<AdminSignatureReservedDTO[]> {
    return this.http.get<AdminSignatureReservedDTO[]>(api.books.reservedAdminSignatures);
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

  getBooksSearch(text: string, login: string): Observable<Book[]> {
    return this.http.get<Book[]>(
      api.books.url +
        `/search?title=${text}&genre=${text}&authorLastName=${text}&authorFirstName=${text}&login=${login}`
    );
  }
  getReservedBookByIdAndLogin(id: number, login: string): Observable<Book[]> {
    return this.http.get<Book[]>(api.books.url + `/is-book-reserved-by-user?id=${id}&login=${login}`);
  }

  postReserveBookByUser(login: string, title: string): Observable<Object> {
    const data = { login, title };
    return this.http.post(api.books.url + `/reserve`, data);
  }

  //TODO URL ENUMS
  postCancelReservedBookByUser(login: string, title: string): Observable<Object> {
    const data = { login, title };
    return this.http.post(api.books.url + `/cancelReserved`, data);
  }

  postCancelReservedSignatureByUser(login: string, id: number): Observable<Object> {
    const data = { login, id };

    return this.http.post(api.books.url + `/cancelSignatureReservation`, data);
  }

  postReadyReservedSignatureByUser(login: string, id: number): Observable<Object> {
    const data = { login, id };
    return this.http.post(api.books.url + `/readySignatureReservation`, data);
  }

  postBorrowReservedSignatureByUser(login: string, id: number): Observable<Object> {
    const data = { login, id };

    return this.http.post(api.books.url + `/borrowSignatureReservation`, data);
  }
}
