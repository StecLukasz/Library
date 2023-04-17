import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminBorrowedListComponent } from './admin-borrowed-list.component';

describe('AdminBorrowedListComponent', () => {
  let component: AdminBorrowedListComponent;
  let fixture: ComponentFixture<AdminBorrowedListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ AdminBorrowedListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminBorrowedListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
