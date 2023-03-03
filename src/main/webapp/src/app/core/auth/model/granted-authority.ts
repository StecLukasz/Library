export class GrantedAuthority {
  static readonly MANAGER = 'manager';
  static readonly DIRECTOR = 'seniorDirector';
  static readonly HR = 'hr';

  /*
  manager, projectManager , employeeAdmin , intranetAdmin, newHrAdmin , librarian,
  englishTeacher, hardware, recruiter, recruiterHr, recruiterApprover, privateCellEnter, privateCellView,
  taskReport, clockReport, scheduledWork, hr, trainingSpecialist, seniorDirector
*/

  authority: string;

  constructor(authority: string) {
    this.authority = authority;
  }
}
