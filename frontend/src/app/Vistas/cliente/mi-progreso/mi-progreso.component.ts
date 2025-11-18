import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProgresoService } from '../../../servicios/progreso.service';
// Importa NgxChartsModule y el tipo de gráfico
import { NgxChartsModule, ScaleType } from '@swimlane/ngx-charts';

@Component({
  selector: 'app-mi-progreso',
  standalone: true,
  // Añade ReactiveFormsModule y NgxChartsModule
  imports: [CommonModule, ReactiveFormsModule, NgxChartsModule],
  templateUrl: './mi-progreso.component.html',
  styleUrls: ['./mi-progreso.component.scss']
})
export class MiProgresoComponent implements OnInit {
  progresoForm: FormGroup;
  historialProgreso: any[] = []; // Datos para el gráfico
  isLoading = true;
  view: [number, number] = [700, 300]; // Tamaño del gráfico [ancho, alto]

  // Opciones del gráfico
  legend: boolean = true;
  showLabels: boolean = true;
  animations: boolean = true;
  xAxis: boolean = true;
  yAxis: boolean = true;
  showYAxisLabel: boolean = true;
  showXAxisLabel: boolean = true;
  xAxisLabel: string = 'Fecha';
  yAxisLabel: string = 'Peso (kg)';
  timeline: boolean = true;
  colorScheme = {
    name: 'cool', // Nombre del esquema de color
    selectable: true,
    group: ScaleType.Ordinal,
    domain: ['#5AA454', '#E44D25', '#CFC0BB', '#7aa3e5', '#a8385d', '#aae3f5'] // Colores
  };

  constructor(
    private fb: FormBuilder,
    private progresoService: ProgresoService
  ) {
    // Inicializa el formulario para registrar progreso
    this.progresoForm = this.fb.group({
      fecha: [new Date().toISOString().substring(0, 10), [Validators.required]], // Fecha de hoy por defecto
      pesoKg: ['', [Validators.min(0)]],
      grasaCorporalPct: ['', [Validators.min(0), Validators.max(100)]],
      notas: ['']
    });
  }

  ngOnInit(): void {
    this.cargarHistorial();
  }

  cargarHistorial(): void {
    this.isLoading = true;
    this.progresoService.getMiProgreso().subscribe({
      next: (data) => {
        // Transforma los datos para ngx-charts (necesita name y series)
        this.historialProgreso = [
          {
            name: 'Peso (kg)',
            series: data
              .filter(p => p.pesoKg != null) // Filtra registros sin peso
              .map(p => ({
                name: new Date(p.fecha), // Convierte la fecha string a objeto Date
                value: p.pesoKg
              }))
          }
          // Podrías añadir otra serie para el % de grasa aquí
        ];
        this.isLoading = false;
        console.log('Historial de progreso:', this.historialProgreso);
      },
      error: (err) => {
        console.error('Error al cargar el historial', err);
        this.isLoading = false;
      }
    });
  }

  registrarProgreso(): void {
    if (this.progresoForm.invalid) {
      this.progresoForm.markAllAsTouched();
      return;
    }

    const datos = this.progresoForm.value;
    // Filtramos valores nulos o vacíos antes de enviar
    const payload: any = { fecha: datos.fecha };
    if (datos.pesoKg) payload.pesoKg = datos.pesoKg;
    if (datos.grasaCorporalPct) payload.grasaCorporalPct = datos.grasaCorporalPct;
    if (datos.notas) payload.notas = datos.notas;

    this.progresoService.crearProgreso(payload).subscribe({
      next: () => {
        alert('Progreso registrado con éxito');
        this.progresoForm.reset({ fecha: new Date().toISOString().substring(0, 10) }); // Resetea el form
        this.cargarHistorial(); // Recarga el gráfico
      },
      error: (err) => {
        console.error('Error al registrar progreso', err);
        alert('No se pudo registrar el progreso.');
      }
    });
  }

  onSelect(event: any): void {
    console.log('Elemento seleccionado:', event);
    // Aquí podrías añadir lógica si quieres hacer algo cuando se hace clic en un punto del gráfico
  }
  
  onActivate(event: any): void {
    console.log('Elemento activado:', event);
    // Se dispara cuando el ratón pasa por encima de un punto
  }
  
  onDeactivate(event: any): void {
    console.log('Elemento desactivado:', event);
    // Se dispara cuando el ratón sale de un punto
  }

  // Función para formatear las etiquetas del eje X (fechas)
  formatoFechaEjeX(val: any): string {
     if (val instanceof Date) {
       return val.toLocaleDateString(); // Formato local corto (ej: 25/10/2025)
     }
     return val;
  }
}