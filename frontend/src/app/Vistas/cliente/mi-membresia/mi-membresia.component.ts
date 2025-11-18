import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // Necesario para ngIf, ngFor, date pipe, etc.
import { MembresiaService } from '../../../servicios/membresia.service';
import { PagoService } from '../../../servicios/pago.service';

@Component({
  selector: 'app-mi-membresia',
  standalone: true,
  imports: [CommonModule], // Asegúrate de importar CommonModule
  templateUrl: './mi-membresia.component.html',
  styleUrls: ['./mi-membresia.component.scss']
})
export class MiMembresiaComponent implements OnInit {
  membresiaActiva: any = null; // Para guardar la info de la membresía
  historialPagos: any[] = []; // Para guardar la lista de pagos
  isLoadingMembresia = true;
  isLoadingPagos = true;
  errorMembresia: string | null = null; // Para mostrar errores si la membresía no se encuentra

  constructor(
    private membresiaService: MembresiaService,
    private pagoService: PagoService
  ) {}

  ngOnInit(): void {
    this.cargarMembresia();
    this.cargarPagos();
  }

  cargarMembresia(): void {
    this.isLoadingMembresia = true;
    this.errorMembresia = null; // Limpia errores previos
    this.membresiaService.getMiMembresia().subscribe({
      next: (data) => {
        this.membresiaActiva = data;
        this.isLoadingMembresia = false;
        console.log('Membresía activa:', data);
      },
      error: (err) => {
        console.error('Error al cargar la membresía', err);
        // Si el backend devuelve 404 (Not Found), mostramos un mensaje amigable
        if (err.status === 404) {
          this.errorMembresia = 'No tienes una membresía activa en este momento.';
        } else {
          this.errorMembresia = 'Ocurrió un error al cargar tu membresía.';
        }
        this.isLoadingMembresia = false;
      }
    });
  }

  cargarPagos(): void {
    this.isLoadingPagos = true;
    this.pagoService.getMiHistorialPagos().subscribe({
      next: (data) => {
        this.historialPagos = data;
        this.isLoadingPagos = false;
        console.log('Historial de pagos:', data);
      },
      error: (err) => {
        console.error('Error al cargar historial de pagos', err);
        this.isLoadingPagos = false;
      }
    });
  }
}