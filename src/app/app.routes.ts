import { Routes } from '@angular/router';
import { InicioComponent } from './Auth/inicio/inicio.component';
import { LoginComponent } from './Auth/login/login.component';
import { DashboardComponent } from './Auth/dashboard/dashboard.component';
import { MembresiaComponent } from './Auth/membresia/membresia.component';
import { ContactoComponent } from './Auth/contacto/contacto.component';
import { RegistroComponent } from './Auth/registro/registro.component';
import { ServiciosComponent } from './Auth/servicios/servicios.component';
import { authGuard } from './Core/guards/auth.guard';
import { guestGuard } from './Core/guards/guest.guard';

export const routes: Routes = [
  { path: 'inicio', component: InicioComponent },

  { path: 'registro', component: RegistroComponent },

  { path: 'login', component: LoginComponent },

  { path: 'membresia', component: MembresiaComponent },

  { path: 'contacto', component: ContactoComponent },

  { path: 'servicios', component: ServiciosComponent },

  {
    path: 'dashboard',
    loadComponent: () =>
      import('./Auth/dashboard/dashboard.component').then(
        (m) => m.DashboardComponent
      ),
  },

  { path: '', redirectTo: 'inicio', pathMatch: 'full' },

  { path: '**', redirectTo: 'inicio' },
];
