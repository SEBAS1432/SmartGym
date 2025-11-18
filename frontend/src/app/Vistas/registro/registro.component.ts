import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AutenticacionService } from '../../services/autenticacion.service';

@Component({
  selector: 'app-registro',
  standalone: true,
  // Importamos ReactiveFormsModule para los formularios y RouterLink para la navegación
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './registro.component.html',
  styleUrl: './registro.component.scss'
})
export class RegistroComponent {
  registroForm: FormGroup;

  // Inyectamos FormBuilder para crear el formulario, el servicio y el router
  constructor(
    private fb: FormBuilder,
    private autenticacionService: AutenticacionService,
    private router: Router
  ) {
    // Definimos la estructura del formulario y sus validaciones
    this.registroForm = this.fb.group({
      nombres: ['', [Validators.required]], // Cambiado de 'nombre' a 'nombres'
      apellidos: ['', [Validators.required]], // Campo nuevo
      correo: ['', [Validators.required, Validators.email]],
      telefono: ['', [Validators.required, Validators.pattern(/^[\+]?[0-9\s\-\(\)]+$/)]],
      contrasena: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  // Método que se ejecuta al enviar el formulario
  onSubmit(): void {
    // Si el formulario no es válido, no hacemos nada
    if (this.registroForm.invalid) {
      this.registroForm.markAllAsTouched(); // Marca todos los campos como "tocados" para mostrar errores
      return;
    }

    // Llamamos al servicio de registro con los datos del formulario
    this.autenticacionService.registro(this.registroForm.value).subscribe({
      next: (response) => {
        console.log('Registro exitoso:', response);
        // Mostramos una alerta al usuario y lo redirigimos al login
        alert('¡Usuario registrado con éxito! Ahora puedes iniciar sesión.');
        this.router.navigate(['/login']);
      },
      error: (error) => {
        console.error('Error en el registro:', error);
        // Mostramos un mensaje de error genérico
        alert('Ocurrió un error durante el registro. Por favor, intenta de nuevo.');
      }
    });
  }
}