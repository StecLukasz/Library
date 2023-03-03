import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, firstValueFrom, map, Observable } from 'rxjs';
import { auth } from '../../shared/const/api';
import { LoggedUser } from './model/logged-user';
import { User } from './model/user';
import { UserDto } from './model/user.dto';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  user$ = new BehaviorSubject<User | undefined>(undefined);
  user?: User = undefined;

  constructor(private http: HttpClient, private router: Router) {}

  login() {
    window.location.href = auth.login;
  }

  logout() {
    this.http.post(auth.logout, {}).subscribe(() => {
      this.user$.next(undefined);
      this.user = undefined;
      this.router.navigate(['login']).then();
    });
  }

  me(): Observable<LoggedUser> {
    return this.http.get<LoggedUser>(auth.me);
  }

  getUser(username: string): Observable<User> {
    return this.http.get<UserDto>(`${auth.users}/${username}`).pipe(map((data) => User.createFromDto(data)));
  }

  async autoLogin(): Promise<void> {
    const loggedUser = await firstValueFrom(this.me()).catch(() => {
      this.router.navigate(['login']).then();
    });

    if (!loggedUser) {
      return;
    }

    const user = await firstValueFrom(this.getUser(loggedUser.username));
    this.user = user;
    this.user$.next(user);
  }
}
