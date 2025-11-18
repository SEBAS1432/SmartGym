import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router'; // Para los botones de Editar/Crear
import { ClaseService } from '../../../servicios/clase.service';

@Component({
  selector: 'app-gestion-clases',
  standalone: true,
  imports: [CommonModule, RouterLink], // Importa RouterLink
  templateUrl: './gestion-clases.component.html',
  styleUrls: ['./gestion-clases.component.scss']
})
export class GestionClasesComponent implements OnInit {
  clases: any[] = [];
  isLoading = true;

  constructor(private claseService: ClaseService) {}

  ngOnInit(): void {
    this.cargarClases();
  }

  cargarClases(): void {
    this.isLoading = true;
    this.claseService.getClases().subscribe({
      next: (data) => {
        this.clases = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar clases', err);
        this.isLoading = false;
      }
    });
  }

  eliminar(id: number): void {
    if (!confirm('¿Estás seguro de que deseas eliminar esta clase? Esta acción no se puede deshacer.')) {
      return;
    }

    this.claseService.eliminarClase(id).subscribe({
      next: () => {
        alert('Clase eliminada con éxito');
        // Recarga la lista para quitar la clase eliminada
        this.cargarClases(); 
      },
      error: (err) => {
        console.error('Error al eliminar clase', err);
        alert('Error al eliminar la clase. Es posible que tenga reservas activas.');
      }
    });
  }
}