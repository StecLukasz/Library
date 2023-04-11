import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './core/auth/auth.guard';
import { AboutComponent } from './page/about/about.component';

import { AdminReservedComponent } from './page/admin-reserved/admin-reserved.component';
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
