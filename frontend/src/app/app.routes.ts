import { Routes } from '@angular/router';
import { InicioComponent } from './Vistas/inicio/inicio.component';
import { LoginComponent } from './Vistas/login/login.component';
import { RegistroComponent } from './Vistas/registro/registro.component';
import { DashboardComponent } from './Vistas/dashboard/dashboard.component';
import { LayoutPrincipalComponent } from './Vistas/layout-principal/layout-principal.component';
import { ListaClasesComponent } from './Vistas/cliente/lista-clases/lista-clases.component';
import { MisReservasComponent } from './Vistas/cliente/mis-reservas/mis-reservas.component';
import { MiPerfilComponent } from './Vistas/cliente/mi-perfil/mi-perfil.component';
import { MisRutinasComponent } from './Vistas/cliente/mis-rutinas/mis-rutinas.component';
import { MiProgresoComponent } from './Vistas/cliente/mi-progreso/mi-progreso.component';
import { MiMembresiaComponent } from './Vistas/cliente/mi-membresia/mi-membresia.component';
import { MisLogrosComponent } from './Vistas/cliente/mis-logros/mis-logros.component';
import { AdminLayoutComponent } from './Vistas/admin/admin-layout/admin-layout.component';
import { authGuard } from './seguridad/auth.guard';
import { roleGuard } from './seguridad/role.guard';
import { DashboardTrainerComponent } from './Vistas/admin/dashboard-trainer/dashboard-trainer.component';
import { GestionClasesComponent } from './Vistas/admin/gestion-clases/gestion-clases.component';
import { FormularioClaseComponent } from './Vistas/admin/formulario-clase/formulario-clase.component';
import { VerAsistentesComponent } from './Vistas/admin/ver-asistentes/ver-asistentes.component';
import { ListaAlumnosComponent } from './Vistas/admin/lista-alumnos/lista-alumnos.component';
import { GestionRutinasAlumnoComponent } from './Vistas/admin/gestion-rutinas-alumno/gestion-rutinas-alumno.component';
import { FormularioRutinaComponent } from './Vistas/admin/formulario-rutina/formulario-rutina.component';
import { GestionUsuariosComponent } from './Vistas/admin/gestion-usuarios/gestion-usuarios.component';
import { FormularioUsuarioComponent } from './Vistas/admin/formulario-usuario/formulario-usuario.component';
import { GestionPagosComponent } from './Vistas/admin/gestion-pagos/gestion-pagos.component';
import { PlanesComponent } from './Vistas/publico/planes/planes.component';
import { UbicacionComponent } from './Vistas/publico/ubicacion/ubicacion.component';
import { ReporteSemanalComponent } from './Vistas/admin/reporte-semanal/reporte-semanal.component';
import { homeGuard } from './seguridad/home.guard';



export const routes: Routes = [
    { path: '', component: InicioComponent, canActivate: [homeGuard] },
    { path: 'login', component: LoginComponent },
    { path: 'registro', component: RegistroComponent },
    { path: 'planes', component: PlanesComponent },
    { path: 'ubicacion', component: UbicacionComponent },
    {
        path: '',
        component: LayoutPrincipalComponent,
        canActivate: [authGuard, roleGuard],
        data: {
          roles: ['CLIENTE'] 
        },
        children: [
          { path: 'dashboard', component: DashboardComponent },
          { path: 'mi-perfil', component: MiPerfilComponent },
          { path: 'clases', component: ListaClasesComponent },
          { path: 'mis-reservas', component: MisReservasComponent },
          { path: 'mis-rutinas', component: MisRutinasComponent },
          { path: 'mi-progreso', component: MiProgresoComponent },
          { path: 'mi-membresia', component: MiMembresiaComponent },
          { path: 'mis-logros', component: MisLogrosComponent },
        ]
      },
      {
        path: 'trainer',
        component: AdminLayoutComponent,
        canActivate: [authGuard, roleGuard],
        data: {
          roles: ['TRAINER', 'ADMIN'] 
        },
        children: [
          { path: 'dashboard', component: DashboardTrainerComponent },
          { path: 'clases', component: GestionClasesComponent },
          { path: 'clases/nuevo', component: FormularioClaseComponent },
          { path: 'clases/editar/:id', component: FormularioClaseComponent },
          { path: 'clases/:id/asistentes', component: VerAsistentesComponent },
          { path: 'alumnos', component: ListaAlumnosComponent },
          { path: 'alumnos/:id/rutinas', component: GestionRutinasAlumnoComponent },
          { path: 'rutinas/nuevo', component: FormularioRutinaComponent },
          { path: 'rutinas/editar/:id', component: FormularioRutinaComponent },
          
          
          { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
        ]
      },
      {
        path: 'admin',
        component: AdminLayoutComponent, 
        canActivate: [authGuard, roleGuard], 
        data: { 
          roles: ['ADMIN']
        },
        children: [
          { path: 'usuarios', component: GestionUsuariosComponent },
          { path: 'usuarios/nuevo', component: FormularioUsuarioComponent },
          { path: 'usuarios/editar/:id', component: FormularioUsuarioComponent },
          { path: 'pagos', component: GestionPagosComponent },
          { path: 'reportes', component: ReporteSemanalComponent },

          { path: '', redirectTo: 'usuarios', pathMatch: 'full' }
        ]
      },

    { path: '**', redirectTo: '', pathMatch: 'full' } 
];