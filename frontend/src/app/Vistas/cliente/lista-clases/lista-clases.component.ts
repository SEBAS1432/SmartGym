// src/app/vistas/cliente/lista-clases/lista-clases.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClaseService } from '../../../servicios/clase.service';
import { ReservaService } from '../../../servicios/reserva.service';

@Component({
  selector: 'app-lista-clases',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lista-clases.component.html',
  styleUrls: ['./lista-clases.component.scss']
})
export class ListaClasesComponent implements OnInit {
  clases: any[] = [];

  constructor(
    private claseService: ClaseService,
    private reservaService: ReservaService
  ) {}

  ngOnInit(): void {
    this.cargarClases();
  }

  cargarClases(): void {
    this.claseService.getClases().subscribe({
      next: (data) => {
        this.clases = data;
        console.log('Clases cargadas:', data);
      },
      error: (err) => console.error('Error al cargar clases', err)
    });
  }

  reservar(claseId: number): void {
    if (!confirm('¿Estás seguro de que deseas reservar esta clase?')) {
      return;
    }
    
    this.reservaService.crearReserva(claseId).subscribe({
      next: (reserva) => {
        alert('¡Reserva creada con éxito!');
        // Opcional: podrías actualizar la disponibilidad de la clase
      },
      error: (err) => {
        console.error('Error al reservar', err);
        alert('Error al crear la reserva. Quizás ya estés inscrito o no hay cupo.');
      }
    });
  }
}