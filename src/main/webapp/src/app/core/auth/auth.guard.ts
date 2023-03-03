import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { catchError, map, Observable, take } from 'rxjs';
import { AuthService } from './auth.service';
import { LoggedUser } from './model/logged-user';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  private ROUTING_DATA_ROLE_PROPERTY = 'roles';

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const currentUser = this.authService.user;

    if (currentUser) {
      if (this.hasAccessRoles(route, currentUser)) {
        return true;
      }

      return this.router.createUrlTree(['/list']);
    }

    return this.authService.me().pipe(
      take(1),
      map((user) => {
        const isUser = !!user;
        if (isUser && this.hasAccessRoles(route, user)) {
          return true;
        }

        if (!this.hasAccessRoles(route, user)) {
          return this.router.createUrlTree(['/list']);
        }

        return this.router.createUrlTree(['/login']);
      }),
      catchError(this.handleError)
    );
  }

  private hasAccessRoles(next: ActivatedRouteSnapshot, user: LoggedUser): boolean {
    const roles = next.data[this.ROUTING_DATA_ROLE_PROPERTY] as string[];

    if (!roles || !roles.length) {
      return true;
    }

    const userAuthorities = user?.authorities?.map((item) => item.authority) ?? [];

    return userAuthorities.some((item) => roles.indexOf(item) >= 0);
  }

  private handleError(errorResponse: HttpErrorResponse): Observable<any> {
    let msg = 'Unknown error';

    switch (errorResponse.error) {
      case 'Unauthorized':
        msg = 'Unauthorized';
        break;
    }

    console.warn('Unauthorized');

    return new Observable<any>();
  }
}
