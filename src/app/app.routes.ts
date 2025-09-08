import { Routes } from '@angular/router';
import { InicioComponent } from './Auth/inicio/inicio.component';
import { LoginComponent } from './Auth/login/login.component';
import { DashboardComponent } from './User/dashboard/dashboard.component';
import { authGuard } from './Core/guards/auth.guard';
import { guestGuard } from './Core/guards/guest.guard';

export const routes: Routes = [
  { path: 'inicio', component: InicioComponent, canActivate: [guestGuard] },

  { path: 'login', component: LoginComponent, canActivate: [guestGuard] },

  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },

  { path: '', redirectTo: 'inicio', pathMatch: 'full' },

  { path: '**', redirectTo: 'inicio' }
];
