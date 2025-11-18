import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReservaService } from '../../../servicios/reserva.service';

@Component({
  selector: 'app-mis-reservas',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mis-reservas.component.html',
  styleUrls: ['./mis-reservas.component.scss']
})
export class MisReservasComponent implements OnInit {
  misReservas: any[] = [];
  isLoading = true;

  constructor(private reservaService: ReservaService) {}

  ngOnInit(): void {
    this.cargarMisReservas();
  }

  cargarMisReservas(): void {
    this.isLoading = true;
    this.reservaService.getMisReservas().subscribe({
      next: (data) => {
        this.misReservas = data;
        this.isLoading = false;
        console.log('Mis reservas:', data);
      },
      error: (err) => {
        console.error('Error al cargar mis reservas', err);
        this.isLoading = false;
      }
    });
  }

  cancelar(reservaId: number): void {
    if (!confirm('¿Estás seguro de que deseas cancelar esta reserva?')) {
      return;
    }

    this.reservaService.cancelarReserva(reservaId).subscribe({
      next: () => {
        alert('Reserva cancelada con éxito.');
        // Volvemos a cargar la lista para que desaparezca la reserva cancelada
        this.cargarMisReservas();
      },
      error: (err) => {
        console.error('Error al cancelar la reserva', err);
        alert('No se pudo cancelar la reserva. Es posible que la clase esté demasiado cerca.');
      }
    });
  }
}