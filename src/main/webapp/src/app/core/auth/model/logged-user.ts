import { Domains } from './domains';
import { GrantedAuthority } from './granted-authority';

export class LoggedUser {
  firstName: string;
  lastName: string;
  username: string;
  domain?: string;
  domainEnum?: Domains;
  authorities?: GrantedAuthority[];

  constructor(loggedUser: LoggedUser) {
    this.firstName = loggedUser.firstName;
    this.lastName = loggedUser.lastName;
    this.username = loggedUser.username;
    this.domain = loggedUser.domain;
    this.domainEnum = loggedUser.domainEnum;
    this.authorities = loggedUser.authorities;
  }
}
