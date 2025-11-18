import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; // Necesario para ngIf y ngFor
import { RouterLink } from '@angular/router';
import { AutenticacionService } from '../../services/autenticacion.service';

@Component({
  selector: 'app-inicio',
  standalone: true,
  // Asegúrate de importar CommonModule y RouterLink
  imports: [CommonModule, RouterLink], 
  templateUrl: './inicio.component.html',
  styleUrl: './inicio.component.scss'
})
export class InicioComponent {

  isMenuOpen = false;

  beneficios = [
    {
      icon: '🏋️',
      titulo: 'Equipos Modernos',
      descripcion: 'Máquinas de última generación para todos tus entrenamientos.',
      gradient: 'from-yellow-400 to-orange-500'
    },
    {
      icon: '🤝',
      titulo: 'Ambiente Amigable',
      descripcion: 'Un espacio inclusivo donde todos se sienten bienvenidos.',
      gradient: 'from-orange-400 to-red-500'
    },
    {
      icon: '⏰',
      titulo: 'Acceso 24/7',
      descripcion: 'Entrena cuando quieras, adaptado a tu horario.',
      gradient: 'from-yellow-500 to-amber-600'
    }
  ];

  estadisticas = [
    { valor: '1000+', label: 'Miembros Activos', icon: '👥' },
    { valor: '50+', label: 'Clases Semanales', icon: '📅' },
    { valor: '24/7', label: 'Acceso Total', icon: '🔓' },
    { valor: '100%', label: 'Satisfacción', icon: '⭐' }
  ];

  // Inyectamos el servicio de autenticación para usar sus métodos
  constructor(public autenticacionService: AutenticacionService) {}

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  // Este método llamará al logout del servicio
  cerrarSesion(): void {
    this.autenticacionService.logout();
    alert('Has cerrado sesión correctamente.');
  }
}