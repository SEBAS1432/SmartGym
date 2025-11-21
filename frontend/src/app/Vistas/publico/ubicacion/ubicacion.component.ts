import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AutenticacionService } from '../../../services/autenticacion.service';

@Component({
  selector: 'app-ubicacion',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './ubicacion.component.html',
  styleUrl: './ubicacion.component.scss'
})
export class UbicacionComponent {
  isMenuOpen = false;
  isUserMenuOpen = false;

  // Inyectamos el servicio para usarlo en la plantilla
  constructor(public autenticacionService: AutenticacionService) {}

  // Método para cerrar sesión
  cerrarSesion(): void {
    this.autenticacionService.logout();
  }

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

  toggleUserMenu(): void {
    this.isUserMenuOpen = !this.isUserMenuOpen;
  }
}