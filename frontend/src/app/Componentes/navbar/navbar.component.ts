import { Component, OnInit } from '@angular/core';
import { AutenticacionService } from '../../services/autenticacion.service';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  nombreUsuario: string | null = null;

  // Inyectamos el servicio para usar sus funciones
  constructor(public autenticacionService: AutenticacionService) {}

  ngOnInit(): void {
    // Obtenemos el nombre del usuario al iniciar el componente
    this.nombreUsuario = this.autenticacionService.getNombreUsuario();
  }

  cerrarSesion(): void {
    // Usamos el método logout del servicio de autenticación
    this.autenticacionService.logout();
  }
}