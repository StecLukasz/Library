import { EMPLOYEE_PHOTO } from '../../../shared/const/constant';
import { RoleType } from '../enum/role-type';
import { LoggedUser } from './logged-user';
import { UserDto } from './user.dto';

export class User {
  id?: number;
  username: string;
  firstName: string;
  firstNamePl: string;
  lastName: string;
  lastNamePl: string;
  email: string;
  department: string;
  employmentForm?: number;
  role: RoleType;
  photo?: string;
  managerId?: number;

  constructor(
    username: string,
    firstName: string,
    firstNamePl: string,
    lastName: string,
    lastNamePl: string,
    email: string = '',
    department: string = '',
    employmentForm?: number,
    role: RoleType = RoleType.USER,
    id?: number,
    managerId?: number
  ) {
    this.username = username;
    this.firstName = firstName;
    this.firstNamePl = firstNamePl;
    this.lastName = lastName;
    this.lastNamePl = lastNamePl;
    this.email = email;
    this.department = department;
    this.employmentForm = employmentForm;
    this.role = role;
    this.photo = `${EMPLOYEE_PHOTO}/${username}`;
    this.id = id;
    this.managerId = managerId;
  }

  static createFromLoggedUser(data: LoggedUser) {
    const { username, firstName, lastName, domain } = data;
    const user = new User(username, firstName, firstName, lastName, lastName);
    user.email = `${username}@${domain}`;

    return user;
  }

  static createFromDto(data: UserDto) {
    const {
      id,
      username,
      firstName,
      firstNamePl,
      lastName,
      lastNamePl,
      email,
      department,
      employmentForm,
      role: roleStr,
      managerId,
    } = data;
    // @ts-ignore
    const role = RoleType[roleStr];

    return new User(
      username,
      firstName,
      firstNamePl,
      lastName,
      lastNamePl,
      email,
      department,
      employmentForm,
      role,
      id,
      managerId
    );
  }

  isAllowChangeUser(): boolean {
    const { role } = this;
    return role !== RoleType.USER;
  }

  isHr(): boolean {
    return this.role === RoleType.HR;
  }

  isManager(): boolean {
    return this.role === RoleType.MANAGER;
  }

  isManagerOrDirector(): boolean {
    return this.role === RoleType.MANAGER || this.role === RoleType.DIRECTOR;
  }

  isDirector(): boolean {
    return this.role === RoleType.DIRECTOR;
  }

  isDirectorOrHr(): boolean {
    return this.role === RoleType.HR || this.role === RoleType.DIRECTOR;
  }

  isCanManageRequests(): boolean {
    return this.role === RoleType.MANAGER || this.role === RoleType.HR || this.role === RoleType.DIRECTOR;
  }
}
