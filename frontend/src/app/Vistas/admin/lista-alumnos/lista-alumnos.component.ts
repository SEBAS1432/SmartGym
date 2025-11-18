import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../../../servicios/usuario.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-lista-alumnos',
  standalone: true,
  imports: [CommonModule, RouterLink], 
  templateUrl: './lista-alumnos.component.html',
  styleUrls: ['./lista-alumnos.component.scss']
})
export class ListaAlumnosComponent implements OnInit {
  alumnos: any[] = [];
  isLoading = true;

  constructor(private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    this.cargarAlumnos();
  }

  cargarAlumnos(): void {
    this.isLoading = true;
    this.usuarioService.getClientes().subscribe({
      next: (data) => {
        this.alumnos = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar alumnos', err);
        this.isLoading = false;
      }
    });
  }
}