import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AutenticacionService } from '../../services/autenticacion.service';
import { NavbarComponent } from '../../Componentes/navbar/navbar.component';
import { RouterLink, RouterLinkActive} from '@angular/router';
import { MembresiaService } from '../../servicios/membresia.service';
import { GamificacionService } from '../../servicios/gamificacion.service';
import { ProgresoService } from '../../servicios/progreso.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  nombreUsuario: string | null = null;

  membresiaResumen: any = null; // Guardará { tipo, fechaFin }
  puntosResumen: number = 0;
  ultimoProgreso: any = null; // Guardará { fecha, pesoKg }
  isLoadingMembresia = true;
  isLoadingPuntos = true;
  isLoadingProgreso = true;

  estadisticasSemanales = {
    entrenamientos: 0,
    minutosTotal: 0,
    caloriasQuemadas: 0
  };

  // Accesos rápidos con badges (opcional, puedes usarlo en el template)
  accesosRapidos = [
    {
      ruta: '/mis-rutinas',
      icono: '📋',
      titulo: 'Mis Rutinas',
      descripcion: 'Gestiona tus entrenamientos',
      badge: null,
      gradient: 'from-blue-500 to-cyan-500'
    },
    {
      ruta: '/clases',
      icono: '🗓️',
      titulo: 'Ver Clases',
      descripcion: 'Horarios disponibles',
      badge: null, // Puedes agregar lógica para contar clases nuevas
      gradient: 'from-purple-500 to-pink-500'
    },
    {
      ruta: '/mis-reservas',
      icono: '✅',
      titulo: 'Mis Reservas',
      descripcion: 'Próximas clases',
      badge: null, // Puedes agregar lógica para contar reservas
      gradient: 'from-green-500 to-emerald-500'
    },
    {
      ruta: '/mi-perfil',
      icono: '⚙️',
      titulo: 'Mi Perfil',
      descripcion: 'Configuración',
      badge: null,
      gradient: 'from-orange-500 to-red-500'
    }
  ];

  constructor(
    private autenticacionService: AutenticacionService,
    private membresiaService: MembresiaService,
    private gamificacionService: GamificacionService,
    private progresoService: ProgresoService
  ) {}

  ngOnInit(): void {
    this.nombreUsuario = this.autenticacionService.getNombreUsuario();
    this.cargarResumenMembresia();
    this.cargarResumenPuntos();
    this.cargarResumenProgreso();
  }

  cargarResumenMembresia(): void {
    this.isLoadingMembresia = true;
    this.membresiaService.getMiMembresia().subscribe({
      next: (data) => {
        this.membresiaResumen = { tipo: data.tipo, fechaFin: data.fechaFin };
        this.isLoadingMembresia = false;
      },
      error: (err) => { 
        console.error('Error cargando membresía', err);
        this.isLoadingMembresia = false;  
        // No mostramos error aquí, el HTML manejará el caso null
      }
    });
  }

  cargarResumenPuntos(): void {
    this.isLoadingPuntos = true;
    this.gamificacionService.getMisPuntos().subscribe({
      next: (data) => {
        this.puntosResumen = data.totalPuntos;
        this.isLoadingPuntos = false;
      },
      error: (err) => { 
        console.error('Error cargando puntos', err); 
        this.isLoadingPuntos = false; 
      }
    });
  }

  cargarResumenProgreso(): void {
    this.isLoadingProgreso = true;
    this.progresoService.getMiProgreso().subscribe({
      next: (data) => {
        // Obtenemos el último registro (el array ya viene ordenado por fecha ASC)
        if (data && data.length > 0) {
          this.ultimoProgreso = data[data.length - 1]; // El último elemento
        }
        this.isLoadingProgreso = false;
      },
      error: (err) => { 
        console.error('Error cargando progreso', err); 
        this.isLoadingProgreso = false; 
      }
    });
  }

  cerrarSesion(): void {
    this.autenticacionService.logout();
  }
}