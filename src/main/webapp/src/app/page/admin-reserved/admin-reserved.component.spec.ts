import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminReservedComponent } from './admin-reserved.component';

describe('AdminReservedComponent', () => {
  let component: AdminReservedComponent;
  let fixture: ComponentFixture<AdminReservedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ AdminReservedComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminReservedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
