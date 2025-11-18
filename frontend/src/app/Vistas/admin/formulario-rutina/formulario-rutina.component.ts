import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
// Importa todo lo necesario para Reactive Forms
import { ReactiveFormsModule, FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { RutinaService } from '../../../servicios/rutina.service';

@Component({
  selector: 'app-formulario-rutina',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink], // Importa ReactiveFormsModule
  templateUrl: './formulario-rutina.component.html',
  styleUrls: ['./formulario-rutina.component.scss']
})
export class FormularioRutinaComponent implements OnInit {
  rutinaForm: FormGroup;
  isEditMode = false;
  rutinaId: number | null = null;
  alumnoId: number | null = null; // ID del alumno al que pertenece esta rutina
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute, // Para leer params de la URL
    private router: Router,
    private rutinaService: RutinaService
  ) {
    // Define la estructura principal del formulario
    this.rutinaForm = this.fb.group({
      usuarioId: [null, Validators.required], // ID del alumno
      nombre: ['', Validators.required],
      objetivo: [''],
      // 'ejercicios' es un array de otros FormGroups
      ejercicios: this.fb.array([]) 
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        // --- MODO EDICIÓN ---
        this.isEditMode = true;
        this.rutinaId = +id;
        this.cargarDatosRutina(this.rutinaId);
      } else {
        // --- MODO CREACIÓN ---
        // Obtenemos el ID del alumno desde el queryParam
        this.route.queryParamMap.subscribe(qParams => {
          const alId = qParams.get('alumnoId');
          if (alId) {
            this.alumnoId = +alId;
            // Asigna el ID del alumno al formulario
            this.rutinaForm.patchValue({ usuarioId: this.alumnoId }); 
          } else {
            // Si no hay ID, redirige (no deberíamos estar aquí)
            alert('No se especificó un alumno para esta rutina.');
            this.router.navigate(['/trainer/alumnos']);
          }
        });
      }
    });
  }

  // --- Métodos para manejar el FormArray de Ejercicios ---

  // Devuelve el FormArray de ejercicios
  get ejercicios(): FormArray {
    return this.rutinaForm.get('ejercicios') as FormArray;
  }

  // Crea un nuevo FormGroup para un ejercicio
  nuevoEjercicio(): FormGroup {
    return this.fb.group({
      ejercicio: ['', Validators.required],
      series: [null],
      repeticiones: [null],
      descansoSegundos: [null]
    });
  }

  // Añade un nuevo ejercicio vacío al FormArray
  agregarEjercicio(): void {
    this.ejercicios.push(this.nuevoEjercicio());
  }

  // Elimina un ejercicio del FormArray por su índice
  eliminarEjercicio(index: number): void {
    this.ejercicios.removeAt(index);
  }

  // --- Carga y Envío de Datos ---

  cargarDatosRutina(id: number): void {
    this.isLoading = true;
    this.rutinaService.getRutinaPorId(id).subscribe({
      next: (data) => {
        this.rutinaForm.patchValue({
          usuarioId: data.usuarioId, // Asegúrate que tu DTO devuelva esto
          nombre: data.nombre,
          objetivo: data.objetivo
        });

        // Llena el FormArray con los ejercicios existentes
        data.ejercicios.forEach((ej: any) => {
          this.ejercicios.push(this.fb.group(ej));
        });

        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar la rutina', err);
        this.isLoading = false;
      }
    });
  }

  onSubmit(): void {
    if (this.rutinaForm.invalid) {
      this.rutinaForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    const datosFormulario = this.rutinaForm.value;

    const observable = this.isEditMode 
      ? this.rutinaService.actualizarRutina(this.rutinaId!, datosFormulario)
      : this.rutinaService.crearRutina(datosFormulario);

    observable.subscribe({
      next: () => {
        alert(`Rutina ${this.isEditMode ? 'actualizada' : 'creada'} con éxito.`);
        this.isLoading = false;
        // Navega de vuelta a la lista de alumnos
        this.router.navigate(['/trainer/alumnos']); 
      },
      error: (err) => {
        console.error('Error al guardar la rutina', err);
        alert('Ocurrió un error. Revisa la consola.');
        this.isLoading = false;
      }
    });
  }
}