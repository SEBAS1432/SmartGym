import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GamificacionService } from '../../../servicios/gamificacion.service'; // Importa el servicio

@Component({
  selector: 'app-mis-logros',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mis-logros.component.html',
  styleUrls: ['./mis-logros.component.scss']
})
export class MisLogrosComponent implements OnInit {
  totalPuntos: number = 0;
  misLogros: any[] = [];
  isLoadingPuntos = true;
  isLoadingLogros = true;

  constructor(private gamificacionService: GamificacionService) {}

  ngOnInit(): void {
    this.cargarPuntos();
    this.cargarLogros();
  }

  cargarPuntos(): void {
    this.isLoadingPuntos = true;
    this.gamificacionService.getMisPuntos().subscribe({
      next: (data) => {
        this.totalPuntos = data.totalPuntos; // Accede a la propiedad correcta
        this.isLoadingPuntos = false;
        console.log('Total Puntos:', this.totalPuntos);
      },
      error: (err) => {
        console.error('Error al cargar puntos', err);
        this.isLoadingPuntos = false;
      }
    });
  }

  cargarLogros(): void {
    this.isLoadingLogros = true;
    this.gamificacionService.getMisLogros().subscribe({
      next: (data) => {
        this.misLogros = data;
        this.isLoadingLogros = false;
        console.log('Mis Logros:', data);
      },
      error: (err) => {
        console.error('Error al cargar logros', err);
        this.isLoadingLogros = false;
      }
    });
  }
}