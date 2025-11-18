import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MembresiaService } from '../../../servicios/membresia.service';
import { PagoService } from '../../../servicios/pago.service';
import { UsuarioService } from '../../../servicios/usuario.service'; // Para la lista de clientes

@Component({
  selector: 'app-gestion-pagos',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule], // Importa ReactiveFormsModule
  templateUrl: './gestion-pagos.component.html',
  styleUrls: ['./gestion-pagos.component.scss']
})
export class GestionPagosComponent implements OnInit {

  membresiaForm: FormGroup;
  pagoForm: FormGroup;
  isLoading = false;

  // Arrays para rellenar los dropdowns
  clientes: any[] = [];
  // Basado en tu BD, podemos hardcodear esto por ahora
  metodosPago: any[] = [
    { id: 1, nombre: 'EFECTIVO' },
    { id: 2, nombre: 'TARJETA' },
    { id: 3, nombre: 'YAPE/PLIN' }
  ];

  constructor(
    private fb: FormBuilder,
    private membresiaService: MembresiaService,
    private pagoService: PagoService,
    private usuarioService: UsuarioService
  ) {
    // --- Formulario para crear Membresía ---
    this.membresiaForm = this.fb.group({
      usuarioId: [null, Validators.required],
      tipo: ['Mensual', Validators.required],
      fechaInicio: [new Date().toISOString().substring(0, 10), Validators.required],
      fechaFin: ['', Validators.required],
      precio: [100.00, [Validators.required, Validators.min(0)]],
      estado: ['ACTIVA', Validators.required]
    });

    // --- Formulario para registrar Pago ---
    this.pagoForm = this.fb.group({
      usuarioId: [null, Validators.required],
      membresiaId: [null], // Es opcional
      metodoPagoId: [null, Validators.required],
      monto: [null, [Validators.required, Validators.min(0)]],
      status: ['COMPLETADO', Validators.required],
      referencia: [''], // Nro de voucher, etc.
      fechaPago: [new Date().toISOString().substring(0, 16), Validators.required] // datetime-local
    });
  }

  ngOnInit(): void {
    this.cargarClientes();
  }

  // Carga la lista de clientes para los dropdowns
  cargarClientes(): void {
    this.isLoading = true;
    this.usuarioService.getClientes().subscribe({
      next: (data) => {
        this.clientes = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar clientes', err);
        this.isLoading = false;
      }
    });
  }

  // --- Métodos de Envío ---

  onAsignarMembresia(): void {
    if (this.membresiaForm.invalid) {
      this.membresiaForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.membresiaService.crearMembresia(this.membresiaForm.value).subscribe({
      next: () => {
        alert('Membresía asignada con éxito');
        this.membresiaForm.reset({
          tipo: 'Mensual',
          fechaInicio: new Date().toISOString().substring(0, 10),
          precio: 100.00,
          estado: 'ACTIVA'
        });
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al asignar membresía', err);
        alert('Error al asignar membresía.');
        this.isLoading = false;
      }
    });
  }

  onRegistrarPago(): void {
    if (this.pagoForm.invalid) {
      this.pagoForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.pagoService.registrarPago(this.pagoForm.value).subscribe({
      next: () => {
        alert('Pago registrado con éxito');
        this.pagoForm.reset({
          status: 'COMPLETADO',
          fechaPago: new Date().toISOString().substring(0, 16)
        });
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al registrar pago', err);
        alert('Error al registrar pago.');
        this.isLoading = false;
      }
    });
  }
}