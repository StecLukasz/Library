import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './core/auth/auth.guard';
import { AboutComponent } from './page/about/about.component';

import { AdminReservedComponent } from './page/admin-reserved/admin-reserved.component';
import { AdminBorrowedListComponent } from './page/admin-borrowed-list/admin-borrowed-list.component';
import { AddBookModalComponent } from './page/admin-panel/add-book-modal/add-book-modal.component';
import { AdminPanelComponent } from './page/admin-panel/admin-panel.component';
import { EditBookComponent } from './page/admin-panel/edit-book/edit-book.component';
import { BorrowedListComponent } from './page/borrowed-list/borrowed-list.component';
import { ListComponent } from './page/list/list.component';
import { LoginComponent } from './page/login/login.component';
import { ReservedComponent } from './page/reserved/reserved.component';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'list' },
  {
    path: 'list',
    component: ListComponent,
  },
  {
    path: 'reserved',
    component: ReservedComponent,
  },
  {
    path: 'admin-reserved',
    component: AdminReservedComponent,
  },
  {
    path: 'borrowed-date',
    component: BorrowedListComponent,
  },
  {
    path: 'admin-panel',
    component: AdminPanelComponent,
  },
  {
    path: 'add-book',
    component: AddBookModalComponent,
  },
  {
    path: 'edit-book/:bookId',
    component: EditBookComponent,
  },
  {
    path: 'admin-borrowed-panel',
    component: AdminBorrowedListComponent,
  },
  {
    path: 'about',
    canActivate: [AuthGuard],
    component: AboutComponent,
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  { path: '**', redirectTo: 'list' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
