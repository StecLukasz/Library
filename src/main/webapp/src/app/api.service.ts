import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { api } from './shared/const/api';
import { AdminSignatureReservedDTO } from './shared/interface/adminSignatureReservedDTO';
import { AppInfo } from './shared/interface/app-info';
import { Book } from './shared/interface/book';
import { BookDTO } from './shared/interface/bookDTO';
import { ReservedSignaturesForUserDTO } from './shared/interface/reservedSignaturesForUserDTO';
import { ResponseGenreDTO } from './shared/interface/responseGenreDTO';
import { SearchDTO } from './shared/interface/searchDTO';
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

  getSignaturesReserved(): Observable<AdminSignatureReservedDTO[]> {
    return this.http.get<AdminSignatureReservedDTO[]>(api.books.reservedAdminSignatures);
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

  getReservedBooksForUser(login: string): Observable<ReservedSignaturesForUserDTO[]> {
    return this.http.get<ReservedSignaturesForUserDTO[]>(api.books.reservedUserBooks + `?login=${login}`);
  }

  getBooksSearch(text: string, login: string, genre: string): Observable<SearchDTO[]> {
    return this.http.get<SearchDTO[]>(
      api.books.searchWithGenreList +
        `title=${text}&genre=${genre}&authorLastName=${text}&authorFirstName=${text}&login=${login}`
    );
  }

  getGenreDTOs(): Observable<ResponseGenreDTO[]> {
    return this.http.get<ResponseGenreDTO[]>(api.books.GenreList);
  }

  getReservedBookByIdAndLogin(id: number, login: string): Observable<Book[]> {
    return this.http.get<Book[]>(api.books.url + `/is-book-reserved-by-user?id=${id}&login=${login}`);
  }

  postReserveBookByUser(login: string, title: string): Observable<Object> {
    const data = { login, title };
    return this.http.post(api.books.reserve, data);
  }

  //TODO URL ENUMS
  postCancelReservedBookByUser(login: string, title: string): Observable<Object> {
    const data = { login, title };
    return this.http.post(api.books.cancel_reserved, data);
  }

  postCancelReservedSignatureByUser(login: string, id: number): Observable<Object> {
    const data = { login, id };

    return this.http.post(api.books.cancel_signature_reservation, data);
  }

  postReadyReservedSignatureByUser(login: string, id: number): Observable<Object> {
    const data = { login, id };
    return this.http.post(api.books.ready_signature_reservation, data);
  }

  postBorrowReservedSignatureByUser(login: string, id: number): Observable<Object> {
    const data = { login, id };

    return this.http.post(api.books.borrow_signature, data);
  }

  //TODO remove after tests
  scheduler(): Observable<Object> {
    return this.http.get(api.books.url + `/scheduler`);
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
    return this.http.delete<void>(api.books.deleteSignature + `/${signatureId}`);
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
