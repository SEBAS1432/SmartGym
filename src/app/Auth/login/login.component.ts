import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../Core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email = '';
  password = '';
  showPassword = false;
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  constructor(private auth: AuthService, private router: Router) {}

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  onSubmit() {
    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    setTimeout(() => {
      const success = this.auth.login(this.email, this.password);

      if (success) {
        this.successMessage = 'Inicio de sesión exitoso';
        this.router.navigate(['/dashboard']);
      } else {
        this.errorMessage = 'Correo o contraseña incorrectos';
      }

      this.isLoading = false;
    }, 1000);
  }

  onForgotPassword(event: Event) {
    event.preventDefault();
    alert('Funcionalidad en construcción');
  }

  onRegister(event: Event) {
    event.preventDefault();
    alert('Registro aún no implementado');
  }
}
