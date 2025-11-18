import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AutenticacionService } from '../../services/autenticacion.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMensaje: string | null = null; // Para mostrar errores de login

  constructor(
    private fb: FormBuilder,
    private autenticacionService: AutenticacionService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      correo: ['', [Validators.required, Validators.email]],
      contrasena: ['', [Validators.required]]
    });
  }

  onSubmit(): void {
    this.errorMensaje = null;
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.autenticacionService.login(this.loginForm.value).subscribe({
      next: (response) => {
        // Guardamos TODOS los datos en localStorage
        localStorage.setItem('token', response.token);
        localStorage.setItem('usuario_nombres', response.nombres);
        localStorage.setItem('usuario_apellidos', response.apellidos);
        localStorage.setItem('usuario_id', response.id);
        localStorage.setItem('usuario_rol', response.rol); // <-- El rol es la clave

        alert(`¡Bienvenido de vuelta, ${response.nombres}!`);

        // --- ¡ESTA ES LA LÓGICA CORREGIDA! ---
        if (response.rol === 'ADMIN') {
          // Si es Admin, lo enviamos a la gestión de usuarios
          this.router.navigate(['/admin/usuarios']); 

        } else if (response.rol === 'TRAINER') {
          // Si es Trainer, lo enviamos a su dashboard
          this.router.navigate(['/trainer/dashboard']);

        } else {
          // Si es Cliente, lo enviamos a la página principal
          this.router.navigate(['/']);
        }
      },
      error: (error) => {
        console.error('Error en el login:', error);
        this.errorMensaje = 'Correo o contraseña incorrectos. Por favor, verifica tus credenciales.';
      }
    });
  }
}