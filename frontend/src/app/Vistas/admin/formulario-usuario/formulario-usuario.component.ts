import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { UsuarioService } from '../../../servicios/usuario.service';
import { MembresiaService } from '../../../servicios/membresia.service';
import { PagoService } from '../../../servicios/pago.service';

@Component({
  selector: 'app-formulario-usuario',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink], 
  templateUrl: './formulario-usuario.component.html',
  styleUrls: ['./formulario-usuario.component.scss']
})
export class FormularioUsuarioComponent implements OnInit {

  usuarioForm: FormGroup;
  
  isEditMode = false;
  usuarioId: number | null = null;
  isLoading = false; // Cambiado a false por defecto

  historialMembresias: any[] = [];
  historialPagos: any[] = [];

constructor(
      private fb: FormBuilder,
      private route: ActivatedRoute,
      private router: Router,
      private usuarioService: UsuarioService,
      private membresiaService: MembresiaService,
      private pagoService: PagoService
    ) {
      // Define el formulario con todas las validaciones
      this.usuarioForm = this.fb.group({
        nombres: ['', Validators.required],
        apellidos: ['', Validators.required],
        correo: ['', [Validators.required, Validators.email]],
        telefono: [''],
        contrasena: ['', [Validators.required, Validators.minLength(6)]], 
        rol: ['CLIENTE', Validators.required], 
        estado: ['ACTIVO', Validators.required]
      });

}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        // --- MODO EDICIÓN ---
        this.isEditMode = true;
        this.usuarioId = +id;
        
        // Ajusta validadores (como ya lo tenías)
        this.usuarioForm.get('contrasena')?.clearValidators();
        this.usuarioForm.get('contrasena')?.updateValueAndValidity();
        
        // Carga todos los datos de este usuario
        this.cargarDatosUsuario(this.usuarioId);
        this.cargarHistorialMembresias(this.usuarioId);
        this.cargarHistorialPagos(this.usuarioId);

      } else {
        // --- MODO CREACIÓN ---
        this.isLoading = false; 
        // No hay historiales que cargar, solo se muestra el formulario de creación
      }
    });
  }

  

  cargarDatosUsuario(id: number): void {
    this.isLoading = true;
    this.usuarioService.getUsuario(id.toString()).subscribe({
      next: (data) => {
        // Rellena el formulario con los datos del usuario
        // Omitimos la contraseña, se queda en blanco
        this.usuarioForm.patchValue({
          nombres: data.nombres,
          apellidos: data.apellidos,
          correo: data.correo,
          telefono: data.telefono,
          rol: data.rol,
          estado: data.estado
        });
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar usuario', err);
        this.isLoading = false;
        alert('No se pudo cargar el usuario.');
      }
    });
  }

  cargarHistorialMembresias(id: number): void {
    this.membresiaService.getMembresiasPorUsuario(id).subscribe({
      next: (data) => {
        this.historialMembresias = data;
        console.log('Membresías cargadas:', data);
      },
      error: (err) => console.error('Error al cargar membresías', err)
    });
  }

  cargarHistorialPagos(id: number): void {
    this.pagoService.getPagosPorUsuario(id).subscribe({
      next: (data) => {
        this.historialPagos = data;
        console.log('Pagos cargados:', data);
      },
      error: (err) => console.error('Error al cargar pagos', err)
    });
  }

  onSubmitUsuario(): void {
    if (this.usuarioForm.invalid) {
      this.usuarioForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    const datosFormulario = this.usuarioForm.getRawValue(); // getRawValue() incluye campos deshabilitados (como el correo si lo estuviera)

    // Lógica de Contraseña: Si es modo edición y el campo contraseña está vacío,
    // no la enviamos para que el backend no la actualice.
    if (this.isEditMode && !datosFormulario.contrasena) {
      delete datosFormulario.contrasena;
    }

    // Determina si crear o actualizar
    const observable = this.isEditMode 
      ? this.usuarioService.actualizarUsuario(this.usuarioId!.toString(), datosFormulario)
      : this.usuarioService.crearUsuario(datosFormulario);

    observable.subscribe({
      next: () => {
        alert(`Usuario ${this.isEditMode ? 'actualizado' : 'creado'} con éxito.`);
        this.isLoading = false;
        this.router.navigate(['/admin/usuarios']); // Vuelve a la lista
      },
      error: (err) => {
        console.error('Error al guardar usuario', err);
        // Muestra el mensaje de error del backend (ej: "El correo ya existe")
        alert(`Error: ${err.error?.message || 'No se pudo guardar el usuario'}`);
        this.isLoading = false;
      }
    });
  }
}
