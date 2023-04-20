import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { api } from './shared/const/api';
import { AppInfo } from './shared/interface/app-info';
import { Book } from './shared/interface/book';
import { BookDTO } from './shared/interface/bookDTO';
import { SignatureDTO } from './shared/interface/signatureDTO';

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

  getSignaturesForAdminPanel(): Observable<SignatureDTO[]> {
    return this.http.get<SignatureDTO[]>(api.books.adminPanelList);
  }

  getBooksSearchForAdmin(text: string): Observable<SignatureDTO[]> {
    return this.http.get<SignatureDTO[]>(api.books.searchForAdmin + `title=${text}&bookSignature=${text}`);
  }

  addBookAdmin(bookDTO: BookDTO): Observable<BookDTO> {
    return this.http.post<BookDTO>(api.books.addBook, bookDTO);
  }

  editBookAdmin(bookId: number, bookDTO: BookDTO): Observable<Object> {
    console.log(bookId);
    return this.http.patch<Object>(api.books.editBook + `/${bookId}`, bookDTO);
  }

  deleteOneSignature(signatureId: number): Observable<void> {
    return this.http.delete<void>(api.books.deleteSignature);
  }

  getBookForAdmin(bookId: number): Observable<BookDTO> {
    return this.http.get<BookDTO>(api.books.url + `/${bookId}`);
  }

  getSignaturesBorrowedForAdmin(): Observable<SignatureDTO[]> {
    return this.http.get<SignatureDTO[]>(api.books.adminBorrowedPanelList);
  }

  returnOfTheBook(login: string, id: number): Observable<Object> {
    const data = { login, id };
    return this.http.post(api.books.returnBookWithStatusAvailable, data);
  }

  reminderOfTheBook(login: string, id: number): Observable<Object> {
    const data = { login, id };
    return this.http.post(api.books.reminderOfTheBookFromAdmin, data);
  }
}
