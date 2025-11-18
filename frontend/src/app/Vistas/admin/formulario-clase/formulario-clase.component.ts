import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ClaseService } from '../../../servicios/clase.service';
import { UsuarioService } from '../../../servicios/usuario.service';

@Component({
  selector: 'app-formulario-clase',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './formulario-clase.component.html',
  styleUrls: ['./formulario-clase.component.scss']
})
export class FormularioClaseComponent implements OnInit {
  claseForm: FormGroup;
  isEditMode = false;
  claseId: number | null = null;
  isLoading = false;

  instructores: any[] = [];

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute, // Para leer el :id de la URL
    private router: Router,       // Para navegar de vuelta
    private claseService: ClaseService,
    private usuarioService: UsuarioService
  ) {
    // Define la estructura del formulario y sus validaciones
    this.claseForm = this.fb.group({
      titulo: ['', [Validators.required, Validators.maxLength(100)]],
      descripcion: [''],
      tipo: ['', Validators.maxLength(50)],
      instructorId: [null, Validators.required],
      fechaInicio: ['', Validators.required],
      duracionMinutos: [60, [Validators.required, Validators.min(1)]],
      capacidad: [10, [Validators.required, Validators.min(1)]],
      estado: ['PROGRAMADA', Validators.required]
    });
  }

  ngOnInit(): void {

    this.cargarInstructores();
    // Revisa si la URL tiene un parámetro 'id'
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.claseId = +id; // Convierte el string 'id' a número
        this.cargarDatosClase(this.claseId);
        
      }
      
    });
  }

  cargarInstructores(): void {
    this.usuarioService.getInstructores().subscribe({
      next: (data) => {
        // Filtramos para mostrar solo Admins y Trainers en el dropdown
        this.instructores = data.filter(u => u.rol === 'ADMIN' || u.rol === 'TRAINER');
      },
      error: (err) => {
        console.error('Error al cargar instructores', err);
      }
    });
  }

  cargarDatosClase(id: number): void {
    this.isLoading = true;
    this.claseService.getClasePorId(id).subscribe({
      next: (data) => {
        // Formatea la fecha para el input datetime-local (yyyy-MM-ddThh:mm)
        const fechaFormateada = new Date(data.fechaInicio).toISOString().substring(0, 16);

        // Rellena el formulario con los datos de la clase
        this.claseForm.patchValue({
          ...data, // El '...data' ya mapea 'instructorId' y los demás campos
          fechaInicio: fechaFormateada // Solo sobrescribimos la fecha formateada
          // Ya no necesitamos la línea 'instructorId: data.instructor.id'
        });
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar la clase', err);
        this.isLoading = false;
      }
    });
  }

  onSubmit(): void {
    if (this.claseForm.invalid) {
      this.claseForm.markAllAsTouched(); // Muestra errores si se intenta enviar
      return;
    }

    this.isLoading = true;
    const datosFormulario = { ...this.claseForm.value };
    const fechaLocalString = datosFormulario.fechaInicio;
    const fechaLocal = new Date(fechaLocalString);

    datosFormulario.fechaInicio = fechaLocal.toISOString();

    // Prepara la llamada al servicio
    const observable = this.isEditMode 
      ? this.claseService.actualizarClase(this.claseId!, datosFormulario)
      : this.claseService.crearClase(datosFormulario);

    observable.subscribe({
      next: () => {
        alert(`Clase ${this.isEditMode ? 'actualizada' : 'creada'} con éxito.`);
        this.isLoading = false;
        // Navega de vuelta a la lista de clases
        this.router.navigate(['/trainer/clases']); 
      },
      error: (err) => {
        console.error('Error al guardar la clase', err);
        alert('Ocurrió un error. Revisa la consola.');
        this.isLoading = false;
      }
    });
  }
}