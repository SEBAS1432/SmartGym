import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AutenticacionService } from '../../../services/autenticacion.service';
import { UsuarioService } from '../../../servicios/usuario.service';

@Component({
  selector: 'app-mi-perfil',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule], // Importa ReactiveFormsModule
  templateUrl: './mi-perfil.component.html',
  styleUrls: ['./mi-perfil.component.scss']
})
export class MiPerfilComponent implements OnInit {
  
  perfilForm: FormGroup;
  usuarioId: string | null = null;
  isLoading = true;

  constructor(
    private fb: FormBuilder,
    private authService: AutenticacionService,
    private usuarioService: UsuarioService
  ) {
    // Inicializa el formulario
    this.perfilForm = this.fb.group({
      nombres: ['', Validators.required],
      apellidos: ['', Validators.required],
      correo: [{ value: '', disabled: true }], // El correo no se puede editar
      telefono: ['']
      // Podrías añadir campos para cambiar contraseña aquí
    });
  }

  ngOnInit(): void {
    this.usuarioId = this.authService.getUsuarioId();
    if (this.usuarioId) {
      this.cargarDatosUsuario();
    } else {
      console.error('No se pudo encontrar el ID del usuario');
      this.isLoading = false;
    }
  }

  cargarDatosUsuario(): void {
    this.usuarioService.getUsuario(this.usuarioId!).subscribe({
      next: (data) => {
        // Rellena el formulario con los datos del backend
        this.perfilForm.patchValue({
          nombres: data.nombres,
          apellidos: data.apellidos,
          correo: data.correo,
          telefono: data.telefono
        });
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar el perfil', err);
        this.isLoading = false;
      }
    });
  }

  guardarCambios(): void {
    if (this.perfilForm.invalid || !this.usuarioId) {
      return;
    }
    
    // Obtenemos solo los valores que se pueden editar
    const datosActualizados = this.perfilForm.getRawValue();
    // Quitamos el correo por si acaso, ya que está deshabilitado
    delete datosActualizados.correo; 

    this.usuarioService.actualizarUsuario(this.usuarioId, datosActualizados).subscribe({
      next: (response) => {
        alert('Perfil actualizado con éxito');
        // Opcional: actualizar el nombre en localStorage si cambió
        localStorage.setItem('usuario_nombres', response.nombres);
        localStorage.setItem('usuario_apellidos', response.apellidos);
        // Recargar la página para que la navbar muestre el nuevo nombre
        window.location.reload();
      },
      error: (err) => {
        console.error('Error al actualizar el perfil', err);
        alert('No se pudo actualizar el perfil.');
      }
    });
  }
}