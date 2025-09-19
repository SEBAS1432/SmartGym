import { Routes } from '@angular/router';
import { InicioComponent } from './Auth/inicio/inicio.component';
import { LoginComponent } from './Auth/login/login.component';
import { DashboardComponent } from './Auth/dashboard/dashboard.component';
import { authGuard } from './Core/guards/auth.guard';
import { guestGuard } from './Core/guards/guest.guard';
import { MembresiaComponent } from './Auth/membresia/membresia.component';
import { ContactoComponent } from './Auth/contacto/contacto.component';

export const routes: Routes = [
  { path: 'inicio', component: InicioComponent },

  { path: 'login', component: LoginComponent },

  { path: 'membresia', component: MembresiaComponent },

  {
    path: 'dashboard',
    loadComponent: () =>
      import('./Auth/dashboard/dashboard.component').then(
        (m) => m.DashboardComponent
      ),
  },
  {
    path: 'inicio',
    loadComponent: () =>
      import('./Auth/inicio/inicio.component').then(m => m.InicioComponent),
  },
  { path: 'contacto', component: ContactoComponent },

  { path: '', redirectTo: 'inicio', pathMatch: 'full' },

  { path: '**', redirectTo: 'inicio' }
];
