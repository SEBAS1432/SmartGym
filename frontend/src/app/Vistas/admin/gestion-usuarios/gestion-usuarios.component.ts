import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router'; 
import { UsuarioService } from '../../../servicios/usuario.service';

@Component({
  selector: 'app-gestion-usuarios',
  standalone: true,
  imports: [CommonModule, RouterLink], 
  templateUrl: './gestion-usuarios.component.html',
  styleUrls: ['./gestion-usuarios.component.scss']
})
export class GestionUsuariosComponent implements OnInit {
  usuarios: any[] = [];
  isLoading = true;

  constructor(private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    this.isLoading = true;
    this.usuarioService.getAllUsuarios().subscribe({
      next: (data) => {
        this.usuarios = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar usuarios', err);
        this.isLoading = false;
      }
    });
  }

  desactivar(id: number): void {
    if (!confirm('¿Estás seguro de que deseas DESACTIVAR este usuario?')) {
      return;
    }

    this.usuarioService.eliminarUsuario(id).subscribe({ // Sigue llamando al servicio 'eliminarUsuario'
      next: () => {
        alert('Usuario desactivado con éxito');
        this.cargarUsuarios(); // Recarga la lista
      },
      error: (err) => {
        console.error('Error al desactivar usuario', err);
        alert('Error al desactivar el usuario.');
      }
    });
  }

  // --- AÑADE ESTE NUEVO MÉTODO ---
  activar(id: number): void {
    if (!confirm('¿Estás seguro de que deseas ACTIVAR este usuario?')) {
      return;
    }

    this.usuarioService.activarUsuario(id).subscribe({
      next: () => {
        alert('Usuario activado con éxito');
        this.cargarUsuarios(); // Recarga la lista
      },
      error: (err) => {
        console.error('Error al activar usuario', err);
        alert('Error al activar el usuario.');
      }
    });
}
}